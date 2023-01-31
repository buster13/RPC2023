package interfaces;

import messages.Response;
import org.json.JSONObject;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StateMachine extends Remote {
    public String read (String json) throws RemoteException;
    public String update (String json) throws RemoteException;
}
