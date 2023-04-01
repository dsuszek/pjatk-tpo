package zad1;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.Buffer;

public class TranslateClient {

    private final int serverListenerPort = 9999;
    private PrintWriter printWriter;
    private OutputStream outputStream;
    private ServerSocket serverSocket;

    public TranslateClient() {
    }

    public String connect(String host, String wordToBeTranslated, String languageCode) {

        try {
            Socket clientSocket = new Socket(host, serverListenerPort);
            printWriter = new PrintWriter(outputStream);
            serverSocket = new ServerSocket();
            InetSocketAddress isa = new InetSocketAddress(host, serverListenerPort);


            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String data;
            String url = String.format(
                            "https://api-free.deepl.com/v2/translate?text=%s&source_lang=PL&target_lang=%s&auth_key=%s"
//                            text,
//                            this.countryCode,
//                            APICODE
                    );

            data = Connection.makeRequest(url);
            System.out.println(data);

//            disconnect();
            clientSocket.close();


        } catch (IOException e1) {
            System.err.println("IO Exception");
            e1.printStackTrace();
        }
        return null;
    }

//    public void disconnect() {
//        try {
//            input.close();
//            output.close();
//            clientStartSocket.close();
//        } catch (IOException i) {
//            System.out.println(i);
//        }
//    }

}
