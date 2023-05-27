package Model.Logic;

import java.io.*;
import java.net.Socket;
import java.util.Observable;
import java.util.Scanner;

/**
 * This class is responsible for the communication between the client and the server.
 * Its sends message to the server and notifies the observers when a message is received.
 * The protocol is: id;method;args1,args2,...
 * The id is the id of the client, -1 means that the client is not connected yet.
 */

public class ClientCommunication extends Observable {

    Socket socket;

    /**
     * Creates a new client communication with the server and starts a new thread.
     * @param ip the host to connect to
     * @param port the port to connect to
     */
    //port

    public ClientCommunication(String ip, int port){
        try{
            socket = new Socket(ip, port);
            send(-1, "connect","start");
            startMessageCheckingThread();
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }


    private void startMessageCheckingThread() {
        Thread messageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                checkForMessage();
            }
        });
        messageThread.start();
    }

    /**
     * Sends a message to the server, protocol: id;method;args1,args2,...
     * @param id the id of the client
     * @param methodName the method name to call
     * @param args the args to the method
     */

    public void send(int id,String methodName, String...args){
        try {
            String message = updateMessage(id, methodName, args);
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream);
            writer.println(message);
            writer.flush();
        } catch (IOException e) {
            // Handle any exceptions that occur during the sending process
            e.printStackTrace();
        }

    }


    /**
     *make the message in format: id;method;args1,args2...
     */
    private String updateMessage(int id, String methodName, String... args) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(String.valueOf(id));
        messageBuilder.append(";"); // Delimiter between ID and method name
        messageBuilder.append(methodName);
        messageBuilder.append(";");

        // Append arguments if present
        if (args != null && args.length > 0) {
            for (String arg : args) {
                messageBuilder.append(","); // Delimiter between arguments
                messageBuilder.append(arg);
            }
        }
        return messageBuilder.toString();
    }

    public void close(){
        if(socket!=null){
            try {
                socket.close();
                // Notify observers that the connection has been closed
                setChanged();
                notifyObservers("closed");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void checkForMessage() {
        while (socket.isConnected() && !socket.isClosed()) {
            try {
                Scanner in = new Scanner(socket.getInputStream());
                if (in.hasNext()) {
                    String message = in.nextLine();
                    setChanged();
                    notifyObservers(message);
                }
                Thread.sleep(250);
            }catch(IOException|InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }
}




