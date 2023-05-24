package Model.Logic;

import java.io.*;
import java.util.Scanner;

public class GuestMessageHandler implements ClientHandler {

    HostServer hostServer;

    @Override
    public void handleClient(InputStream in, OutputStream out) throws IOException {
        BufferedReader buff = new BufferedReader(new InputStreamReader(in)); // read the query from the player.
        PrintWriter writer = new PrintWriter(out); // open file to write the answer to the player.
        Scanner from = new Scanner()
        String answer = parseMessage(buff.readLine()); // parse the message --> id ; method; arg1,args2,...
        // Do something with the answer...
        writer.println(answer);
        writer.flush();
    }

    private String parseMessage(String message) {
        String answer = "";
        String[] msg = message.split(";"); // id;method;args...
        if (msg.length >= 3) {
            String id = msg[0];
            String method = msg[1];
            String args = msg[2];
            String[] argsArr = args.split(",");

            if (method.equals("challenge")) {
                hostServer.sendToMyServer("c", argsArr[0]);
                hostServer.sendToPlayerMessage(Integer.parseInt(id),method,answer);
            }
        }
        return answer;
    }

    @Override
    public void close() {

    }
}


