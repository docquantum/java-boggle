package edu.unl.cse.csce361.boggle.backend.network;

public class NetworkUtils {

    public static void debugPrint(String type, String msg){
        System.out.printf("[%s %s] %s\n", type, Thread.currentThread().getName(), msg);
    }
}
