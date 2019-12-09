package edu.unl.cse.csce361.boggle.backend.network;

import java.io.*;
import java.net.Socket;
import java.util.PriorityQueue;
import java.util.Queue;

public class BoggleClient implements Runnable{
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private Socket socket;
    private Thread sendData;
    private Thread getData;
    private Thread selfThread;
    private Queue<DataCodes> codeQueue;
    public String player; // TODO: Only used for testing, remove very soon
    private boolean running;

    public BoggleClient(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        this.outStream = new ObjectOutputStream(this.socket.getOutputStream());
        this.inStream = new ObjectInputStream(this.socket.getInputStream());
        this.codeQueue = new PriorityQueue<>();
        this.running = true;
    }

    public void stopClient() throws IOException, InterruptedException {
        System.out.println("Stopping client");
        running = false;
        synchronized (sendData){
            sendData.notify();
        }
//        inStream.close();
//        outStream.close();
        socket.close();
        sendData.join();
        getData.join();
    }

    public synchronized void queueData(DataCodes code){
        codeQueue.add(code);
        synchronized (sendData){
            sendData.notify();
        }
    }

    @Override
    public void run() {
        this.selfThread = Thread.currentThread();
        System.out.println("[Client " + Thread.currentThread().getName() + "] Client connected to " + socket);
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
                        System.out.println("[Client " + Thread.currentThread().getName() + "] Waiting for input...");
                        DataCodes code = (DataCodes) inStream.readObject();
                        System.out.println("[Client " + Thread.currentThread().getName() + "] recieved " + code.toString());
                        switch (code) {
                            case PLAYER_NAME:
                                System.out.println("Client asked to send name");
                                queueData(DataCodes.PLAYER_NAME);
                                break;
                            case ALL_PLAYERS:
                                break;
                            case GAME_BOARD:
                                System.out.println("Client got gameboard");
//                                GameBoard.getInstance().setGameBoard((String[][]) inStream.readObject());
                                String[][] board = (String[][]) inStream.readObject();
                                //Arrays.stream(board).forEach(System.out::println);
                                break;
                            case WORD_LIST:
                                break;
                            case ALL_SCORES:
                                break;
                            case SCORE:
                                break;
                            case EXIT:
                                running = false;
                                synchronized (selfThread){
                                    selfThread.notify();
                                }
                                break;
                            default:
                                break;
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                        running = false;
                        synchronized (selfThread){
                            selfThread.notify();
                        }
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
                    try {
                        // waiting until woken
                        System.out.println("[Client " + Thread.currentThread().getName() + "] Waiting until woken...");
                        synchronized (Thread.currentThread()){
                            Thread.currentThread().wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!running) {
                        break;
                    }
                    try {
                        System.out.println("[Client " + Thread.currentThread().getName() + "] Got pinged for " + codeQueue.peek());
                        switch (codeQueue.poll()) {
                            case PLAYER_NAME:
                                outStream.writeObject(DataCodes.PLAYER_NAME);
                                outStream.writeObject(player);
                                break;
                            case ALL_PLAYERS:
                                break;
                            case GAME_BOARD:
                                // It's the client, no functionality
                                break;
                            case WORD_LIST:
                                break;
                            case ALL_SCORES:
                                break;
                            case SCORE:
                                break;
                            case EXIT:
                                outStream.writeUTF(DataCodes.EXIT.toString());
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

        System.out.println("[Client " + Thread.currentThread().getName() + "] Closing: " + socket);

        try {
            stopClient();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
