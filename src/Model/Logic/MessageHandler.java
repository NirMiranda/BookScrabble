package Model.Logic;

import java.io.*;
import java.util.Scanner;

public class MessageHandler implements ClientHandler {

    HostServer hostServer;

    @Override
    public void handleClient(InputStream in, OutputStream out) throws IOException {
        BufferedReader fGuest = new BufferedReader(new InputStreamReader(in)); //read from the guest query.
        PrintWriter sendToGuest = new PrintWriter(out); //the answer to the guest will sent it in.
        String message = fGuest.readLine(); //read the first line from the guest.
        String[] msg = message.split(";"); // id;method;args...
        if (msg.length >= 3) {
            String id = msg[0];
            String method = msg[1];
            String args = msg[2];
            String[] argsArr = args.split(",");
            Scanner fServer = new Scanner(HostModel.getHost().GetSocketMyServer().getOutputStream().toString());
            String answerFromServer = "";

            //check here witch methodName
            if (method == "challege") {
                hostServer.sendToMyServer("C", argsArr[0]); //send to the server the query from the guest to the server in format: ("C" , word)
                answerFromServer = fServer.nextLine(); //after get the message, the server will send the answer.
                hostServer.notifyObservers(id + ":" + "challenge :" + args + "," + answerFromServer);
            } else if (method == "tryPlaceWord") {
                hostServer.sendToMyServer("Q", argsArr[0]); //send to the server the query from the guest to the server in format: ("Q" , word)
                answerFromServer = fServer.nextLine(); //after get the message, the server will send the answer.
                hostServer.notifyObservers(id + ":" + "tryPlaceWord :" + args + "," + answerFromServer);
            }
            /**
             * Notify the host model about the
             */
            else{
                hostServer.hasChanged();
                hostServer.notifyObservers(message);
            }

        }
    }

    @Override
    public void close() {
        if (hostServer != null) {
            hostServer.close(); // Close the host server if it is not null
        }
    }

}