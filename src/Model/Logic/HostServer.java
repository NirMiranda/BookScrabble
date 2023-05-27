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
    public ClientHandler clientHandler;
    private HashMap<Integer, Socket> clientsMap;
    private List<File> listBooks;

    public Socket myServer;

    /**
     * HostServer constructor creates new HostServer
     *
     * @param hostPort      the port of the host to connect to
     * @param myServerPort  the port of MyServer to connect to
     * @param myServerIP    the IP of MyServer to connect to
     * @param stop          indicate to know when to finish the loop
     *                      clientsMap --> to keep track of the connected clients in the HostServer class
     */

    public HostServer(int hostPort, int myServerPort, String myServerIP, boolean stop,ClientHandler ch) {
        this.hostPort = hostPort;
        this.myServerPort = myServerPort;
        this.myServerIP = myServerIP;
        this.stop = false;
        this.clientsMap = new HashMap<>();
        this.clientHandler=ch;
        this.start();
    }

    public void updateObservers(String message){
        setChanged();
        notifyObservers(message);
    }


    private void runServer() {
        ServerSocket server = null;
        new Thread((this::checkForMessage)).start();
        try {
            server = new ServerSocket(hostPort);
            //need to be here new thread.

            while (!stop) {
                Socket client = server.accept();

                this.clientsMap.put(1,client);
                //HostModel.getHost().addNewPlayer(client);
                // Handle the client connection in a separate thread
                clientHandler.handleClient(client.getInputStream(), client.getOutputStream());

                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * check ForMessage method is responsible for continuous review of incoming messages from clients
     */

    private void checkForMessage() {
        // Assuming that the clientsMap contains the client sockets
        while (!stop) {
            for (Socket clientSocket : this.clientsMap.values()) {
                try {
                    // Check if there is any incoming message from the client
                    InputStream inputStream = clientSocket.getInputStream();

                    //The code checks if there is any incoming message from the client.If the value is greater than 0, it means there is an incoming message.
                    if (inputStream.available() > 0) {
                        clientHandler.handleClient(inputStream, clientSocket.getOutputStream());
                    }
                } catch (IOException e) {}
            }
            try {
                Thread.sleep(250); //Helps reduce CPU usage and allows you to perform other tasks.
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void start() {
        new Thread(() -> runServer()).start();
    }

    public void close() {
        stop = true; // Set the stop flag to true to exit the server loop

        // Close all client connections
        for (Socket clientSocket : clientsMap.values()) {
            try {
                clientSocket.close();
            } catch (IOException e) {
            }
        }

        // Clear the clients map
        clientsMap.clear();

        // Notify observers of the server closure
        setChanged();
        notifyObservers("Server closed");
        // updateAllClient("1;serverClosed");
    }


    /**
     * This function will update every socket about any message.
     *
     * @param message The message needs to be update all the sockets
     */
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

    /**
     * This method is send to MyServer the letter('q'-query or 'c'); books name; word
     *
     * @param letter can be 'q'-query or 'c'-challenge
     * @param word   the word that the user wants to place
     */
    public Socket sendToMyServer(String letter, String word) {
        try {
            myServer = new Socket(myServerIP, myServerPort);
            PrintWriter myServerOut = new PrintWriter(myServer.getOutputStream());
            StringBuilder message = new StringBuilder();
            message.append(letter);
            for (File book : listBooks) {
                message.append(book.getName() + ",");
            }
            message.append(word);
            myServerOut.println(message);//send to MyServer the query from the HostModel.
            myServerOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myServer;
    }

    /**
     * This function is send answer to the player.
     *
     * @param id     the id of the player
     * @param method the method that that has been sent to the server
     * @param answer the answer that the hostServer is giving back
     */

    public void sendToPlayerMessage(int id, String method, String answer) {
        Socket clientSocket = clientsMap.get(id);
        if (clientSocket != null) {
            try {
                OutputStream outputStream = clientSocket.getOutputStream();
                PrintWriter clientOut = new PrintWriter(outputStream);
                StringBuilder message = new StringBuilder();
                message.append(method).append(";").append(answer);
                clientOut.println(message);
                clientOut.flush();
            } catch (IOException e) {
                // Handle any exceptions that occur while sending the message to the client
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("The player with id: " + id + "is not connected to the server");
        }
    }


}