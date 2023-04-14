package server;

import interfaces.StateMachine;
import utils.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SMServer extends UnicastRemoteObject implements StateMachine {
    private final String PASSWORD = "ThisIsNotThePassword";
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
    public String toString() {
        return "SMServer{" + "a=" + a + ", b=" + b + '}';
    }

    @Override
    public String read(String json) throws RemoteException {
        Response resp;
        json = AES.decrypt(json, this.PASSWORD);
        try{
            JSONObject request = new JSONObject(json);

            int variable = request.getInt("var");

            //Arma evento
            Evento_Mssg evt = Lamport.arma_Evento(3, variable, 0);
            //Lamport agrega
            Lamport.agrega_Evento(evt);

            //No debe de avanzar hasta que procese el evento local

            double num;
            switch (variable) {
                case 0 -> num = getA();
                case 1 -> num = getB();
                default -> throw new UnsupportedOperationException();
            };

            Logger.getLogger(SMServer.class.getName()).log(Level.INFO, "Se ha procesado una solicitud de lectura de " + getClientHost() + "\n" + this.toString());
            resp = new Response(3, String.valueOf(num), "OK");
        } catch (UnsupportedOperationException uoEx) {
            Logger.getLogger(SMServer.class.getName()).log(Level.WARNING, "Operación no soportada.", uoEx);
            resp = new Response(-1, "false", "Operación no soportada.");
        } catch (ServerNotActiveException svEx){
            Logger.getLogger(SMServer.class.getName()).log(Level.WARNING, "Servidor no disponible.", svEx);
            resp = new Response(-1, "false", "Servidor no disponible.");
        } catch (JSONException jsEx){
            Logger.getLogger(SMServer.class.getName()).log(Level.WARNING, "Error al procesar una soliticud de actualización.", jsEx);
            resp = new Response(-1, "false", jsEx.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return AES.encrypt(resp.toString(), this.PASSWORD);
    }

    @Override
    public String update(String json) throws RemoteException {
        Response resp;
        json = AES.decrypt(json, this.PASSWORD);
        try{
            JSONObject request = new JSONObject(json);
            int variable = request.getInt("var");
            int operation = request.getInt("oper");
            double value = request.getDouble("value");

            //Arma evento
            Evento_Mssg evt = Lamport.arma_Evento(operation, variable, value);
            //Lamport agrega
            Lamport.agrega_Evento(evt);

            //No debe de avanzar hasta que procese el evento local

            switch (variable) {
                case 0 -> {
                    switch (operation) {
                        case 0 -> this.setA(value);
                        case 1 -> this.addA(value);
                        case 2 -> this.multA(value);
                        default -> throw new UnsupportedOperationException();
                    }
                }
                case 1 -> {
                    switch (operation) {
                        case 0 -> this.setB(value);
                        case 1 -> this.addB(value);
                        case 2 -> this.multB(value);
                        default -> throw new UnsupportedOperationException();
                    }
                }
                default -> throw new UnsupportedOperationException();
            }
            Logger.getLogger(SMServer.class.getName()).log(Level.INFO, "Se ha procesado una solicitud de actualización de " + getClientHost() + "\n" + this.toString());
            resp = new Response(operation, "true", "OK");
        } catch (UnsupportedOperationException uoEx) {
            Logger.getLogger(SMServer.class.getName()).log(Level.WARNING, "Operación no soportada.", uoEx);
            resp = new Response(-1, "false", "Operación no soportada.");
        } catch (ServerNotActiveException svEx){
            Logger.getLogger(SMServer.class.getName()).log(Level.WARNING, "Servidor no disponible.", svEx);
            resp = new Response(-1, "false", "Servidor no disponible.");
        } catch (JSONException jsEx){
            Logger.getLogger(SMServer.class.getName()).log(Level.WARNING, "Error al procesar una soliticud de actualización.", jsEx);
            resp = new Response(-1, "false", jsEx.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return AES.encrypt(resp.toString(), this.PASSWORD);
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            String name = "StateMachine";
            SMServer engine = new SMServer();
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, engine);
            Logger.getLogger(SMServer.class.getName()).log(Level.INFO, "El servidor se ha iniciado exitosamente.");

            //Agregar Sockets
            //Manejador de sockets (extiende a Thread) para .start()
            ManejadorSockets_Serv servidor_Sockets = new ManejadorSockets_Serv();
            servidor_Sockets.run();

        } catch (RemoteException ex) {
            Logger.getLogger(SMServer.class.getName()).log(Level.SEVERE, "Error al iniciar el servidor.", ex);
        }
    }
}
