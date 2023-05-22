package Model.Logic;

import java.io.*;
import java.net.Socket;
import java.util.Observable;

public class ClientCommunication extends Observable {

    Socket socket;

    //ip
    //port

    public ClientCommunication(String ip, int port){
        try{
            socket = new Socket(ip, port);
            send(-1, "connect");
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

    public void send(int id,String methodName, String...args){
            try {
                String message = updateMessage(id, methodName, args);
                OutputStream outputStream = socket.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                writer.write(message);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                // Handle any exceptions that occur during the sending process
                e.printStackTrace();
            }
        }

    private String updateMessage(int id, String methodName, String... args) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(String.valueOf(id));
        messageBuilder.append(";"); // Delimiter between ID and method name
        messageBuilder.append(methodName);

        // Append arguments if present
        if (args != null && args.length > 0) {
            for (String arg : args) {
                messageBuilder.append(";"); // Delimiter between arguments
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

    private void checkForMessage(){
            try {
                InputStream inputStream = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String message;
                while ((message = reader.readLine()) != null) {
                    setChanged();
                    notifyObservers(message);
                    Thread.sleep(250);//after 1/4 sec it will check again.
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }




