package zad1;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    public void getConnection() {
        try {
            // Utworzenie gniazda
            String serverHost = null; // adres IP serwera ("cyfrowo" lub z użyciem DNS)
            int serverPort = null;      // numer portu na którym nasłuchuje serwer

            Socket socket = new Socket(serverHost, serverPort);

            // Uzyskanie strumieni do komunikacji
            OutputStream sockOut = socket.getOutputStream();
            InputStream sockIn = socket.getInputStream();

            // Komunikacja (zależna od protokołu)

            // Wysłanie zlecenia do serwera
            sockOut.write(...);

            // Odczytanie odpowiedzi serwera
            sockIn.read(...);

            // Po zakończeniu komunikacji - zamkniecie strumieni i gniazda
            sockOut.close();
            sockIn.close();
            socket.close();

        } catch (UnknownHostException exc) {
            // nieznany host
        } catch (SocketException exc) {
            // wyjątki związane z komunikacją przez gniazda
        } catch (IOException exc) {
            // inne wyjątki we/wy
        }
    }


    public String getTranslation() {

    }
}
