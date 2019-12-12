package edu.unl.cse.csce361.boggle.backend.network;

public class NetworkUtils {

    public static final String IPV4_REGEX = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
    public static final String PORT_REGEX = "^()([1-9]|[1-5]?[0-9]{2,4}|6[1-4][0-9]{3}|65[1-4][0-9]{2}|655[1-2][0-9]|6553[1-5])$";

    public static void debugPrint(String type, String msg){
        System.out.printf("[%s %s] %s\n", type, Thread.currentThread().getName(), msg);
    }
}
