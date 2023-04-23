package zad1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.*;


public class ServerTranslator extends Thread {

    private static final String API_KEY = "87ad2cd9-fa1f-773f-67a7-e7c15d61fd6d:fx";
    private static final String API_ENDPOINT = "https://api-free.deepl.com/v2/translate";
    private static String host = "localhost";
    private PrintWriter output;
    private BufferedReader input;
    private static int port = 10001;
    private ServerSocket serverSocket;
    private static String request;

    public ServerTranslator(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;

        log("Server started");
        log("Listening at port: " + serverSocket.getLocalPort());
        log("Bind address: " + serverSocket.getInetAddress());

        serviceConnections();
    }

    public void serviceConnections() {

        try {
            Socket connection = serverSocket.accept();
            log("Connection established");

            String translation = sendRequestToTranslationServer(connection);
            sendTranslationToClient(translation);
        } catch (IOException e1) {
            System.err.println("IO Exception");
            e1.printStackTrace();
        }
    }

    public String sendRequestToTranslationServer(Socket connection) throws IOException {


        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        output = new PrintWriter(
                connection.getOutputStream(), true);

        // reading the requests
        request = input.readLine();
        String targetLang = request.split(" ")[0];
        String textToTranslate = request.split(" ")[1];

        log("Text to translate: " + textToTranslate);
        log("Target lang: " + targetLang);

        URL url = new URL(API_ENDPOINT);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpURLConnection.setRequestProperty("Authorization", "DeepL-Auth-Key " + API_KEY);

        String data = "source_lang=PL&target_lang=" + targetLang + "&text=" + URLEncoder.encode(textToTranslate, "UTF-8");
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

    public void sendTranslationToClient(String translation) throws IOException {
        String[] splitted = request.split(" ");
        int targetPort = Integer.parseInt(splitted[2]);

        Socket connection = new Socket("localhost", targetPort);

        output = new PrintWriter(
                connection.getOutputStream(), true);
        log("Translation sent: " + translation);

        output.println(translation);
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

        new ServerTranslator(serverSocket);
    }

    public static void log(String message) {
        System.out.println("[Server translator]: " + message);
    }
}
