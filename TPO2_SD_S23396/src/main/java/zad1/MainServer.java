package zad1;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class MainServer extends Thread {

    // this server is only an intermediary - it gets data from client and creates another client
    private static ServerSocket serverSocket;
    private Socket translationServerSocket;
    private static String host = "localhost";
    private static final int port = 10000;
    private PrintWriter output;
    private BufferedReader input;
    private boolean isServerRunning;
    private static final Map<String, String> languagesPortsMapping = new HashMap<>();

    private static Map<String, Integer> languages = new HashMap<>() {{
        put("EN", 10001);
        put("FR", 10002);
        put("DE", 10003);
    }};

    public MainServer(ServerSocket serverSocket) {
        MainServer.serverSocket = serverSocket;

        log("Server started");
        log("Listening at port: " + serverSocket.getLocalPort());
        log("Bind address: " + serverSocket.getInetAddress());

        serviceConnections();
    }

    public void serviceConnections() {
        isServerRunning = true;

        while (isServerRunning) { // server works all the time
            try {

                Socket connection = serverSocket.accept();
                log("Connection established");

                sendRequestToTranslationServer(connection);

            } catch (IOException e1) {
                System.err.println("IO Exception");
                e1.printStackTrace();
            }
        }

//        try {
//            serverSocket.close();
//        } catch (IOException e2) {
//            e2.printStackTrace();
//        }
    }

    public void sendRequestToTranslationServer(Socket connection) throws IOException {
        // passes the arguments to appropriate translation server
        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        output = new PrintWriter(
                connection.getOutputStream(), true);

        // reading the requests
        String request = input.readLine();
        String targetLang = request.split(" ")[0];
        String textToTranslate = request.split(" ")[1];
        int targetPort = Integer.parseInt(request.split(" ")[2]);
        int translationServerPort = getPortOfTranslationServer(targetLang);

        log("Text to translate: " + textToTranslate);
        log("Target port: " + targetPort);
        log("Target lang: " + targetLang);
        log("Request received: " + request);

        // Send request to server - translator
        translationServerSocket = new Socket(InetAddress.getByName("localhost"), translationServerPort);

        output = new PrintWriter(
                translationServerSocket.getOutputStream(), true);
        makeRequest(textToTranslate + " " + targetPort);
    }

    private int getPortOfTranslationServer(String targetLang) {
        return languages.get(targetLang);
    }


    public void closeConnection() {
        try {
            input.close();
            output.close();
            serverSocket.close();

        } catch (IOException e1) {
            System.err.println("IO Exception while closing connection");
            e1.printStackTrace();
        }
    }

    public void makeRequest(String request) {

        log("Request sent: " + request);

        output.println(request);
    }

    private void writeResponse(String message) {
        if (message != null) {
//            output.write(message + "\n");
            output.println(message);
        }
    }

    private void initializeMapWithPortsLanguages(HashMap<String, String> map) {
        map.put("EN", "10001");
        map.put("FR", "10002");
    }

    public static void log(String message) {
        System.out.println("[Main server]: " + message);
    }


    public static void main(String[] args) {

        try {
            serverSocket = new ServerSocket();
            InetSocketAddress isa = new InetSocketAddress(host, port);
            serverSocket.bind(isa);


        } catch (IOException e) {
            e.printStackTrace();
        }

        new MainServer(serverSocket);

    }
}
