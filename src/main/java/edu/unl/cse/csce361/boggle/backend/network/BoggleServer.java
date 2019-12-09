package edu.unl.cse.csce361.boggle.backend.network;

import edu.unl.cse.csce361.boggle.logic.GameManager;

import java.io.*;
import java.net.*;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;

import static java.lang.Thread.sleep;

public class BoggleServer implements Runnable {
    private ServerSocket socket;
    Vector<Thread> runningThreads;
    Vector<Thread> finishedThreads;
    Vector<ClientHandler> clients;
    private final int numOfClients;
    private boolean serverRunning;

    /**
     * Builds a new server, will bind to any available port on the system.
     * @param numOfClients number of players
     * @throws IOException
     */
    public BoggleServer(int numOfClients) throws IOException{
        this(0, numOfClients);
    }

    /**
     * Builds a new server, will bind to the port given if it's available,
     * else throw an exception.
     * @param port port to bind to
     * @param numOfClients number of players
     * @throws IOException
     */
    public BoggleServer(int port, int numOfClients) throws IOException {
        this.socket = new ServerSocket(port);
        this.numOfClients = numOfClients;
        this.clients = new Vector<>(numOfClients);
        this.runningThreads = new Vector<>(numOfClients);
        this.finishedThreads = new Vector<>(numOfClients);
    }

    public void stopServer(){
        serverRunning = false;
        try{
            socket.close();
        } catch (IOException e){
            System.err.println(e);
        }
    }

    public synchronized void cleanUpThread(Thread thread){
        this.runningThreads.remove(thread);
        this.finishedThreads.add(thread);
    }

    /**
     * Get's the current address of the server/host. If the host is on a *nix system,
     * Finds the ip of which the main interface uses and returns that ip since on some
     * *nix systems returns as 127.0.1.1.
     * @return
     */
    public String getAddress() {
        String s = null;
        if(System.getProperty("os.name").toLowerCase().matches(".*(nix|nux|aix)+.*")){
            InetAddress n = null;
            try {
                n = NetworkInterface.networkInterfaces()
                        .filter(networkInterface -> {
                            boolean result = false;
                            try {
                                result = !networkInterface.isLoopback();
                            } catch (SocketException e) {
                                System.err.println("[Server] ERROR: Can't access network interfaces!");
                            }
                            return result;
                        })
                        .flatMap(networkInterface -> networkInterface.getInterfaceAddresses().stream())
                        .filter(interfaceAddress -> interfaceAddress.getAddress().isSiteLocalAddress())
                        .map(interfaceAddress -> interfaceAddress.getAddress()).findFirst().orElse(null);
            } catch (SocketException e) {
                System.err.println("[Server] ERROR: Can't access network interfaces!");
            }
            if(n != null){
                return n.toString().substring(1);
            }
        }
        try {
            s = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.err.println("[Server] ERROR: Can't access network interfaces!");
        }
        return s;
    }

    /**
     * Returns the currently set port of the server
     * @return
     */
    public int getPort(){
        return this.socket.getLocalPort();
    }

    /**
     * Allows for upper level functions to send data to a client. Used by host to get info
     * from a client or to notify them of changes.
     * @param clientIndex
     * @param code
     */
    public synchronized void sendDataToClient(int clientIndex, DataCodes code){
        this.clients.get(clientIndex).queueData(code);
    }

    /**
     * Allows for upper level functions to send data to all clients. Used by host to get info
     * from all clients or to notify them of changes.
     * @param code
     */
    public synchronized void sendDataToAllClients(DataCodes code){
        for (ClientHandler client: this.clients) {
            client.queueData(code);
        }
    }

    /**
     * Returns the number of currently connected clients.
     * @return
     */
    public int getNumOfConnectedClients(){
        return this.runningThreads.size();
    }

    /**
     * TODO should be able to get data from a specific client as generic object... Needs thought
     * @param clientIndex
     * @return
     */
    public Object getDataFromClient(int clientIndex){
        return null;
    }

    /**
     * Runs the server and queries clients as needed
     */
    @Override
    public void run(){
        this.serverRunning = true;
        while(serverRunning){
            try {
                Socket clientSocket = socket.accept();
                System.out.println("[Server] New client request received : " + clientSocket);
                this.clients.add(new ClientHandler(clientSocket));
                this.runningThreads.add(new Thread(this.clients.lastElement()));
                this.runningThreads.lastElement().start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(this.runningThreads.size() == this.numOfClients){
                while(this.finishedThreads.size() != this.numOfClients){
                    try {
                        sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    for (Thread finishedThread : finishedThreads) {
                        System.out.println("[Server] Joining thread " + finishedThread.getName());
                        finishedThread.join();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    stopServer();
                }
            }
        }
    }

    public class ClientHandler implements Runnable {
        private ObjectInputStream inStream;
        private ObjectOutputStream outStream;
        private Socket socket;
        private Thread sendData;
        private Thread getData;
        private Thread selfThread;
        private Queue<DataCodes> codeQueue;
        private boolean handlerRunning;

        public ClientHandler(Socket socket) throws IOException{
            this.socket = socket;
            this.outStream = new ObjectOutputStream(this.socket.getOutputStream());
            this.inStream = new ObjectInputStream(this.socket.getInputStream());
            this.handlerRunning = true;
            this.codeQueue = new PriorityQueue<>();
        }

        public void stopHandler() throws InterruptedException, IOException {
            System.out.println("Closing CH...");
            handlerRunning = false;
            synchronized (sendData){
                sendData.notify();
            }
//            inStream.close();
//            outStream.close();
            socket.close();
            getData.join();
            sendData.join();
            cleanUpThread(Thread.currentThread());
        }

        public synchronized void queueData(DataCodes code){
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
            System.out.println("[CH " + Thread.currentThread().getName() + "] Connecting to client with: " + socket);

            /**
             * Gets code from client, then figures out what to do with that code
             * and send back data to client it need by by calling the sister thread.
             */
            this.getData = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(handlerRunning){
                        try {
                            System.out.println("[CH " + Thread.currentThread().getName() + "] Waiting for input...");
                            DataCodes code = (DataCodes) inStream.readObject();
                            System.out.println("[CH " + Thread.currentThread().getName() + "] recieved " + code.toString());
                            switch (code){
                                case PLAYER_NAME:
                                    System.out.println((String) inStream.readObject());
                                    break;
                                case ALL_PLAYERS:
                                    break;
                                case GAME_BOARD:
                                    queueData(DataCodes.GAME_BOARD);
                                    break;
                                case WORD_LIST:
                                    break;
                                case ALL_SCORES:
                                    break;
                                case SCORE:
                                    break;
                                case EXIT:
                                    // set up exit stack...
                                    break;
                                default:
                                    break;
                            }
                        } catch (IOException | ClassNotFoundException e) {
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
                                System.out.println("[CH " + Thread.currentThread().getName() + "] Waiting until woken...");
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
                            System.out.println("[CH " + Thread.currentThread().getName() + "] Got pinged for " + codeQueue.peek());
                            switch (codeQueue.poll()) {
                                case PLAYER_NAME:
                                    System.out.println("Asking client to send name");
                                    outStream.writeObject(DataCodes.PLAYER_NAME);
                                    break;
                                case ALL_PLAYERS:
                                    break;
                                case GAME_BOARD:
                                    outStream.writeObject(DataCodes.GAME_BOARD);
                                    outStream.writeObject(GameManager.getInstance().getBoard());
                                    break;
                                case WORD_LIST:
                                    break;
                                case ALL_SCORES:
                                    break;
                                case SCORE:
                                    break;
                                case EXIT:
                                    outStream.writeObject(DataCodes.EXIT);
                                    handlerRunning = false;
                                    synchronized (selfThread){
                                        selfThread.notify();
                                    }
                                    break;
                                default:
                                    break;
                            }
                        } catch (IOException e){
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
}
