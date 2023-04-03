package zad1;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientStart extends Thread {

    private Socket clientStartSocket;
    private BufferedReader input;
    private PrintWriter output;


    // Klient przekazuje do serwera głównego zapytanie w postaci:
    // {"polskie słowo do przetłumaczenia", "kod języka docelowego", port}.

    public void start() {

        log("inside method start()");
        String serverHost = "localhost";
        int mainServerPort = 9000;

        // Establish a connection
        try {

            log("Trying to connect to server socket with parameters: host - " + serverHost + " and port " + mainServerPort);
            clientStartSocket = new Socket(serverHost, mainServerPort);
            log("Connected");

            input = new BufferedReader (
//                    new InputStreamReader(clientStartSocket.getInputStream()));
                    new InputStreamReader(System.in));

            output = new PrintWriter (
                    new OutputStreamWriter(clientStartSocket.getOutputStream()), true);

            log("Streams created");


        } catch (UnknownHostException e1) {
            System.err.println("Unknown host");
            e1.printStackTrace();
        } catch (IOException e2) {
            System.err.println("IO Exception");
            e2.printStackTrace();
            return;
        }

        // String to read message from input
        String line = "";

        while (!line.equals("Over")) {
            output.println(line);
        }

        closeConnection();
    }

    public void closeConnection(){
        try {
            input.close();
            output.close();
            clientStartSocket.close();
        } catch (IOException e1) {
            System.err.println("IO Exception");
            e1.printStackTrace();
        }
    }

    public static void log(String message) {
        System.out.println("[C]: " + message);
    }

    public static void main(String[] args) {
        ClientStart clientStart = new ClientStart();
        clientStart.start();
    }

}
