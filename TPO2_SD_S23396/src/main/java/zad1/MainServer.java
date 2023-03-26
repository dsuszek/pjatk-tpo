package zad1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {

    // This server is only an intermediary - it gets data from client and sends to server-dictionary
    private static int port = 50001;

    public void getTranslation(String wordToBeTranslated, String clientAddress, int port) {

        try {
            ServerSocket serverSocket = new ServerSocket(50001);
            Socket clientSocket = serverSocket.accept();
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(in)
            );

            String msg = br.readLine();
            System.out.println("Server received: " + msg);

            PrintWriter pw = new PrintWriter(out, true);

        } catch (IOException e) {
            System.err.println("IO Exception caught. Please check the correctness of input data.");
            e.printStackTrace();
        }
    }
}
