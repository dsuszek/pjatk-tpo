package zad1;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientStart extends Thread {

    private Socket clientStartSocket;
    private PrintWriter output;
    private BufferedReader input;
    private String host;
    private int port;

    public ClientStart(String host, int port){
        try {
            clientStartSocket = new Socket(host, port);
            output = new PrintWriter(clientStartSocket.getOutputStream(), true);
            input = new BufferedReader(
                    new InputStreamReader(
                            clientStartSocket.getInputStream()
                    )
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Klient przekazuje do serwera głównego zapytanie w postaci:
    // {"polskie słowo do przetłumaczenia", "kod języka docelowego", port}.

    public void makeRequest(String request) {

        // Establish a connection
        try {

            log("Request: " + request);

            // Communication
            output.println(request);
            // Reading response from server
            String response = input.readLine();
            log("Response from server: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(){
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
        ClientStart clientStart = new ClientStart("localhost", 10000);
        clientStart.start();
        clientStart.makeRequest("EN pies 10412");
    }

}
