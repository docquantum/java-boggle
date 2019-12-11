package edu.unl.cse.csce361.boggle.backend;

import edu.unl.cse.csce361.boggle.backend.network.BoggleClient;
import edu.unl.cse.csce361.boggle.backend.network.BoggleServer;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class NetworkTester {

    @Test
    public void testClientServerConnection(){
        BoggleServer server = null;
        try {
            server = new BoggleServer(12345, 1);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        BoggleClient client = null;
        try{
            client = new BoggleClient("127.0.0.1", 12345);
        } catch (IOException e){
            e.printStackTrace();
            assertTrue(false);
        }
        server.run();
        client.run();
    }
    @Test
    public void testMultiClientServerConnection(){
        BoggleServer server = null;
        try {
            server = new BoggleServer(12345, 1);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        BoggleClient client = null;
        BoggleClient client2 = null;
        BoggleClient client3 = null;
        BoggleClient client4 = null;
        try{
            client = new BoggleClient("127.0.0.1", 12345);
            client2 = new BoggleClient("127.0.0.1", 12345);
            client3 = new BoggleClient("127.0.0.1", 12345);
            client4 = new BoggleClient("127.0.0.1", 12345);
        } catch (IOException e){
            e.printStackTrace();
            assertTrue(false);
        }
        new Thread(server).start();
        new Thread(client).start();
        new Thread(client2).start();
        new Thread(client3).start();
        new Thread(client4).start();

    }
}
