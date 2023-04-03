package Zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;

public class Server {

    private static Map<String, String> mainServerData = new HashMap<>();

    // charset for coding & decoding the buffers
    private static Charset charset = Charset.forName("ISO-8859-2");
    private static final int BSIZE = 1024;

    private ByteBuffer bbuf = ByteBuffer.allocate(BSIZE);

    // request to be processed
    private StringBuffer reqString = new StringBuffer();

    Server() {

        try {

            String host = "localhost";
            int port = 5000;
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); // connection opened
            InetSocketAddress isa = new InetSocketAddress(host, port); // binds the channel's socket to a local address
            serverSocketChannel.bind(isa);

            serverSocketChannel.configureBlocking(false); // the connection will not be blocked for other clients

            Selector selector = Selector.open(); // create and open the selector

            serverSocketChannel.register(selector,
                    SelectionKey.OP_ACCEPT);

            log("I'm waiting");

            while (true) {

                selector.select(); // this operation is blocking - server waits for selector to notify about the readiness
                // of any operations on any channel

                // when some operations are ready to be processed, selector describes them
                Set<SelectionKey> keys = selector.selectedKeys();

                Iterator<SelectionKey> keyIterator = keys.iterator();

                while (keyIterator.hasNext()) { // for each key

                    SelectionKey key = keyIterator.next();

                    keyIterator.remove();

                    if (key.isAcceptable()) {
                        log("Someone connected to me. I'm accepting the connection request.");

                        SocketChannel clientChannel = serverSocketChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        continue;
                    }

                    if (key.isReadable()) { // one of the channels ready for reading
                        SocketChannel clientChannel = (SocketChannel) key.channel();

                        serviceRequest(clientChannel); // service of client's request

                        continue;
                    }

                    if (key.isWritable()) { // one of the channels ready for writing

                        // @TODO finish condition if(key.isWritable)
                        continue;

                    }
                }

            }


        } catch (IOException e1) {
            System.err.println("IO Exception in constructor inside class Server");
            e1.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new Server();
    }

    private static void log(String message) {
        System.out.println("[Server]: " + message);
    }

    private void serviceRequest(SocketChannel socketChannel) {
        // all operations...
        if (!socketChannel.isOpen()) { // if socket channel is still closed
            return;
        }

        // otherwise

        // reads the requests from client (Admin) and processes them
        // it uses buffer defined above

        // po uruchomieniu Zad1.Server nie ma zadnych dostepnych tematow
        // tematy tworzy Zad1.Admin
        // po połączeniu klient powinien dostać listę wszystkich dostępnych tematów

        String command = reqString.toString();
        System.out.println(command);

        String keyOfTopic;
        String topicDescription;

        if (command.equals("Add topic")) {
            // when admin wants to add topic

            keyOfTopic = ""; // @TODO finish the requests
            topicDescription = "";

            if (!mainServerData.containsKey(keyOfTopic)) { // if key hasn't been added yet
                mainServerData.put(keyOfTopic, topicDescription);
            }
        }

    }

}
