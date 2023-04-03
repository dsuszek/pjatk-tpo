package Zad1;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class Client {

    SocketChannel channel = null;
    String server = "localhost";
    int port = 5000;

    Client() {
        try {
            channel = SocketChannel.open();
            channel.configureBlocking(false); //
        } catch (IOException e1) {
            System.err.println("IO Exception while configuring the socket channel in class Client");
            e1.printStackTrace();
        }

    }

    public void sendRequest() {

    }


}
