package Test;

import Model.Logic.ClientCommunication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class TestClientCommunication {


    public static class ClientGuestTest implements Observer { //Test class that check notify to the guest.
        public String messege = null;

        public ClientGuestTest(Observable o) {
            o.addObserver(this);
        } // adding the guest to the list og the clientcommunication observable.

        @Override
        public void update(Observable o, Object arg) {
            messege = (String) arg;
        } //get messege from the Observable and updating the data member string
    }


    public static void testCheckForMessage() { // the function check if the message received from the loclhost server to the two guest.
        ServerSocket server = null;
        ClientCommunication Guestclient = null;
        ClientCommunication Guestclient2 = null;
        ClientGuestTest test1 = null;
        ClientGuestTest test2 = null;
        try {
            server = new ServerSocket(3687);
            Guestclient = new ClientCommunication("localhost", 3687);
            test1 = new ClientGuestTest(Guestclient);
            Socket socket = server.accept();
            Guestclient2 = new ClientCommunication("localhost", 3687);
            test2 = new ClientGuestTest(Guestclient2);
            Socket socket2 = server.accept();
            socket.getOutputStream().write("message delivered 1\n".getBytes());
            socket2.getOutputStream().write("message delivered 2\n".getBytes());
            Thread.sleep(1000);
            if (test1.messege == null || !test1.messege.equals("message delivered 1")) {
                System.out.println(" Error ,message for the first guest didnt recived ,CheckForMessage didnt work ");
            } else {
                System.out.println(" Good job,message for the first guest received ,CheckForMessage work!:) ");
            }
            if (test2.messege == null || !test2.messege.equals("message delivered 2")) {
                System.out.println("Error ,message for the second guest didnt recived ,CheckForMessage didnt work");
            }
            else {
                System.out.println(" Good job,message for the second guest received ,CheckForMessage work!:) ");
            }
            socket.close();
            socket2.close();
            server.close();
            Guestclient2.close();
            Guestclient.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)
    {
        System.out.println("checking Checkformessage methode");
        testCheckForMessage();
        System.out.println("Tests finished successfully,ClientCommunication is OK");
    }

}



