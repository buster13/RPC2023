package client;

import interfaces.StateMachine;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SMClient {
    public static void main(String[] args) {
        try {
            String name = "StateMachine";
            Registry registry = LocateRegistry.getRegistry("localhost");
            StateMachine sm = (StateMachine) registry.lookup(name);
            System.out.println(sm.read(1));
            sm.update(1,1,19);

            System.out.println(sm.read(1));
        } catch (Exception ex) {
            Logger.getLogger(SMClient.class.getName()).log(Level.SEVERE, "Error al intentar conectar con el servidor.", ex);
        }
    }
}
