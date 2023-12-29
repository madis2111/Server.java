package chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Server {
    private ServerSocket serverSocket;
    private BufferedWriter logger;
    private Map clientsMap;

    public Server() {

        try {
            logger = new BufferedWriter(new FileWriter("src/main/resources/serverFiles/file.log"));
            clientsMap = new HashMap<String, BufferedWriter>();

            String port = new BufferedReader(new FileReader("src/main/resources/serverFiles/settings.txt")).readLine();
            serverSocket = new ServerSocket(Integer.parseInt(port));

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void start() {

        while (true) {
            Socket socket = serverSocket.accept();

            new Thread(() -> {

                new ClientHandler(this, socket);


            }).start();
        }
    }

    public void addClient(String name, BufferedWriter bw) {
        clientsMap.put(name, bw);
    }

    public void sendMessageToAll(String senderName, String message) {

        Set keys = clientsMap.keySet();
        for (Object nameFromMap : keys) {
            if (senderName.equals(nameFromMap)) {
                continue;
            }
            BufferedWriter bw = (BufferedWriter)(clientsMap.get(nameFromMap));
            bw.write(message);
            bw.newLine();
            bw.flush();
        }

    }

    public void zapisatMessageVLog(String message) {
        logger.write(message);
        logger.write("\n");
        logger.flush();
    }


}