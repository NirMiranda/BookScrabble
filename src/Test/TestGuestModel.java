package Test;

import Model.Logic.ClientCommunication;
import Model.Logic.GuestModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TestGuestModel {
    public static void testCheckForMessage()  {
        char[][] demoBoard = {
                {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O'},
                {'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'A', 'B', 'C', 'D'},
                {'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S'},
                {'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'},
                {'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W'},
                {'X', 'Y', 'Z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L'},
                {'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'A'},
                {'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P'},
                {'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'A', 'B', 'C', 'D', 'E'},
                {'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'},
                {'U', 'V', 'W', 'X', 'Y', 'Z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'},
                {'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X'},
                {'Y', 'Z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M'},
                {'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'A', 'B'},
                {'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q'}
        };
        ServerSocket server = null;
        ClientCommunication c1=null;
        GuestModel gustmodel=null;

        try {
            server = new ServerSocket(1234);
            c1 = new ClientCommunication("localhost", 1234);//open localserver to demo message feom gustModel
            Socket socket = server.accept();
            gustmodel = new GuestModel(c1);
            socket.getOutputStream().write("1;setGameOrder;0:0,1:1,2:2,3:3".getBytes()); //בקשה 1
            socket.getOutputStream().write("\n".getBytes());
            Thread.sleep(10000);
            if (gustmodel.getTurnsOrder().get(1)!=1) {
                System.out.println("communication1 between clientCommunication and GustModel doesnt work ");
            } else {
                System.out.println("Good job! the playerTurns is change"); //סיום בקשה
            }
            Thread.sleep(10000);
            socket.getOutputStream().write("1;setPlayersScores;0:0,1:12,2:0,3:0".getBytes()); //בקשה שנייה
            socket.getOutputStream().write("\n".getBytes());
            if (gustmodel.getPlayersScores().get(1)!=12) {
                System.out.println("communication1 between clientCommunication and GustModel doesnt work ");
            } else {
                System.out.println("Good job! the playerScore is change"); //סיום בקשה
            }
            Thread.sleep(20000);
            socket.getOutputStream().write("1;setPlayersNumberOfTiles;0:5,1:7,2:6,3:3".getBytes()); //בקשה שלישית
            socket.getOutputStream().write("\n".getBytes());
            if (gustmodel.getPlayersNumberOfTiles().get(2).equals(6)) {
                System.out.println("communication1 between clientCommunication and GustModel doesnt work ");
            } else {
                System.out.println("Good job! the playerNumberOfTiles is change");// סיום בקשה
            }
            Thread.sleep(10000);
//        socket.getOutputStream().write("1;setPlayersScores;30".getBytes());
//        socket.getOutputStream().write("\n".getBytes());
//        if (gustmodel.getNumberOfTilesInBag()==30) {
//            System.out.println("communication1 between clientCommunication and GustModel doesnt work ");
//        } else {
//            System.out.println("Good job! the Number Of tiles in the bag  is change");
//        }
            //      Thread.sleep(1000);

            Thread.sleep(30000);
            socket.getOutputStream().write("1;setBoardStatus;0:0=A,0:1=B,0:2=C,0:3=D,0:4=E,0:5=F,0:6=G,0:7=H,0:8=I,0:9=J,0:10=K,0:11=L,0:12=M,0:13=N,0:14=O,1:0=P,1:1=Q,1:2=R,1:3=S,1:4=T,1:5=U,1:6=V,1:7=W,1:8=X,1:9=Y,1:10=Z,1:11=A,1:12=B,1:13=C,1:14=D,2:0=E,2:1=F,2:2=G,2:3=H,2:4=I,2:5=J,2:6=K,2:7=L,2:8=M,2:9=N,2:10=O,2:11=P,2:12=Q,2:13=R,2:14=S,3:0=T,3:1=U,3:2=V,3:3=W,3:4=X,3:5=Y,3:6=Z,3:7=A,3:8=B,3:9=C,3:10=D,3:11=E,3:12=F,3:13=G,3:14=H,4:0=I,4:1=J,4:2=K,4:3=L,4:4=M,4:5=N,4:6=O,4:7=P,4:8=Q,4:9=R,4:10=S,4:11=T,4:12=U,4:13=V,4:14=W,5:0=X,5:1=Y,5:2=Z,5:3=A,5:4=B,5:5=C,5:6=D,5:7=E,5:8=F,5:9=G,5:10=H,5:11=I,5:12=J,5:13=K,5:14=L,6:0=M,6:1=N,6:2=O,6:3=P,6:4=Q,6:5=R,6:6=S,6:7=T,6:8=U,6:9=V,6:10=W,6:11=X,6:12=Y,6:13=Z,6:14=A,7:0=B,7:1=C,7:2=D,7:3=E,7:4=F,7:5=G,7:6=H,7:7=I,7:8=J,7:9=K,7:10=L,7:11=M,7:12=N,7:13=O,7:14=P,8:0=Q,8:1=R,8:2=S,8:3=T,8:4=U,8:5=V,8:6=W,8:7=X,8:8=Y,8:9=Z,8:10=A,8:11=B,8:12=C,8:13=D,8:14=E,9:0=F,9:1=G,9:2=H,9:3=I,9:4=J,9:5=K,9:6=L,9:7=M,9:8=N,9:9=O,9:10=P,9:11=Q,9:12=R,9:13=S,9:14=T,10:0=U,10:1=V,10:2=W,10:3=X,10:4=Y,10:5=Z,10:6=A,10:7=B,10:8=C,10:9=D,10:10=E,10:11=F,10:12=G,10:13=H,10:14=I,11:0=J,11:1=K,11:2=L,11:3=M,11:4=N,11:5=O,11:6=P,11:7=Q,11:8=R,11:9=S,11:10=T,11:11=U,11:12=V,11:13=W,11:14=X,12:0=Y,12:1=Z,12:2=A,12:3=B,12:4=C,12:5=D,12:6=E,12:7=F,12:8=G,12:9=H,12:10=I,12:11=J,12:12=K,12:13=L,12:14=M,13:0=N,13:1=O,13:2=P,13:3=Q,13:4=R,13:5=S,13:6=T,13:7=U,13:8=V,13:9=W,13:10=X,13:11=Y,13:12=Z,13:13=A,13:14=B,14:0=C,14:1=D,14:2=E,14:3=F,14:4=G,14:5=H,14:6=I,14:7=J,14:8=K,14:9=L,14:10=M,14:11=N,14:12=O,14:13=P,14:14=Q".getBytes());
            socket.getOutputStream().write("\n".getBytes());
            char[][] demoBoard2=gustmodel.getBoardStatus();
            int flag=0;
            for(int i=0;i<15;i++)
            {
                for(int j=0;j<15;j++)
                {
                    if(demoBoard2[i][j]==demoBoard[i][j]&&i==14&&j==14)
                    {

                        flag=1;
                    }
                }
            }
            if (flag==0) {
                System.out.println("communication1 between clientCommunication and GustModel doesnt work ");
            } else {
                System.out.println("The board of player with id 1 is:");
                for(int i=0;i<15;i++)
                {
                    for(int j=0;j<15;j++)
                    {
                        System.out.println(demoBoard2[i][j]+",");
                    }
                }

                System.out.println("Good job! the Board status is change");
            }
            Thread.sleep(10000);
            socket.close();
            server.close();
            c1.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    public static void main(String[] args) throws InterruptedException {
        System.out.println("checking Checkformessage methode");
        testCheckForMessage();


        System.out.println("Tests finished successfully,GustModel is OK");
    }


}