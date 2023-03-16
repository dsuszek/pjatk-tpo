package zad2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Connection {

    public static String makeRequest(String UrlAPI) throws IOException {

        String s = "";

        try {
            URL url = new URL(UrlAPI);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
                String line;
                while ((line = in.readLine()) != null)
                    s += line;
            }
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}
