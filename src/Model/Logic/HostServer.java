package Model.Logic;

import java.net.Socket;
import java.util.Observable;

public class HostServer extends Observable {
    private MyServer hostServer;
    private Socket myServerSocket;
    private Socket[] clientsSockets;
    private ClientHandler clientHandler;

    private void updateAllClient(){
        //?
    }
}
