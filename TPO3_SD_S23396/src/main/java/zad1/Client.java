package zad1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Client {

    public Client() {

        SocketChannel channel = null;
        String server = "localhost"; // adres hosta serwera
        int port = 10000; // numer portu

        try {
            // creating the channel
            channel = SocketChannel.open();

            // setting non-blocking mode
            channel.configureBlocking(false);

            // connection of channel
            channel.connect(new InetSocketAddress(server, port));

            log("Łączę się z serwerem ...");

            while (!channel.finishConnect()) {
                // ew. pokazywanie czasu łączenia (np. pasek postępu)
                // lub wykonywanie jakichś innych (krótkotrwałych) działań
            }

        } catch (UnknownHostException exc) {
            System.err.println("Uknown host " + server);
            // ...
        } catch (Exception exc) {
            exc.printStackTrace();
            // ...
        }

        log("Jestem połączony z serwerem ...");

        Charset charset = Charset.forName("ISO-8859-2");
        Scanner scanner = new Scanner(System.in);

        // Alokowanie bufora bajtowego
        // allocateDirect pozwala na wykorzystanie mechanizmów sprzętowych
        // do przyspieszenia operacji we/wy
        // Uwaga: taki bufor powinien być alokowany jednokrotnie
        // i wielokrotnie wykorzystywany w operacjach we/wy
        int rozmiar_bufora = 1024;
        ByteBuffer inBuf = ByteBuffer.allocateDirect(rozmiar_bufora);
        CharBuffer cbuf = null;

        log("Wysyłam: Hi");

        try {
            channel.write(charset.encode("Hi\n"));

            while (true) { // pętla czytania
                inBuf.clear(); //
                int readBytes = channel.read(inBuf);

                if (readBytes == 0) {
                    continue;
                } else if (readBytes == -1) { // kanał zamknięty po stronie serwera, dalsze czytanie niemożliwe
                    break;
                } else {
                    inBuf.flip(); // przestawienie bufora
                    cbuf = charset.decode(inBuf);
                    String dataFromServer = cbuf.toString();

                    log("Serwer właśnie odpisał: " + dataFromServer);
                    cbuf.clear();

                    if (dataFromServer.equals("Bye")) {
                        break;
                    }
                }

                // teraz klient pisze do serwera
                String input = scanner.nextLine();
                cbuf = CharBuffer.wrap(input + "\n");
                ByteBuffer outBuf = charset.encode(cbuf);
                channel.write(outBuf);

                log("Piszę: " + input);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        scanner.close();

    }

    private void subscribe(String topic) {

    }

    private void unsubscribe(String topic) {

    }

    private static void log(String message) {
        System.out.println("[Client]: " + message);
    }

    public static void main(String[] args) {
        Client client = new Client();
    }

}
