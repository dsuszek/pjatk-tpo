package zad1;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private InetAddress host;
    private int port;
    private ArrayList<SocketChannel> clients;

    private static Map<String, String> mainServerData = new ConcurrentHashMap<>();

    // charset for coding & decoding the buffers
    private Charset charset = Charset.forName("ISO-8859-2");
    private final int BSIZE = 1024;

    private ByteBuffer bbuf = ByteBuffer.allocate(BSIZE);

    // request to be processed
    private StringBuffer reqString = new StringBuffer();

    public Server(InetAddress host, int port) {
        this.host = host;
        this.port = port;
    }

    public static Map<String, String> getMainServerData() {
        return mainServerData;
    }

    public void connect(){
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); // connection opened
            InetSocketAddress isa = new InetSocketAddress(host, port); // binds the channel's socket to a local address
            serverSocketChannel.socket().bind(isa);
            serverSocketChannel.configureBlocking(false); // the connection will not be blocked for other clients

            Selector selector = Selector.open(); // create and open the selector

            serverSocketChannel.register(selector,
                    SelectionKey.OP_ACCEPT);

            clients = new ArrayList<>();

            log("I'm waiting at host: " + host + " and port: " + port);

            while (true) {

                selector.select(); // this operation is blocking - server waits for selector to notify about the readiness
                // of any operations on any channel

                // when some operations are ready to be processed, selector describes them
                Set<SelectionKey> keys = selector.selectedKeys();

                Iterator<SelectionKey> keyIterator = keys.iterator();

                while (keyIterator.hasNext()) { // for each key

                    SelectionKey key = (SelectionKey) keyIterator.next();

                    keyIterator.remove();

                    if (key.isAcceptable()) {
                        log("Someone connected to me. I'm accepting the connection request.");

                        SocketChannel clientChannel = serverSocketChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);

                        clients.add(clientChannel);

                        continue;
                    }

                    if (key.isReadable()) { // one of the channels ready for reading
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        serviceRequest(clientChannel); // service of client's request

                        continue;
                    }

                    if (key.isWritable()) { // one of the channels ready for writing
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        // @TODO finish condition if(key.isWritable)

                    }
                }

            }


        } catch (IOException e1) {
            System.err.println("IO Exception in constructor inside class Server");
            e1.printStackTrace();
        }

    }

    public ArrayList<SocketChannel> getAllClients() {
        return clients;
    }


    private void serviceRequest(SocketChannel socketChannel) {
        // all operations...
        if (!socketChannel.isOpen()) { // if socket channel is still closed
            return;
        }

        log("I'm reading the request from the client...");

        // reading the request
        reqString.setLength(0);
        bbuf.clear();

        try {
            readLoop:
            // Czytanie jest nieblokujące
            while (true) {               // kontynujemy je dopóki
                int n = socketChannel.read(bbuf);   // nie natrafimy na koniec wiersza
                if (n > 0) {
                    bbuf.flip();
                    CharBuffer cbuf = charset.decode(bbuf);
                    while (cbuf.hasRemaining()) {
                        char c = cbuf.get();
                        //System.out.println(c);
                        if (c == '\r' || c == '\n') break readLoop;
                        else {
                            //System.out.println(c);
                            reqString.append(c);
                        }
                    }
                }
            }

            // otherwise

            // reads the requests from client (Admin) and processes them
            // it uses buffer defined above

            // po uruchomieniu Zad1.Server nie ma zadnych dostepnych tematow
            // tematy tworzy Zad1.Admin
            // po połączeniu klient powinien dostać listę wszystkich dostępnych tematów

            String command = reqString.toString();
            String commandCode = "";
            String keyOfTopic = "";
            String topicDescription = "";
            String currentDescription = "";

            String[] actualRequest = command.split(" ", 3);

            log("Request from client: " + command);

            // each request has different flag
            if(command.startsWith("1")) {
                // action "remove topic" - code 1 (2 parameters)

                if(actualRequest.length < 2) {
                    sendString(socketChannel, "Not enough arguments provided. Please check that.");
                    log("Not enough arguments provided. Please check that.");
                }
                commandCode = actualRequest[0];
                keyOfTopic = actualRequest[1];

                if(mainServerData.containsKey(keyOfTopic)) {
                    mainServerData.remove(keyOfTopic);
                } else {
                    sendString(socketChannel, "Topic not available.");
                    log("Topic not available");
                }
            } else if(command.startsWith("2")) {
                // action "add topic" - code 2 (3 parameters)

                if(actualRequest.length < 3) {
                    sendString(socketChannel, "Not enough arguments provided. Please check that.");
                    log("Not enough arguments provided. Please check that.");
                } else {
                    commandCode = actualRequest[0];
                    keyOfTopic = actualRequest[1];
                    topicDescription = actualRequest[2];
                }

                mainServerData.put(keyOfTopic, topicDescription);
            } else if(command.startsWith("3")) {
                // action "update topic" - code 3 (3 parameters)

                if(actualRequest.length < 3) {
                    sendString(socketChannel, "Not enough arguments provided. Please check that.");
                    log("Not enough arguments provided. Please check that.");
                } else {
                    commandCode = actualRequest[0];
                    keyOfTopic = actualRequest[1];
                    topicDescription = actualRequest[2];

                }
                mainServerData.put(keyOfTopic, topicDescription);

            } else if(command.startsWith("4")) {
                // inform all clients about the changes - code 4 (1 parameter)

            } else if(command.startsWith("5")) {
                log("I'm in command.startWith 5 section");
                // action "show all topics" - code 5 (1 parameter)
                List<String> allTopics = mainServerData.keySet().stream().toList();
                for(int i = 0; i < allTopics.size(); i++) {
                    log("Topic: " + allTopics.get(i));
                }
                sendString(socketChannel, String.join(",", allTopics));
            } else {
                sendString(socketChannel, "Unknown command code. Please check that.");
            }

        } catch (IOException e1) {
            System.err.println("IO Exception while servicing the request from client");
            e1.printStackTrace();
        }
    }


    private StringBuffer remsg = new StringBuffer(); // Odpowiedź

    private void sendString(SocketChannel sc, String msg)
            throws IOException {
        remsg.setLength(0);
        remsg.append(' ');
        remsg.append('\n');

        if (msg != null) {
            remsg.append(msg);
            remsg.append('\n');
        }

        log("I'm sending the following response: " + remsg);
        ByteBuffer buf = charset.encode(CharBuffer.wrap(remsg));
        sc.write(buf);
    }


    public void addTopic(String topic, String description) {
        mainServerData.put(topic, description);
    }

    public void getDescription(String topic) {
        mainServerData.get(topic);
    }

    public void removeTopic(String topic) {
//        mainServerData.remove(keyOfTopic);
//        writeResp(socketChannel, "The topic " + keyOfTopic + " was removed from the list.");

    }


    public void getAllTopics(List<String> topics) {
        for (int i = 0; i < topics.size(); i++) {
            log("Key no. " + i + ": " + topics.get(i));
        }
    }


    public static void main(String args[]) throws UnknownHostException {
        Server server = new Server(InetAddress.getByName("localhost"), 10000);
        server.connect();
    }

    private void log(String message) {
        System.out.println("[Server]: " + message);
    }

    private void closeConnection(SocketChannel socketChannel) throws IOException {
        socketChannel.close();
        socketChannel.socket().close();
    }
}
