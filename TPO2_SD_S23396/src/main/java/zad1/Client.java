package zad1;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {

    private long id;
    private final int clientPort = 50000;
    private static final int mainServerPort = 50001;
    private String request;
    private String wordToBeTranslated;
    private String languageCode;

    // Klient przekazuje do serwera głównego zapytanie w postaci:
    // {"polskie słowo do przetłumaczenia", "kod języka docelowego", port}.

    public static String getTranslation(String wordInPolish, String languageCode) {

        String line = null;

        try {
            // Utworzenie gniazda
            Socket socket = new Socket("localhost", mainServerPort);

            // Uzyskanie strumieni do komunikacji
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );

            OutputStream sockOut = socket.getOutputStream();
            InputStream sockIn = socket.getInputStream();

            // Komunikacja (zależna od protokołu)

            // Wysłanie zlecenia do serwera
            sockOut.write(Integer.parseInt(wordInPolish));

            // Odczytanie odpowiedzi serwera
            sockIn.read();
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            // Po zakończeniu komunikacji - zamkniecie strumieni i gniazda
            sockOut.close();
            sockIn.close();
            socket.close();

        } catch (UnknownHostException e1) {
            System.err.println("Unknown host.");
            e1.printStackTrace();
        } catch (SocketException e2) {
            System.err.println("Problem with communication via sockets.");
            e2.printStackTrace();
        } catch (IOException e3) {
            System.err.println("IO Exception");
            e3.printStackTrace();
        }

        return line;
    }


    public static String getTranslation() {
        return null;
    }
}
