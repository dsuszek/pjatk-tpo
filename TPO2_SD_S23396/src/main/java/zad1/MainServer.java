package zad1;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class MainServer extends Thread {

    // this server is only an intermediary - it gets data from client and creates another client
    private static ServerSocket serverSocket;
    private Socket translationServerSocket;
    private static String host = "localhost";
    private static final int port = 10000;
    private PrintWriter output;
    private BufferedReader input;
    private boolean isServerRunning;

    public static Map<String, Integer> languages = new HashMap<>() {{
        put("EN", 10001);
        put("FR", 10002);
        put("DE", 10003);
        put("FI", 10005);
        put("PT", 10006);
        put("UK", 10007);
        put("TR", 10009);
        put("IT", 10023);
        put("ES", 10024);
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


        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                ServerSocket serverTranslatorSocket = null;
                try {
                    serverTranslatorSocket = new ServerSocket();
                    InetSocketAddress isa = new InetSocketAddress(host, getPortOfTranslationServer(targetLang));
                    serverTranslatorSocket.bind(isa);
                    ServerTranslator serverTranslator = new ServerTranslator(serverTranslatorSocket);
                    serverTranslator.start();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        serverTranslatorSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        // Send request to server - translator
        translationServerSocket = new Socket(InetAddress.getByName("localhost"), translationServerPort);

        output = new PrintWriter(
                translationServerSocket.getOutputStream(), true);
        makeRequest(targetLang  + " " + textToTranslate + " " + targetPort);
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
            output.println(message);
        }
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
