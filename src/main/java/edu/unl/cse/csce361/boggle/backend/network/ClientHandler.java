package edu.unl.cse.csce361.boggle.backend.network;

import edu.unl.cse.csce361.boggle.logic.GameManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.PriorityQueue;
import java.util.Queue;

public class ClientHandler implements Runnable {
    private final BoggleServer boggleServer;
    private final String debugName = "CH";
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
    private Socket socket;
    private Thread sendData;
    private Thread getData;
    private Thread selfThread;
    private Queue<OpCode> codeQueue;
    private boolean handlerRunning;

    public ClientHandler(BoggleServer boggleServer, Socket socket) throws IOException {
        this.boggleServer = boggleServer;
        this.socket = socket;
        this.outStream = new ObjectOutputStream(this.socket.getOutputStream());
        this.inStream = new ObjectInputStream(this.socket.getInputStream());
        this.handlerRunning = true;
        this.codeQueue = new PriorityQueue<>();
    }

    public void stopHandler() throws InterruptedException, IOException {
        NetworkUtils.debugPrint(debugName,"Closing...");
        handlerRunning = false;
        synchronized (sendData){
            sendData.notify();
        }
        inStream.close();
        outStream.close();
        socket.close();
        getData.join();
        sendData.join();
        boggleServer.cleanUpThread(Thread.currentThread());
    }

    public synchronized void queueData(OpCode code){
        codeQueue.add(code);
        synchronized (sendData){
            sendData.notify();
        }
    }

    /**
     * Implements the server/client threads to get and pass
     * info to clients
     */
    @Override
    public void run(){
        this.selfThread = Thread.currentThread();
        NetworkUtils.debugPrint(debugName, "Connecting to client with: " + socket);

        /**
         * Gets code from client, then figures out what to do with that code
         * and send back data to client it need by by calling the sister thread.
         */
        this.getData = new Thread(new Runnable() {
            @Override
            public void run() {
                while(handlerRunning){
                    try {
                        NetworkUtils.debugPrint(debugName, "Waiting for input...");
                        OpCode code = (OpCode) inStream.readObject();
                        NetworkUtils.debugPrint( debugName, "received " + code.toString());
                        switch (code){
                            case PLAYER_NAME:
                                // Got player name from client
                                System.out.println((String) inStream.readObject());
                                break;
                            case GAME_BOARD:
                                // Client asked for game board
                                queueData(OpCode.GAME_BOARD);
                                break;
                            case START_GAME:
                                // Client started game
                                break;
                            case FINISHED:
                                // client finished game
                                break;
                            case WORD_LIST:
                                // Got word list from client
                                //TODO GameManager.getInstance().addWordList(inStream.readObject());
                                break;
                            case ALL_SCORES:
                                // Client wants all the scores
                                queueData(OpCode.ALL_SCORES);
                                break;
                            case EXIT:
                                // Client has exited
                                stopHandler();
                                break;
                            default:
                                break;
                        }
                    } catch (IOException | ClassNotFoundException | InterruptedException e) {
                        e.printStackTrace();
                        handlerRunning = false;
                        synchronized (selfThread){
                            selfThread.notify();
                        }
                    }
                }
            }
        });

        /**
         * Sends a data to the client, waits to be woken to send something.
         */
        this.sendData = new Thread(new Runnable() {
            @Override
            public void run() {
                while(handlerRunning){
                    if(codeQueue.isEmpty()){
                        try {
                            // waiting until woken
                            NetworkUtils.debugPrint(debugName, "Waiting until woken...");
                            synchronized (Thread.currentThread()){
                                Thread.currentThread().wait();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if(!handlerRunning){
                        break;
                    }
                    try {
                        NetworkUtils.debugPrint(debugName, "Got pinged for " + codeQueue.peek());
                        switch (codeQueue.poll()) {
                            case PLAYER_NAME:
                                // Asking player for player name
                                outStream.writeObject(OpCode.PLAYER_NAME);
                                break;
                            case GAME_BOARD:
                                // Telling player it's sending game board.
                                outStream.writeObject(OpCode.GAME_BOARD);
                                outStream.writeObject(GameManager.getInstance().getBoard());
                                break;
                            case START_GAME:
                                // Telling player to start game
                                outStream.writeObject(OpCode.START_GAME);
                                break;
                            case FINISHED:
                                // Telling player everyone has finished
                                outStream.writeObject(OpCode.FINISHED);
                                break;
                            case WORD_LIST:
                                // Telling client to send word list
                                outStream.writeObject(OpCode.WORD_LIST);
                                break;
                            case ALL_SCORES:
                                // Sending scores to client
                                outStream.writeObject(OpCode.ALL_SCORES);
                                //TODO outStream.writeObject(GameManager.getInstance().getScores());
                                break;
                            case EXIT:
                                // Telling client server is exiting
                                outStream.writeObject(OpCode.EXIT);
                                stopHandler();
                                break;
                            default:
                                break;
                        }
                    } catch (IOException | InterruptedException e){
                        e.printStackTrace();
                        handlerRunning = false;
                        synchronized (selfThread){
                            selfThread.notify();
                        }
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
        try {
            stopHandler();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
