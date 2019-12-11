package edu.unl.cse.csce361.boggle.backend;

import edu.unl.cse.csce361.boggle.backend.network.BoggleClient;
import edu.unl.cse.csce361.boggle.backend.network.BoggleServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class NetworkManager {
    private boolean isServer;
    private BoggleClient client;
    private BoggleServer server;
    static NetworkManager instance = null;

    public static NetworkManager getInstance(boolean isServer){
        if(instance == null){
            instance = new NetworkManager(isServer);
        }
        return instance;
    }

    private NetworkManager(boolean isServer){
        this.isServer = isServer;
    }

    public void run (){
        if(isServer){
            startServer();
            startClient();
        }
    }

    private void startServer(){

    }

    private void startClient(){

    }
}
