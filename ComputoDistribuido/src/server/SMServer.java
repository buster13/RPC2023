package server;

import interfaces.StateMachine;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.rmi.server.RemoteServer.getClientHost;

public class SMServer implements StateMachine {
    private double a;
    private double b;

    public SMServer() throws RemoteException {
        super();
    }

    public double getA() {
        return this.a;
    }

    public double getB() {
        return this.b;
    }

    public void setA(double value) {
        this.a = value;
    }

    public void setB(double value) {
        this.b = value;
    }

    public void addA(double value) {
        this.a += value;
    }

    public void addB(double value) {
        this.b += value;
    }

    public void multA(double value) {
        this.a *= value;
    }

    public void multB(double value) {
        this.b *= value;
    }

    @Override
    public double read(int variable) throws RemoteException {
        try{
            Logger.getLogger(SMServer.class.getName()).log(Level.INFO, "Se ha procesado una solicitud de lectura de " + getClientHost());
            return switch (variable) {
                case 0 -> getA();
                case 1 -> getB();
                default -> 0;
            };
        } catch(Exception ex){
            Logger.getLogger(SMServer.class.getName()).log(Level.WARNING, "Error al procesar una soliticud de lectura.", ex);
            return 0;
        }
    }

    @Override
    public boolean update(int variable, int operation, double value) throws RemoteException {
        try{
            switch (variable) {
                case 0 -> {
                    switch (operation) {
                        case 0 -> this.setA(value);
                        case 1 -> this.addA(value);
                        case 2 -> this.multA(value);
                    }
                }
                case 1 -> {
                    switch (operation) {
                        case 0 -> this.setB(value);
                        case 1 -> this.addB(value);
                        case 2 -> this.multB(value);
                    }
                }
            }
            Logger.getLogger(SMServer.class.getName()).log(Level.INFO, "Se ha procesado una solicitud de actualización de " + getClientHost());
            return true;
        } catch(Exception ex){
            Logger.getLogger(SMServer.class.getName()).log(Level.WARNING, "Error al procesar una soliticud de actualización.", ex);
            return false;
        }
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            String name = "StateMachine";
            SMServer engine = new SMServer();
            StateMachine stub = (StateMachine) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            Logger.getLogger(SMServer.class.getName()).log(Level.INFO, "El servidor se ha iniciado exitosamente.");
        } catch (RemoteException ex) {
            Logger.getLogger(SMServer.class.getName()).log(Level.SEVERE, "Error al iniciar el servidor.", ex);
        }
    }
}
