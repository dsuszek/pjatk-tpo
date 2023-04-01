package zad1;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class ServerTranslator {

    private ServerSocket serverSocket;
    private BufferedReader in;
    private PrintWriter out;

    public ServerTranslator() {

    }

    private static Map<String, Integer> languages = new HashMap<>() {{
        put("EN", 9001);
        put("FR", 9002);
        put("GE", 9003);
    }};


    public void run() {

    }
}
