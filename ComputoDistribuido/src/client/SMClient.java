package client;

import interfaces.StateMachine;
import utils.AES;
import utils.Request;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class SMClient {
    public static void main(String[] args) {
        String PASSWORD = "ThisIsNotThePassword";
        Scanner scan= new Scanner(System.in);

        try {
            String name = "StateMachine";
            Registry registry = LocateRegistry.getRegistry("localhost");
            StateMachine sm = (StateMachine) registry.lookup(name);

            int var, op;
            double num = 0;

            while(true) {
                System.out.println("Elije la variable: \n" +
                        "A... (0) \n" +
                        "B ... (1)" );
                while(!scan.hasNextInt()){
                    System.out.println("Ingresa un entero válido");
                    scan.next();
                }
                var = scan.nextInt();

                System.out.println("Elije la operación: \n" +
                        "Set ... (0) \n" +
                        "Add ... (1) \n" +
                        "Mult ... (2) \n" +
                        "Get ... (3)"
                );
                while(!scan.hasNextInt()){
                    System.out.println("Ingresa un entero válido");
                    scan.next();
                }
                op= scan.nextInt();

                switch (op){
                    case(0):
                    case(1):
                    case(2):{
                        System.out.print("Valor nuevo: " );
                        while(!scan.hasNextDouble()){
                            System.out.println("Ingresa un número válido");
                            scan.next();
                        }
                        num = scan.nextDouble();
                        break;
                    }
                }

                Request request = new Request(var, op, num);
                String req = AES.encrypt(request.toString(), PASSWORD);
                if(op == 3){
                    System.out.println(AES.decrypt(sm.read(req), PASSWORD));
                } else {
                    System.out.println(AES.decrypt(sm.update(req), PASSWORD));
                }

                System.out.println("¿Deseas continuar? \n" +
                        "Si... (0) \n" +
                        "No... (-1)" );
                while(!scan.hasNextInt()){
                    System.out.println("Ingresa un entero válido");
                    scan.next();
                }
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
