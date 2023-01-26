package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StateMachine extends Remote {
    public double read (int variable) throws RemoteException;
    public boolean update (int variable, int operation, double value) throws RemoteException;
}
