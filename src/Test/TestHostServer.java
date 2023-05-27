package Test;

import Model.Logic.*;
//import Model.Logic.files.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class TestHostServer {


    //----------------------------------------------------------------------------------------------------------------------
    public static class TestHostModel implements Observer {
        public String message = null;

        public TestHostModel(Observable o) {
            o.addObserver(this);
        }

        @Override
        public void update(Observable o, Object arg) {
            this.message = (String) arg;
            System.out.println("This message Host Model got:" + message);
        }
    }

//-----------------------------------------------------------------------------------------------

    public static void checkMessageFromServer() {
        TestHostModel test1 = null;
        MessageHandler handler = new MessageHandler();
        handler.hostServer = new HostServer(2356, 1234, "localhost", false, handler); //HostServer-->MyServer
        test1 = new TestHostModel(handler.hostServer); //to check to notify to the observable.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ClientCommunication c1 = new ClientCommunication("localhost", 2356);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        c1.send(1, "chen", "1", "2", "3"); //client Communication ---> HostServer
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        c1.send(1, "tryPlaceWord", "1", "2", "3"); //client Communication ---> HostServer
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        c1.send(1, "challenge", "1", "2", "3");
        //----------------------------------------------------------------------------------------------------------------------------------
//            if (test1.message == null || !test1.message.equals("1;chen;,1,2,3")) {
//                System.out.println("Expected to get 1;chen;,1,2,3," +"  "+"result:" + test1.message);
//                System.out.println(" Error ,message for the first guest didn't received ,CheckForMessage didn't work \n");
//            } else {
//                System.out.println(" Good job,message for the first guest received ,CheckForMessage work!:) ");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
                c1.close();
                handler.hostServer.close();

            }




    public static void main(String[] args) {

        MyServer myServer = new MyServer(1234, new ClientHandler() {
            @Override
            public void handleClient(InputStream inFromclient, OutputStream outToClient) throws IOException {
                Scanner scan = new Scanner(inFromclient);
                String mes = scan.next();
                System.out.println(mes);
                if (mes.equals("Q")) {
                    System.out.println("Hello from My Server\n");
                } else {
                    System.out.println("The MyServer don't get the message\n");
                }

                if (mes.equals("C")) {
                    System.out.println("Hello from My Server\n");
                } else {
                    System.out.println("The MyServer don't get the message\n");
                }
            }

            @Override
            public void close() {
            }
        });

        myServer.start();
        System.out.println("Checking HostServer class\n");
        checkMessageFromServer();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.activeCount());
        myServer.close();

    }
}