package edu.unl.cse.csce361.boggle.backend;

import edu.unl.cse.csce361.boggle.backend.network.BoggleClient;
import edu.unl.cse.csce361.boggle.backend.network.BoggleServer;
import edu.unl.cse.csce361.boggle.backend.network.OpCode;
import edu.unl.cse.csce361.boggle.logic.GameManager;
import edu.unl.cse.csce361.boggle.logic.Player;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;
import java.nio.file.Path;
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
    // Observable State Properties
    private BooleanProperty allReady = new SimpleBooleanProperty(false);
    private IntegerProperty nameTaken = new SimpleIntegerProperty(0);
    private IntegerProperty gotAllWords = new SimpleIntegerProperty(0);


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

    /**
     * Checks to see if the player is found
     * @param playerName
     * @return True if found, false if not
     */
    public boolean checkPlayer(String playerName){
      return GameManager.getInstance().getPlayers().stream()
              .map(player -> player.getPlayerName())
              .filter(s -> s.equals(playerName)).count() != 0;
    }

    public Player addPlayer(String playerName){
        return GameManager.getInstance().addPlayer(playerName);
    }

    public String startNetwork() {
        if(isHost){
            // make sure clients is defined
            if(numOfClients == 0){
                return "Number of clients is 0.";
            }
            try {
                this.server = new BoggleServer(numOfClients);
            } catch (IOException e) {
                return "Server failed to start: " + e.getMessage();
            }
            serverThread = new Thread(this.server);
            serverThread.start();
        } else {
            // make sure address and port are set before starting connection
            if(address == null || port == -1){
                return "IP or Port not set!";
            }
            try{
                this.client = new BoggleClient(address, port);
            } catch (IOException e){
                return "Client connection failed to start: " + e.getMessage();
            }
            clientThread = new Thread(this.client);
            clientThread.start();
        }
        return "";
    }

    // Client Comm
    public void setAddress(String address){
        this.address = address;
    }

    public void setPort(int port){
        this.port = port;
    }

    public void getGameBoardFromServer(){
        client.sendDataToServer(OpCode.GAME_BOARD, null);
    }

    public void sendPlayerName(String playerName){
        client.sendDataToServer(OpCode.PLAYER_NAME, playerName);
    };

    public void sendPlayerObject(Player player) {
        client.sendDataToServer(OpCode.WORD_LIST, player);
    }

    public IntegerProperty getNameTakenProperty() {
        return nameTaken;
    }

    // Shared Com
    public BooleanProperty getAllReadyProperty(){
        return this.allReady;
    }

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

    public void hostStartGame(){
        this.server.sendDataToAllClients(OpCode.START_GAME, null);
    }

    public int getNumOfClients(){
        return this.numOfClients;
    }

    public void sendAllScores(){
        server.sendDataToAllClients(OpCode.ALL_SCORES, GameManager.getInstance().getPlayers());
        GameManager.getInstance().getGotAllScoresProperty().setValue(true);
    }

    public IntegerProperty getAllWordsProperty() {
        return this.gotAllWords;
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
