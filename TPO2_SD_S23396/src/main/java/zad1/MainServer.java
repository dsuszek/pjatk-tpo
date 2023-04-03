package zad1;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer extends Thread {

    // This server is only an intermediary - it gets data from client and creates another client
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;
    private volatile boolean serverRunning = true; // keyword "volatile" makes it possible to work with multiple threads
    private final String host = "localhost";
    private final int mainServerPort = 9000;


    private int serverID; // <-- a tego uzywamy do rozroznienia serwerow

    public MainServer(){}

    public void start() {

        try {
            serverSocket = new ServerSocket(mainServerPort);
            log("Server created");
            log("Waiting for a client");

            while (serverRunning) {
                log("Connection established");
                log("Listening on port: " + mainServerPort);
                serverSocket.accept();
                log("Accepted");
                serviceRequests(clientSocket);
            }

            serverSocket.close();
        } catch (IOException e1) {
            System.err.println("IO Exception");
            e1.printStackTrace();
        }
    }

    // Handles requests from client
    public void serviceRequests(Socket clientSocket) {
        try { // Create the streams
            input = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            output = new PrintWriter(
                    clientSocket.getOutputStream(), true);

            // String to read message from input
            String line = "";

            // Keep reading until "Over" is input
            while (!line.equals("Over")) {
                try {
                    line = input.readLine();
                    System.out.println(line);
                } catch (IOException e1) {
                    System.err.println("IO Exception");
                    e1.printStackTrace();
                } catch (NullPointerException e2) {
                    System.err.println("Client did not send any message");
                    e2.printStackTrace();
                }
            }

            serverRunning = false;
            input.close();
            output.close();

        } catch (IOException e1) {
            System.err.println("IO Exception in serviceRequests() method");
            e1.printStackTrace();
        }
    }

    public static void log(String message) {
        System.out.println("[Main server]: " + message);
    }


    public static void main(String[] args)   {
        // tutaj musimy stworzyc wiele obiektow

        MainServer mainServer = new MainServer();
        mainServer.start();
    }

}
