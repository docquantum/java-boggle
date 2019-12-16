package edu.unl.cse.csce361.boggle.backend.network;

import edu.unl.cse.csce361.boggle.backend.BackendManager;
import edu.unl.cse.csce361.boggle.logic.GameManager;
import edu.unl.cse.csce361.boggle.logic.Player;
import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

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
    private boolean gameBoardReceived;

    public boolean isRunning() {
        return running;
    }

    public BoggleClient(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        this.outStream = new ObjectOutputStream(this.socket.getOutputStream());
        this.inStream = new ObjectInputStream(this.socket.getInputStream());
        this.dataQueue = new LinkedList<>();
        this.running = true;
        this.gameBoardReceived = false;
        self = this;
    }

    public void stopClient() {
        NetworkUtils.debugPrint(debugName,"Stopping client");
        running = false;
        synchronized (selfThread){
            selfThread.notify();
        }
        try {
            inStream.close();
            outStream.close();
            socket.close();
        } catch (IOException e) {
            NetworkUtils.debugPrint(debugName, "Closing streams");
        }
        try{
            sendData.join();
            getData.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public synchronized void sendDataToServer(OpCode code, Object data){
        dataQueue.add(new Pair<OpCode, Object>(code, data));
        synchronized (sendData){
            sendData.notify();
        }
    }

    public void setGameBoardReceived(boolean gameBoardReceived) {
        this.gameBoardReceived = gameBoardReceived;
    }

    @Override
    public void run() {
        selfThread = Thread.currentThread();
        NetworkUtils.debugPrint(debugName, "Client connected to " + socket);
        /*
         * Implements the server/client threads to get and pass
         * info to clients
         */

        /**
         * Gets code from client, then figures out what to do with that code
         * and send back data to client it need by by calling the sister thread.
         */
        this.getData = new Thread(() -> {
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
                            GameManager.getInstance().setLocalPlayer((Player) data.getValue());
                            BackendManager.getInstance().getNameTakenProperty().setValue(BackendManager.getInstance().getNameTakenProperty().get() - 1);
                            new Thread(() -> {
                                while(!self.gameBoardReceived){
                                    BackendManager.getInstance().getGameBoardFromServer();
                                    try {
                                        Thread.currentThread().sleep(5000L);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }).start();
                            break;
                        case GAME_BOARD:
                            if(!self.gameBoardReceived){
                                GameManager.getInstance().setGameBoard((String[][]) data.getValue());
                                self.setGameBoardReceived(true);
                            }
                            break;
                        case START_GAME:
                            BackendManager.getInstance().getAllReadyProperty().setValue(true);
                            break;
                        case FINISHED:
                            //TODO GameManager.getInstance().endGame();
                            break;
                        case WORD_LIST:
                            sendDataToServer(OpCode.WORD_LIST, GameManager.getInstance().getLocalPlayer());
                            break;
                        case ALL_SCORES:
                            if(data.getValue() instanceof HashSet){
                                GameManager.getInstance().setPlayers((HashSet<Player>) data.getValue());
                                GameManager.getInstance().getPlayers().forEach(p -> System.out.println(p.getPlayerName() + p.getPlayerScore()));
                                NetworkUtils.debugPrint(debugName, data.getValue().toString());
                            }
                            GameManager.getInstance().getGotAllScoresProperty().setValue(true);
                            break;
                        case EXIT:
                            self.running = false;
                            break;
                        default:
                            break;
                    }
                } catch (IOException e){
                    if(self.isRunning()){
                        e.printStackTrace();
                        self.running = false;
                    }
                    NetworkUtils.debugPrint(debugName, "Connection Closed");
                } catch(ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * Sends data to the client, waits to be woken to send something.
         */
        this.sendData = new Thread(() -> {
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
                        case GAME_BOARD:
                            // Ask server for board
                            // Send player name to server
                            outStream.writeObject(dataQueue.poll());
                            break;
                        case START_GAME:
                            // Tell server client has started game
                            outStream.writeObject(dataQueue.poll());
                            break;
                        case FINISHED:
                            // Tell server client has finished and is waiting
                            outStream.writeObject(OpCode.FINISHED);
                            break;
                        case WORD_LIST:
                            // Send server the player object from the player
                            outStream.writeObject(dataQueue.poll());
                            break;
                        case ALL_SCORES:
                            // Ask server to get all scores
                            outStream.writeObject(dataQueue.poll());
                            break;
                        case EXIT:
                            // Tell server that client is exiting
                            outStream.writeObject(dataQueue.poll());
                            break;
                        default:
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        sendData.start();
        getData.start();

        BackendManager.getInstance().getGameBoardFromServer();

        try {
            synchronized (selfThread){
                selfThread.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        NetworkUtils.debugPrint(debugName, "Closing: " + socket);

        stopClient();
    }
}
