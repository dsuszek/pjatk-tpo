package zad1;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MainServer extends Thread {

    private static final String API_KEY = "87ad2cd9-fa1f-773f-67a7-e7c15d61fd6d:fx";
    private static final String API_ENDPOINT = "https://api-free.deepl.com/v2/translate";

    // This server is only an intermediary - it gets data from client and creates another client
    private ServerSocket serverSocket = null;
    private static String host = "localhost";
    private static final int port = 10000;
    private PrintWriter output;
    private BufferedReader input;
    private boolean isServerRunning;
    private static final Map<String, String> languagesPortsMapping = new HashMap<>();
    private static String sourceLang = "PL";


    public MainServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;

        log("Server started");
        log("Listening at port: " + serverSocket.getLocalPort());
        log("bind address: " + serverSocket.getInetAddress());

        serviceConnections();
    }

    public void serviceConnections() {
        isServerRunning = true;

        while (isServerRunning) { // server works all the time
            try {

                Socket connection = serverSocket.accept();
                log("Connection established");
                getTranslation(connection);

            } catch (IOException e1) {
                System.err.println("IO Exception");
                e1.printStackTrace();
            }
        }

        try {
            serverSocket.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    // Handles requests from client
    public String getTranslation(Socket connection) throws IOException {

            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            output = new PrintWriter(
                    connection.getOutputStream(), true);

            // reading the requests
            String request = input.readLine();


            String targetLang = request.split(" ")[0];
            String textToTranslate = request.split(" ")[1];
            String targetPort = request.split(" ")[2];

            log("target lang: " + targetLang);
            log("text to translate: " + textToTranslate);
            log("target port: " + targetPort);

            URL url = new URL(API_ENDPOINT);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("Authorization", "DeepL-Auth-Key " + API_KEY);

            String data = "source_lang=" + sourceLang + "&target_lang=" + targetLang + "&text=" + URLEncoder.encode(textToTranslate, "UTF-8");
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            os.write(data.getBytes("UTF-8"));
            os.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            System.out.println(response);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(String.valueOf(response));

            log(String.valueOf(json.path("translations").get(0).path("text")).replace("\"", ""));
            return String.valueOf(json.path("translations").get(0).path("text")).replace("\"", "");
    }


    public void closeConnection() {
        try {
            input.close();
            output.close();
            serverSocket.close();

        } catch (IOException e1) {
            System.err.println("IO Exception while closing connection");
            e1.printStackTrace();
        }
    }

    private void writeResponse(String message) {
        if (message != null) {
//            output.write(message + "\n");
            output.println(message);
        }
    }

    private void initializeMapWithPortsLanguages(HashMap<String, String> map) {
        map.put("EN", "10001");
        map.put("FR", "10002");
    }

    public static void log(String message) {
        System.out.println("[Main server]: " + message);
    }


    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket();
            InetSocketAddress isa = new InetSocketAddress(host, port);
            serverSocket.bind(isa);


        } catch (IOException e) {
            e.printStackTrace();
        }

        new MainServer(serverSocket);

    }
}
