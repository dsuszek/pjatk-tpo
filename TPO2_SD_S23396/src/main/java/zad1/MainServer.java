package zad1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServer {

    // ten serwer jest pośrednikiem -> tylko zbiera strumienie od klienta i przesyła dalej

    private static int port;

    public void getConnection(int port) {

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();
            InputStream in = clientSocket.getInputStream();
            OutputStream out = clientSocket.getOutputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String msg = br.readLine();
            System.out.println("Server received: " + msg);

            PrintWriter pw = new PrintWriter(out, true);
        } catch (IOException e) {
            System.err.println("IO Exception caught. Please check the correctness of input data.");
            e.printStackTrace();
        }
    }
}
