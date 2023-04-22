package zad1;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Client {

    private InetAddress host;
    private int port;
    private SocketChannel channel = null;
    private Map<String, String> mainServerData = Server.getMainServerData();
    private List<String> listOfAvailableTopics = new ArrayList<>();
    private List<String> listOfSubscribedTopics = new ArrayList<>();

    public List<String> getListOfAvailableTopics() {
        return this.listOfAvailableTopics;
    }

    public List<String> getListOfSubscribedTopics() {
        return this.listOfSubscribedTopics;
    }

    public void subscribeTopic(String topic) {
        listOfSubscribedTopics.add(topic);
    }

    public void unsubscribeTopic(String topic) {
        listOfSubscribedTopics.remove(topic);
    }

    public Client(InetAddress host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        try {

            for (int i = 0; i < mainServerData.size(); i++) {
                log("Topic " + i + " description: " + mainServerData.get(i));
            }

            // creating the channel
            channel = SocketChannel.open();

            // setting non-blocking mode
            channel.configureBlocking(false);

            // binding the socket channel with host and port
            channel.connect(new InetSocketAddress(host, port));

            log("Connecting to the server ...");

            while (!channel.finishConnect()) {
                // ew. pokazywanie czasu łączenia (np. pasek postępu)
                // lub wykonywanie jakichś innych (krótkotrwałych) działań
            }

        } catch (UnknownHostException e1) {
            System.err.println("Uknown host " + host);
            e1.printStackTrace();
        } catch (IOException e2) {
            System.err.println("IO Exception when trying to connect to server.");
            e2.printStackTrace();
        }

        log("I'm connected to the server ...");
    }

    public void readResponse() {

        Charset charset = Charset.forName("ISO-8859-2");

        // Alokowanie bufora bajtowego
        // allocateDirect pozwala na wykorzystanie mechanizmów sprzętowych
        // do przyspieszenia operacji we/wy

        // Uwaga: taki bufor powinien być alokowany jednokrotnie
        // i wielokrotnie wykorzystywany w operacjach we/wy
        int buffer_size = 1024;
        ByteBuffer inBuf = ByteBuffer.allocateDirect(buffer_size);
        CharBuffer cbuf = null;

        try {
            while (true) {
                inBuf.clear(); //
                int readBytes = channel.read(inBuf);

                if (readBytes == 0) {
                    continue;
                } else if (readBytes == -1) { // channel closed on server side, reading isn't possible anymore
                    break;
                } else {
                    inBuf.flip();
                    cbuf = charset.decode(inBuf);
                    String dataFromServer = cbuf.toString();

                    while (dataFromServer != "\n") {
                        log("Server just responded: " + dataFromServer);
                    }
                    cbuf.clear();

                    if (dataFromServer.equals("-1")) {
                        break;
                    }
                }

            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    public void sendRequest(String request) throws IOException {

        log("I'm inside sendRequest method.");
        channel = SocketChannel.open();

        // setting non-blocking mode
        channel.configureBlocking(false);

        // binding the socket channel with host and port
        channel.connect(new InetSocketAddress(host, port));

        while (!channel.finishConnect()) {
        }

        // client writes to server
        Charset charset = Charset.forName("ISO-8859-2");
        CharBuffer cbuf = null;
        cbuf = CharBuffer.wrap(request + "\n");
        ByteBuffer outBuf = charset.encode(cbuf);
        ByteBuffer inBuf = charset.encode(cbuf);

        try {
            channel.write(outBuf);
            cbuf.clear();
            channel.read(inBuf);
            log("Answer from server: " + inBuf);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private static void log(String message) {
        System.out.println("[Client]: " + message);
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client(InetAddress.getByName("localhost"), 10000);
        client.connect();
        client.sendRequest("3 fwfwf fewfwe");
        client.readResponse();
    }

}
