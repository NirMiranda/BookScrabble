package Model.Logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class MyServer {

    private int port;
    private ClientHandler ch;
    private volatile boolean stop;

    public MyServer(int port,ClientHandler ch) {
        this.port=port;
        this.ch=ch;
        stop=false;
    }

    private void runServer() {
        ServerSocket server=null;
        Socket aClient=null;
        try {
            server=new ServerSocket(port);
            server.setSoTimeout(1000);
            System.out.println("Server started. Waiting for client connections...");
            System.out.flush();
            while(!stop){
                try{
                    aClient=server.accept(); // blocking call
                    System.out.println("Client connected: " + aClient.getInetAddress().getHostAddress());

                    try {
                        ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
                        ch.close();
                        aClient.close();
                    } catch (IOException e) {/*...*/}
                }catch(SocketTimeoutException e) {/*...*/}
            }
            server.close();
        }
        catch(IOException e) {
        }finally {
            // Close the client and server sockets in case of any exceptions
            try {
                if(aClient!=null && !aClient.isClosed())
                    aClient.close();
                if(server!=null && !server.isClosed())
                    server.close();
            }catch(IOException e) {}
        }
    }

    public void start(){
        new Thread(()->runServer()).start();
    }

    public void close(){
        stop=true;
    }


}