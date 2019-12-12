package edu.unl.cse.csce361.boggle.backend.network;

import java.io.*;
import java.net.*;
import java.util.Vector;

import static java.lang.Thread.sleep;

public class BoggleServer implements Runnable {
    private final String debugName = "Server";
    private ServerSocket socket;
    Vector<Thread> runningThreads;
    Vector<Thread> finishedThreads;
    Vector<ClientHandler> clients;
    private final int numOfClients;
    private boolean serverRunning;
    private boolean allConnected;

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

    /**
     * Called from client handler, moves thread from running thread list to
     * finished thread list ready to get joined and closed up.
     * @param thread
     */
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
                                NetworkUtils.debugPrint(debugName, "ERROR: Can't access network interfaces!");
                            }
                            return result;
                        })
                        .flatMap(networkInterface -> networkInterface.getInterfaceAddresses().stream())
                        .filter(interfaceAddress -> interfaceAddress.getAddress().isSiteLocalAddress())
                        .map(interfaceAddress -> interfaceAddress.getAddress()).findFirst().orElse(null);
            } catch (SocketException e) {
                NetworkUtils.debugPrint(debugName, "ERROR: Can't access network interfaces!");
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
    public synchronized void sendDataToClient(int clientIndex, OpCode code){
        this.clients.get(clientIndex).queueData(code);
    }

    /**
     * Allows for upper level functions to send data to all clients. Used by host to get info
     * from all clients or to notify them of changes.
     * @param code
     */
    public synchronized void sendDataToAllClients(OpCode code){
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
                NetworkUtils.debugPrint(debugName, "New client request received : " + clientSocket);
                this.clients.add(new ClientHandler(this, clientSocket));
                this.runningThreads.add(new Thread(this.clients.lastElement()));
                this.runningThreads.lastElement().start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // If all clients have connected, wait to start cleanup
            if(this.runningThreads.size() == this.numOfClients){
                // Wait till all connect
                while(!allConnected){
                    if(numOfClients == clients.stream().filter(clientHandler -> clientHandler.isConnected()).count()){
                        allConnected = true;
                        break;
                    }
                    try {
                        sleep(500L);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

                // While the threads aren't finished, wait
                while(this.finishedThreads.size() != this.numOfClients){
                    try {
                        sleep(5000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // Join the threads
                try {
                    for (Thread finishedThread : finishedThreads) {
                        NetworkUtils.debugPrint(debugName, "Joining thread " + finishedThread.getName());
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

}
