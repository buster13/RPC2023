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

            String var;
            int op;
            double num = 0;

            while(true) {
                System.out.println("Escribe la variable: \n");
                var = scan.next();

                System.out.println("Elije la operación: \n" +
                        "Put ... (0) \n" +
                        "Get ... (1) \n" +
                        "Fast-get ... (2) \n"
                );
                while(!scan.hasNextInt()){
                    System.out.println("Ingresa un entero válido");
                    scan.next();
                }
                op= scan.nextInt();

                if(op == 0){
                    System.out.print("Valor nuevo: " );
                    while(!scan.hasNextDouble()){
                        System.out.println("Ingresa un número válido");
                        scan.next();
                    }
                    num = scan.nextDouble();
                }



                Request request = new Request(var, op, num);
                String req = AES.encrypt(request.toString(), PASSWORD);

                switch (op){
                    case(0):{
                        System.out.println(AES.decrypt(sm.opera(req), PASSWORD));
                        break;
                    }
                    case(1):{
                        System.out.println(AES.decrypt(sm.opera(req), PASSWORD));
                        break;
                    }
                    case(2):{
                        System.out.println(AES.decrypt(sm.fastGet(req), PASSWORD));
                        break;
                    }
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
