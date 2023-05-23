package Model.Logic;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;

public class HostServer extends Observable {
    private int hostPort;
    private int myServerPort;
    private String myServerIP;
    private volatile boolean stop;
    private ClientHandler clientHandler;
    private HashMap<Integer, Socket> clientsMap;

    //private List<String> booksName;


    /**
     * HostServer constructor creates new HostServer
     *
     * @param hostPort      the port of the host to connect to
     * @param myServerPort  the port of MyServer to connect to
     * @param myServerIP    the IP of MyServer to connect to
     * @param stop          indicate to know when to finish the loop
     * @param clientHandler implementation interface to solve query received from the clients
     * clientsMap --> to keep track of the connected clients in the HostServer class
     */

    public HostServer(int hostPort,int myServerPort, String myServerIP,boolean stop, ClientHandler clientHandler) {
        this.hostPort = hostPort;
        this.myServerPort = myServerPort;
        this.myServerIP = myServerIP;
        this.stop = false;
        this.clientHandler = clientHandler;
        this.clientsMap = new HashMap<>();
        this.start();
    }


    private void runServer() {
        ServerSocket server = null;
        try {
            server = new ServerSocket(myServerPort);
            //need to be here new thread.
            Thread clientThread = new Thread(this::checkForMessage);
            clientThread.start();
            while (!stop) {
                Socket client = server.accept();
                HostModel.getHost().addNewPlayer(client);
                // Handle the client connection in a separate thread
                clientHandler.handleClient(client.getInputStream(), client.getOutputStream());
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            System.err.println("Error occurred while running the server: " + e.getMessage());
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    System.err.println("Error occurred while closing the server socket: " + e.getMessage());
                }
            }
        }
    }

    /**
     * check ForMessage method is responsible for continuous review of incoming messages from clients
     */

    private void checkForMessage() {
        // Assuming that the clientsMap contains the client sockets
        while (!stop) {
            for (Socket clientSocket : clientsMap.values()) {
                try {
                    // Check if there is any incoming message from the client
                    InputStream inputStream = clientSocket.getInputStream();

                    //The code checks if there is any incoming message from the client.If the value is greater than 0, it means there is an incoming message.
                    if (inputStream.available() > 0) {
                        //Responsible for processing customer messages.
                        clientHandler.handleClient(inputStream, clientSocket.getOutputStream());
                    }
                } catch (IOException e) {
                }
            }
            try {
                Thread.sleep(250); //Helps reduce CPU usage and allows you to perform other tasks.
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void start(){
        new Thread(()->runServer()).start();
    }

    public void close() {
            stop = true; // Set the stop flag to true to exit the server loop

            // Close all client connections
            for (Socket clientSocket : clientsMap.values()) {
                try {
                    clientSocket.close();
                } catch (IOException e) {}
            }

            // Clear the clients map
            clientsMap.clear();

            // Notify observers of the server closure
            setChanged();
            notifyObservers("Server closed");
           // updateAllClient("1;serverClosed");
        }



    private void updateAllClient(String message) {
            // Iterate over the connected clients
            for (Socket clientSocket : clientsMap.values()) {
                try {
                    // Send the update message to each client
                    clientSocket.getOutputStream().write(message.getBytes());
                    clientSocket.getOutputStream().flush();
                } catch (IOException e) {
                    // Handle any exceptions that occur while sending the update
                    e.printStackTrace();
                }
            }
}

    public void send(String letter , String word ){
        Socket myServer = new Socket(myServerIP,myServerPort);
        PrintWriter outputStream = new PrintWriter(myServer.getOutputStream());
        StringBuilder message = new StringBuilder();
        message.append(";");



    }

    }





