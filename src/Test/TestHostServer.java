package Test;

import Model.Logic.ClientHandler;
import Model.Logic.GuestMessagelHandler;
import Model.Logic.HostServer;
import Model.Logic.MyServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class TestHostServer {
    public static MyServer myserver = null;
    public static class TestHostModel implements Observer{
        public String message = null;


        public TestHostModel(Observable o) {
            o.addObserver(this);
        }


        @Override
        public void update(Observable o, Object arg) {
            message = (String) arg;
        }
    }
    public static void testCheckForMessageServer(){
        Socket toServer=null;                           // Opening a socket tunnel;
        HostServer s1= null;                           // The observable
//        HostServer s2= null;
        TestHostModel test1=null;                      // Imitates the host model object(The observer)
//        TestHostModel test2=null;
        ClientHandler clientHandler=null;
        ServerSocket serversocket=null;
        try{
            serversocket= new ServerSocket(3456); // Connecting the socket to the port of myServer
            clientHandler=new ClientHandler() {
                @Override
                public void handleClient(InputStream inFromclient, OutputStream outToClient) throws IOException {

                }

                @Override
                public void close() {

                }
            };
            myserver= new MyServer(3456,clientHandler);
            s1=new HostServer(2345,3456,"localMyServer",false,);
            test1= new TestHostModel(s1);
            myserver.start();
            Socket socket = serversocket.accept();
            // socket.getOutputStream();


//            s2=new HostServer(2346,3456,"localMyServer",false,clientHandler);
//            test2= new TestHostModel(s2);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
