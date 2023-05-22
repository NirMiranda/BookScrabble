package Model.Logic;

import java.util.Observable;
import java.util.Observer;

public class GuestModel extends PlayerModel implements Observer<ClientCommunication> {
    ClientCommunication clientCommunication;

    @Override
    public void update(Observable o, Object arg) {

    }
}
