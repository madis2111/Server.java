package chat;

import java.io.*;
import java.net.Socket;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.time.LocalDateTime;
import java.util.Set;

public class ClientHandler {

    Server server;

    private InputStream inputStream;
    private InputStreamReader isr;
    private BufferedReader reader;

    private OutputStream outputStream;
    private OutputStreamWriter osw;
    private BufferedWriter writer;

    String clientName;

    public ClientHandler(Server server, Socket socket) {

        this.server = server;

        try {
            inputStream = socket.getInputStream();
            isr = new InputStreamReader(inputStream);
            reader = new BufferedReader(isr);
            outputStream = socket.getOutputStream();
            osw = new OutputStreamWriter(outputStream);
            writer = new BufferedWriter(osw);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void privetstviye() {
        writer.write("Добро пожаловать в чат");
        writer.newLine();
        writer.flush();

        writer.write("Введите имя для участия в чате:");
        writer.newLine();
        writer.flush();

        clientName = reader.readLine();
        server.addClient(clientName, writer);
    }

    public void obsheniye() {
        while (true) {
            System.out.println("inside while");

            String line = clientName + ": " + reader.readLine();
            String time = LocalDateTime.now() + " ";

            server.sendMessageToAll(clientName, line);


            line = time + line;
            server.zapisatMessageVLog(line);
        }

    }
}
