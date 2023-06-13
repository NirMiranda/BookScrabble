package Test;

import Model.Data.Board;
import Model.Logic.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

//host model --> observer to hostServer.

//host model --> can be player.

public class TestHostModel {

    public static void checkIsValid() {
        MessageHandler messageHandler = new MessageHandler();
        messageHandler.hostServer = new HostServer(2356, 1234, "localhost", false, messageHandler);
        HostModel hostModel = HostModel.getHostModel(messageHandler.hostServer);
        Board board = hostModel.board;
        int index = hostModel.getCurrentPlayerIndex();
//      char[][] board = hostModel.getBoardStatus();
        int numberOfTiles = hostModel.getNumberOfTilesInBag();
        ClientCommunication c1 = new ClientCommunication("localhost", 2356);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //c1.send(1,"tryPlaceWord","WATER","7","7","true");//client Communication ---> HostServer
        messageHandler.hostServer.updateObservers("1;tryPlaceWord;WATER,7,7,true,true");
//
        if ( hostModel.playersScores.get(1)!=0) {
            System.out.println("Player scores has changed successfully");
        } else {
            System.out.println("Player scores hasn't changed");
        }

        if (index != hostModel.currentPlayerIndex) {
            System.out.println("Player index turn has changed successfully");
        } else {
            System.out.println("Player index turn hasn't changed");
        }

        if (board.getTile()[7][7].score==4) {
            System.out.println("The board has changed successfully");
        } else {
            System.out.println("The board hasn't changed");
        }

        if (numberOfTiles == hostModel.getNumberOfTilesInBag()) {
            System.out.println("The number of tiles in the bag has changed successfully");
        } else {
            System.out.println("The number of tiles in the bag hasn't changed");
        }

        //Print the update message from the host model to all the guests.
//        try {
//            Scanner scan = new Scanner(messageHandler.hostServer.clientsMap.get(0).getOutputStream().toString());
//            String out = scan.next();
//            System.out.println("The message update that sent from hostModel is: " + out);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }


    public static void main(String[] args) {

        MyServer myServer = new MyServer(1234, new ClientHandler() {
            @Override
            public void handleClient(InputStream inFromclient, OutputStream outToClient) throws IOException {
                Scanner scan = new Scanner(inFromclient);
                String mes = scan.next();
                System.out.println(mes);
                if (mes.equals("Q")||(mes.equals("C"))) {
                    System.out.println("true");
                } else {
                    System.out.println("false");
                }
            }


            @Override
            public void close() {
            }
        });

        myServer.start();
        System.out.println("TestHostModel is getting started");
        checkIsValid();
        System.out.println("Done");
        myServer.close();
    }


}

