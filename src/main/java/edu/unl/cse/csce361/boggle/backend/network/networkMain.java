package edu.unl.cse.csce361.boggle.backend.network;

import java.io.IOException;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class networkMain {

    public static void main(String[] args) throws InterruptedException {
        BoggleServer server = null;
        try {
            server = new BoggleServer(12345, 4);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Thread s1 = new Thread(server);
        s1.start();

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
            return;
        }
        Thread c1 = new Thread(client);
        Thread c2 = new Thread(client2);
        Thread c3 = new Thread(client3);
        Thread c4 = new Thread(client4);

        c1.start();
        c2.start();
        c3.start();
        c4.start();

        sleep(100);
        server.getClients().get(0).sendData(DataCodes.GAME_BOARD);
        sleep(100);
        client.player = "Player 1";
        server.getClients().get(0).sendData(DataCodes.PLAYER_NAME);
        sleep(100);
        server.getClients().get(0).sendData(DataCodes.EXIT);
        server.getClients().get(1).sendData(DataCodes.EXIT);
        server.getClients().get(2).sendData(DataCodes.EXIT);
        server.getClients().get(3).sendData(DataCodes.EXIT);

        try{
            System.out.println("[Main] joining Client 1");
            c1.join();
            System.out.println("[Main] joining Client 2");
            c2.join();
            System.out.println("[Main] joining Client 3");
            c3.join();
            System.out.println("[Main] joining Client 4");
            c4.join();
            System.out.println("[Main] joining Server");
            s1.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
