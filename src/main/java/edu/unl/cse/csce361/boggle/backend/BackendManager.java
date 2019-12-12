package edu.unl.cse.csce361.boggle.backend;

import edu.unl.cse.csce361.boggle.backend.network.BoggleClient;
import edu.unl.cse.csce361.boggle.backend.network.BoggleServer;
import edu.unl.cse.csce361.boggle.backend.network.OpCode;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static edu.unl.cse.csce361.boggle.backend.DictionaryLoader.getFilePathsInDir;
import static edu.unl.cse.csce361.boggle.backend.DictionaryLoader.getLinesInFiles;

public class BackendManager {

    private Set<String> dictionary;
    // Determines server modality
    private boolean isHost = false;
    // Server mode
    private Thread serverThread = null;
    private BoggleServer server = null;
    private int numOfClients = 0;
    // Client mode
    private Thread clientThread = null;
    private BoggleClient client = null;
    private String address = null;
    private int port = -1;
    // Singleton
    private static BackendManager uniqueInstance = null;
    private BooleanProperty allConnected = new SimpleBooleanProperty();

    private BackendManager() {
        this.dictionary = loadDictionary();
    }

    public static BackendManager getInstance() {
        if(uniqueInstance == null){
            uniqueInstance = new BackendManager();
        }
        return uniqueInstance;
    }

    public void setHostMode(boolean host) {
        isHost = host;
    }

    public boolean getHostMode(){
        return this.isHost;
    }

    public void startNetwork() throws IOException {
        if(isHost){
            // make sure clients is defined
            if(numOfClients == 0){
                System.err.println("Number of clients is 0.");
                return;
            }
            try {
                this.server = new BoggleServer(numOfClients);
            } catch (IOException e) {
                System.err.println("Server failed to start: " + e.getMessage());
                return;
            }
            serverThread = new Thread(this.server);
            serverThread.start();
        } else {
            // make sure address and port are set before starting connection
            if(address == null || port == -1){
                System.err.println("IP or Port not set!");
                return;
            }
            try{
                this.client = new BoggleClient(address, port);
            } catch (IOException e){
                System.err.println("Client connection failed to start: " + e.getMessage());
                return;
            }
            clientThread = new Thread(this.client);
            clientThread.start();
        }
    }

    // Client Comm
    public void setAddress(String address){
        this.address = address;
    }

    public void setPort(int port){
        this.port = port;
    }

    public void sendPlayerName(){};

    // Server Comm
    public void setNumOfClients(int numOfClients){
        this.numOfClients = numOfClients;
    }

    public String getAddress(){
        if(!this.serverThread.isAlive()){
            System.err.println("Server not ready!");
            return null;
        }
        return this.server.getAddress();
    }

    public int getPort(){
        if(!this.serverThread.isAlive()){
            System.err.println("Server not ready!");
            return -1;
        }
        return this.server.getPort();
    }

    public BooleanProperty getAllConnectedProperty(){
        return this.server.getAllConnectedProperty();
    }

    public void hostStartGame(){
        this.server.sendDataToAllClients(OpCode.START_GAME);
    }


    // Dictionary tools
    public Set<String> getDictionary() {
        return dictionary;
    }

    public Set<String> loadDictionary(){
        List<Path> path;
        Set<String> dic;
        path = getFilePathsInDir("src/main/resources/words/dicts");
        dic = getLinesInFiles(path);
        return dic;
    }

    public void setDictionary(Set<String> dictionary){
        this.dictionary = dictionary;
    }

}
