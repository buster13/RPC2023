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
import java.util.HashMap;

public class SMServer extends UnicastRemoteObject implements StateMachine {
    private final String PASSWORD = "ThisIsNotThePassword";
    private HashMap<String, Double> diccionario;

    public SMServer() throws RemoteException {
        super();
        this.diccionario = new HashMap<>();
    }

    @Override
    public String toString() {
        return "SMServer{" + "diccionario=" + diccionario +  '}';
    }

    @Override
    public String get(String json) throws RemoteException {
        Response resp;
        //json = AES.decrypt(json, this.PASSWORD);
        return getString(json);
    }

    public String fastGet(String json) throws RemoteException {
        Response resp;
        json = AES.decrypt(json, this.PASSWORD);
        return getString(json);
    }

    private String getString(String json) {
        Response resp;
        try{
            JSONObject request = new JSONObject(json);
            String variable = request.getString("key");
            double num = this.diccionario.get(variable);

            Logger.getLogger(SMServer.class.getName()).log(Level.INFO, "Se ha procesado una solicitud de lectura " + "\n" + this.toString());
            resp = new Response(0, String.valueOf(num), "OK");
        } catch (UnsupportedOperationException uoEx) {
            Logger.getLogger(SMServer.class.getName()).log(Level.WARNING, "Operación no soportada.", uoEx);
            resp = new Response(-1, "false", "Operación no soportada.");
        }  catch (JSONException jsEx){
            Logger.getLogger(SMServer.class.getName()).log(Level.WARNING, "Error al procesar una soliticud de actualización.", jsEx);
            resp = new Response(-1, "false", jsEx.toString());
        }   catch (NullPointerException npEx){
            Logger.getLogger(SMServer.class.getName()).log(Level.WARNING, "Error, la variable no se encuentra en el diccionario", npEx);
            resp = new Response(-1, "false", npEx.toString());
        }
        return AES.encrypt(resp.toString(), this.PASSWORD);
    }

    @Override
    public String update(String json) throws RemoteException {
        Response resp;
        //json = AES.decrypt(json, this.PASSWORD);
        try{
            System.out.println(json);
            JSONObject request = new JSONObject(json);
            String variable = request.getString("key");
            double value = request.getDouble("value");
            this.diccionario.put(variable,value);

            Logger.getLogger(SMServer.class.getName()).log(Level.INFO, "Se ha procesado una solicitud de actualización " + "\n" + this.toString());
            resp = new Response(1, "true", "OK");
        } catch (UnsupportedOperationException uoEx) {
            Logger.getLogger(SMServer.class.getName()).log(Level.WARNING, "Operación no soportada.", uoEx);
            resp = new Response(-1, "false", "Operación no soportada.");
        }  catch (JSONException jsEx){
            Logger.getLogger(SMServer.class.getName()).log(Level.WARNING, "Error al procesar una soliticud de actualización.", jsEx);
            resp = new Response(-1, "false", jsEx.toString());
        }
        return AES.encrypt(resp.toString(), this.PASSWORD);
    }


    //Quitar si no funciona intento A******

    public String opera(String json) throws RemoteException{
        Response resp;
        json = AES.decrypt(json, this.PASSWORD);
        try {
            JSONObject request = new JSONObject(json);

            if(request.getInt("oper") != 2) {

                String key = request.getString("key");
                int operation = request.getInt("oper");
                double value = request.getDouble("value");

                //Arma evento
                Evento_Mssg evt = Lamport.arma_Evento(operation, key, value);
                //Lamport agrega
                Lamport.agrega_Evento(evt);

                return Lamport.revisaEvento();
            }else{
                return fastGet( request.toString() );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            String name = "StateMachine";
            SMServer engine = new SMServer();
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, engine);
            Logger.getLogger(SMServer.class.getName()).log(Level.INFO, "El servidor se ha iniciado exitosamente.");

            Lamport.initialize(3, engine);

            //Agregar Sockets
            //Manejador de sockets (extiende a Thread) para .start()
            ManejadorSockets_Serv servidor_Sockets = new ManejadorSockets_Serv();
            servidor_Sockets.run();

        } catch (RemoteException ex) {
            Logger.getLogger(SMServer.class.getName()).log(Level.SEVERE, "Error al iniciar el servidor.", ex);
        }
    }
}
