package zad1;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer extends Thread {

    // This server is only an intermediary - it gets data from client and creates another client
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private volatile boolean serverRunning = true; // keyword "volatile" makes it possible to work with multiple threads
    private final String host = "localhost";
    private final int mainServerPort = 9000;
    private final InetSocketAddress isa = new InetSocketAddress(host, mainServerPort);

    public void run() {

        try {
            serverSocket.bind(isa);

            while (serverRunning) {
                Socket connection = serverSocket.accept();
                log("Connection established");

                serviceRequests(connection);
                serverSocket.close();
            }

        } catch (IOException e1) {
            System.err.println("IO Exception");
            e1.printStackTrace();
        }

        try {
            serverSocket.close();
        } catch (IOException e1) {
            System.err.println("Exception during closing the server socket in MainServer class");
            e1.printStackTrace();
        }
    }

    // Handles requests from client
    public void serviceRequests(Socket clientSocket) {
        try { // Create the streams
            in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
        } catch (IOException e1) {

            e1.printStackTrace();
        }
    }

    public static void log(String message) {
        System.out.println("[Main server]: " + message);
    }


    public static void main(String[] args) {
    }

}
