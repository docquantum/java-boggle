package edu.unl.cse.csce361.boggle.backend.network;

import edu.unl.cse.csce361.boggle.backend.BackendManager;
import edu.unl.cse.csce361.boggle.logic.GameManager;
import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class BoggleClient implements Runnable{
    private final String debugName = "Client";
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private Socket socket;
    private Thread sendData;
    private Thread getData;
    private Thread selfThread;
    private static BoggleClient self;
    private Queue<Pair<OpCode, Object>> dataQueue;
    private boolean running;

    public boolean isRunning() {
        return running;
    }

    public BoggleClient(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        this.outStream = new ObjectOutputStream(this.socket.getOutputStream());
        this.inStream = new ObjectInputStream(this.socket.getInputStream());
        this.dataQueue = new LinkedList<>();
        this.running = true;
        self = this;
    }

    public void stopClient() throws IOException, InterruptedException {
        NetworkUtils.debugPrint(debugName,"Stopping client");
        running = false;
        synchronized (selfThread){
            selfThread.notify();
        }
        inStream.close();
        outStream.close();
        socket.close();
        sendData.join();
        getData.join();
    }

    public synchronized void sendDataToServer(OpCode code, Object data){
        dataQueue.add(new Pair<OpCode, Object>(code, data));
        synchronized (sendData){
            sendData.notify();
        }
    }

    @Override
    public void run() {
        selfThread = Thread.currentThread();
        NetworkUtils.debugPrint(debugName, "Client connected to " + socket);
        /**
         * Implements the server/client threads to get and pass
         * info to clients
         */

        /**
         * Gets code from client, then figures out what to do with that code
         * and send back data to client it need by by calling the sister thread.
         */
        this.getData = new Thread(new Runnable() {
            @Override
            public void run() {
                while (self.isRunning()) {
                    try {
                        if(!self.isRunning()) break;
                        NetworkUtils.debugPrint(debugName, "Waiting for input...");
                        Pair<OpCode, Object> data = (Pair<OpCode, Object>) inStream.readObject();
                        NetworkUtils.debugPrint(debugName, "received " + data.toString());
                        switch (data.getKey()) {
                            case PLAYER_NAME:
                                sendDataToServer(OpCode.PLAYER_NAME, GameManager.getInstance().getPlayerName());
                                break;
                            case NAME_TAKEN:
                                BackendManager.getInstance().getNameTakenProperty().setValue(BackendManager.getInstance().getNameTakenProperty().get() + 1);
                                break;
                            case WAIT_TO_START:
                                BackendManager.getInstance().getNameTakenProperty().setValue(BackendManager.getInstance().getNameTakenProperty().get() - 1);
                                break;
                            case GAME_BOARD:
                                // Server is sending game board
                                GameManager.getInstance().setGameBoard((String[][]) data.getValue());
                                break;
                            case START_GAME:
                                BackendManager.getInstance().getAllReadyProperty().setValue(true);
                                break;
                            case FINISHED:
                                //TODO GameManager.getInstance().endGame();
                                break;
                            case WORD_LIST:
                                //TODO sendDataToServer(OpCode.WORD_LIST, GameManager.getInstance().getPlayerWords());
                                break;
                            case ALL_SCORES:
                                //TODO Figure out what to do with the scores
                                break;
                            case EXIT:
                                stopClient();
                                break;
                            default:
                                break;
                        }
                    } catch (IOException e){
                        if(self.isRunning()){
                            e.printStackTrace();
                        }
                        NetworkUtils.debugPrint(debugName, "Connection Closed");
                    } catch(ClassNotFoundException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        /**
         * Sends data to the client, waits to be woken to send something.
         */
        this.sendData = new Thread(new Runnable() {
            @Override
            public void run() {
                while (self.isRunning()) {
                    if(dataQueue.isEmpty()) {
                        try {
                            // waiting until woken
                            NetworkUtils.debugPrint(debugName, "Waiting until woken...");
                            synchronized (Thread.currentThread()) {
                                Thread.currentThread().wait();
                            }
                        } catch (InterruptedException e) {
                            if(running) {
                                e.printStackTrace();
                                running = false;
                            }
                        }
                    }
                    if (!self.isRunning()) break;
                    try {
                        NetworkUtils.debugPrint(debugName, "Got pinged for " + dataQueue.peek());

                        switch (dataQueue.peek().getKey()) {
                            case PLAYER_NAME:
                                // Send player name to server
                                outStream.writeObject(dataQueue.poll());
                                break;
                            case GAME_BOARD:
                                // Ask server for board
                                outStream.writeObject(OpCode.GAME_BOARD);
                                break;
                            case START_GAME:
                                // Tell server client has started game
                                outStream.writeObject(OpCode.START_GAME);
                                break;
                            case FINISHED:
                                // Tell server client has finished and is waiting
                                outStream.writeObject(OpCode.FINISHED);
                                break;
                            case WORD_LIST:
                                // Send server the words from the player
                                //TODO outStream.writeObject(GameManager.getInstance().getPlayerWords());
                                break;
                            case ALL_SCORES:
                                // Ask server to get all scores
                                outStream.writeObject(OpCode.ALL_SCORES);
                                break;
                            case EXIT:
                                // Tell server that client is exiting
                                outStream.writeUTF(OpCode.EXIT.toString());
                                break;
                            default:
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        sendData.start();
        getData.start();

        try {
            synchronized (Thread.currentThread()){
                Thread.currentThread().wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        NetworkUtils.debugPrint(debugName, "Closing: " + socket);

        try {
            stopClient();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
