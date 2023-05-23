package Test;

import Model.Logic.ClientHandler;
import Model.Logic.GuestMessagelHandler;
import Model.Logic.HostServer;
import Model.Logic.MyServer;

import java.net.ServerSocket;
import java.util.Observable;
import java.util.Observer;

public class TestHostServer {
    public static MyServer myserver = null;
    public static class HostServerTest implements Observer{
        public String message = null;


        public HostServerTest(Observable o) {
            o.addObserver(this);
        }


        @Override
        public void update(Observable o, Object arg) {
            message = (String) arg;
        }
    }
    public static void testCheckForMessageServer(){
        HostServer s1= null;
        HostServer s2= null;
        HostServerTest test1=null;
        HostServerTest test2=null;
        ClientHandler clientHandler=null;
        try{
            server=new ServerSocket(3456);
            clientHandler=new GuestMessagelHandler("message");
            myserver= new MyServer(3456,clientHandler);
            s1=new HostServer(2345,3456,"localMyServer",false,clientHandler);

        }



    }
}
