package client;

import interfaces.StateMachine;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class SMClient {
    public static void main(String[] args) {
        Scanner scan= new Scanner(System.in);

        try {
            String name = "StateMachine";
            Registry registry = LocateRegistry.getRegistry("localhost");
            StateMachine sm = (StateMachine) registry.lookup(name);



            while(true) {
                System.out.println("Elije la variable: \n" +
                        "A... (0) \n" +
                        "B ... (1) \n" );
                int var= scan.nextInt();
                System.out.println("Elije la operación: \n" +
                        "Set ... (0) \n" +
                        "Add ... (1) \n" +
                        "Mult ... (2) \n" +
                        "Get ... (3) \n"
                );
                int op= scan.nextInt();

                switch (op){
                    case(0):{
                        System.out.println("Valor nuevo:" );
                        double nuevo= scan.nextDouble();
                        System.out.println(sm.update(var,op,nuevo));
                        break;
                    }
                    case(1):{
                        System.out.println("Sumando:" );
                        double sumando= scan.nextDouble();
                        System.out.println(sm.update(var,op,sumando));
                        break;
                    }
                    case(2):{
                        System.out.println("Factor:" );
                        double factor= scan.nextDouble();
                        System.out.println(sm.update(var,op,factor));
                        break;
                    }
                    case(3):{
                        System.out.println(sm.read(var));
                        break;
                    }
                }
                System.out.println("¿Deseas continuar? \n" +
                        "Si... (0) \n" +
                        "No... (-1) \n" );
                int cont= scan.nextInt();
                if(cont == -1){
                    break;
                }

            }

        } catch (Exception ex) {
            Logger.getLogger(SMClient.class.getName()).log(Level.SEVERE, "Error al intentar conectar con el servidor.", ex);
        }
    }
}
