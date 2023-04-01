package zad1;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientStart extends Thread {

    private static Socket clientStartSocket;
    private static DataInputStream input = null;
    private static DataOutputStream output = null;


    // Klient przekazuje do serwera głównego zapytanie w postaci:
    // {"polskie słowo do przetłumaczenia", "kod języka docelowego", port}.

    public void start() {

        System.out.println("HI IM WORKING !");

        String serverHost = "127.0.0.1";
        int mainServerPort = 50001;

        // establish a connection
        try {
            clientStartSocket = new Socket(serverHost, mainServerPort);
            log("Connected");

            // takes input from terminal
            input = new DataInputStream(System.in);

            // sends output to the socket
            output = new DataOutputStream(
                    clientStartSocket.getOutputStream());
        } catch (UnknownHostException e1) {
            System.err.println("Unknown host");
            e1.printStackTrace();
        } catch (IOException i) {
            System.out.println(i);
            return;
        }

        // string to read message from input
        String line = "";

        // keep reading until "Over" is input
        while (!line.equals("Over")) {
            try {
                line = input.readLine();
                output.writeUTF(line);
            } catch (IOException i) {
                System.out.println(i);
            }
        }

        closeConnection();
    }

    public static void closeConnection(){
        try {
            input.close();
            output.close();
            clientStartSocket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void log(String message) {
        System.out.println("[C]: " + message);
    }

}
