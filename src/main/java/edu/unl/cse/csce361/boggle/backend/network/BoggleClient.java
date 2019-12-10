package edu.unl.cse.csce361.boggle.backend.network;

import edu.unl.cse.csce361.boggle.logic.GameBoard;
import edu.unl.cse.csce361.boggle.logic.GameManager;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class BoggleClient implements Runnable{
    private final String debugName = "Client";
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private Socket socket;
    private Thread sendData;
    private Thread getData;
    private Thread selfThread;
    private Queue<OpCode> codeQueue;
    private boolean running;

    public BoggleClient(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        this.outStream = new ObjectOutputStream(this.socket.getOutputStream());
        this.inStream = new ObjectInputStream(this.socket.getInputStream());
        this.codeQueue = new PriorityQueue<>();
        this.running = true;
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

    public synchronized void sendDataToServer(OpCode code){
        codeQueue.add(code);
        synchronized (sendData){
            sendData.notify();
        }
    }

    @Override
    public void run() {
        this.selfThread = Thread.currentThread();
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
                while (running) {
                    try {
                        NetworkUtils.debugPrint(debugName, "Waiting for input...");
                        OpCode code = (OpCode) inStream.readObject();
                        NetworkUtils.debugPrint(debugName, "received " + code.toString());
                        switch (code) {
                            case PLAYER_NAME:
                                // Server asked for player name
                                sendDataToServer(OpCode.PLAYER_NAME);
                                break;
                            case GAME_BOARD:
                                // Server is sending game board
                                String[][] serverBoard = (String[][]) inStream.readObject();
                                //TODO GameManager.getInstance().setGameBoard(serverBoard);

                                break;
                            case START_GAME:
                                //TODO GameManager.getInstance().startGame();
                                break;
                            case FINISHED:
                                //TODO GameManager.getInstance().endGame();
                                break;
                            case WORD_LIST:
                                sendDataToServer(OpCode.WORD_LIST);
                                break;
                            case ALL_SCORES:
                                Map<String, Integer> scores = ((Map<String, Integer>) inStream.readObject());
                                // figure out what to do with the scores.
                                break;
                            case EXIT:
                                stopClient();
                                break;
                            default:
                                break;
                        }
                    } catch (IOException e){
                        if(running){
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
                while (running) {
                    if(codeQueue.isEmpty()) {
                        try {
                            // waiting until woken
                            NetworkUtils.debugPrint(debugName, "Waiting until woken...");
                            synchronized (Thread.currentThread()) {
                                Thread.currentThread().wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!running) {
                        break;
                    }
                    try {
                        NetworkUtils.debugPrint(debugName, "Got pinged for " + codeQueue.peek());
                        switch (codeQueue.poll()) {
                            case PLAYER_NAME:
                                // Send player name to server
                                //TODO outStream.writeObject(GameManager.getInstance().getPlayerName());
                                outStream.writeObject("Default Test");
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
