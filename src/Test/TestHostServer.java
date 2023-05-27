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

    public static class chen implements ClientHandler {

        HostServer hostServer;

        @Override
        public void handleClient(InputStream in, OutputStream out) throws IOException {
            Scanner scan = new Scanner(in);
            String message = scan.next();
            hostServer.updateObservers(message);
        }

        @Override
        public void close() {

        }
    }

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
        chen handler = new chen();
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

        if (test1.message == null || !test1.message.equals("-1;connect;,start")) {
            System.out.println("Expected to get -1;connect;,start,result:" + test1.message);
            System.out.println(" Error ,message for the first guest didn't received ,CheckForMessage didn't work \n");
        } else {
            System.out.println(" Good job,message for the first guest received ,CheckForMessage work!:) ");

            c1.close();
            handler.hostServer.close();

        }
    }

    public static void main(String[] args) {

        MyServer myServer = new MyServer(1234, new ClientHandler() {
            @Override
            public void handleClient(InputStream inFromclient, OutputStream outToClient) throws IOException {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inFromclient));
                String mes = reader.readLine();
                if (mes.equals("message1 delivered to My server \n")) {
                    System.out.println("Hello from Host Server\n");
                } else {
                    System.out.println("The HostServer don't get the message\n");
                }
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outToClient));
                writer.write("HostServer get the message from the handler");
            }

            @Override
            public void close() {

            }
        });

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