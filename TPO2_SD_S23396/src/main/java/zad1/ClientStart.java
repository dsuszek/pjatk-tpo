package zad1;

import java.io.*;
import java.net.*;

public class ClientStart extends Thread implements Runnable {

    private Socket clientStartSocket;
    private PrintWriter output;
    private BufferedReader input;
    private static final String host = "localhost";
    private static final int serverPort = 10000;
    private static final int myPort = 9999;
    //-----------------------------------------------------------------------------------------------

    private ServerSocket ss;

    public ClientStart(String data) {
        try {
            clientStartSocket = new Socket(host, serverPort);
            output = new PrintWriter(clientStartSocket.getOutputStream(), true);
            input = new BufferedReader(
                    new InputStreamReader(
                            clientStartSocket.getInputStream()
                    )
            );
            //-----------------------------------------------------------------------------------------------
            makeRequest(data);
            ss = new ServerSocket();
            int port = Integer.parseInt(data.split(" ")[2]);
            InetSocketAddress isa = new InetSocketAddress("localhost", port);
            ss.bind(isa);
            System.out.println("Waiting for response");
            Socket connection = ss.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String responseFromTranslationServer = reader.readLine();
            System.out.println(responseFromTranslationServer);
            connection.close();
            ss.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // klient przekazuje do serwera głównego zapytanie w postaci:
    // {"polskie słowo do przetłumaczenia", "kod języka docelowego", port}.

    public void makeRequest(String request) {

        log("Request: " + request);

        // send request
        output.println(request);
    }

    public void closeConnection() {
        try {
            input.close();
            output.close();
            clientStartSocket.close();
        } catch (IOException e1) {
            System.err.println("IO Exception while closing connection");
            e1.printStackTrace();
        }
    }

    public static void log(String message) {
        System.out.println("[Client]: " + message);
    }

    public static void main(String[] args) {
        ClientStart clientStart = new ClientStart("EN pies 10010");

        clientStart.closeConnection();

    }

    public static int getMyPort() {
        return myPort;
    }
}
