package zad1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Thread {

    private static String host = "localhost";
    private static int serverListenerPort = 9999;
    private PrintWriter output;
    private BufferedReader input;
    private boolean isServerRunning;
    private ServerSocket serverListenerSocket;


    public ServerListener() throws IOException {
        serverListenerSocket = new ServerSocket();
        InetSocketAddress isa = new InetSocketAddress(host, serverListenerPort);
        serverListenerSocket.bind(isa);
        serverListenerSocket.accept();

        log("Server started");
        log("Listening at port: " + serverListenerSocket.getLocalPort());
        log("Bind address: " + serverListenerSocket.getInetAddress());

        start();
    }

    public void run() {
        boolean serverRunning = true;
        while (serverRunning) {
            try {
                Socket connection = serverListenerSocket.accept();
                System.out.println("Connection established by " + serverListenerSocket);
                serviceRequests(connection);

            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }                               // close server socket
        try { serverListenerSocket.close(); } catch (Exception exc) {}
    }


    // Obsługa zleceń od klienta
    private void serviceRequests(Socket connection)
            throws IOException {
        try {
            input = new BufferedReader(                   // utworzenie strumieni
                    new InputStreamReader(
                            connection.getInputStream()));
            output = new PrintWriter(
                    connection.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket();
            InetSocketAddress isa = new InetSocketAddress(host, serverListenerPort);
            serverSocket.bind(isa);

        } catch (IOException e) {
            e.printStackTrace();
        }

        new ServerTranslator(serverSocket);
    }

    public static void log(String message) {
        System.out.println("[Server listener]: " + message);
    }

}
