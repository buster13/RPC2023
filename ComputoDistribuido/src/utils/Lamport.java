package utils;

import java.io.IOException;
import java.util.PriorityQueue;

public class Lamport {

    private static int reloj;
    private static int id_nodo;
    public static PriorityQueue<Evento_Mssg> messages_queue = new PriorityQueue<Evento_Mssg>();
    private static int msg_recieved;
    private static EnviaSockets manejador;

    //Variables para sockets
    //private final int Puerto = 1234;
    //private final String HOST = "localhost";
    //protected ServerSocket ss

    public static void initialize(int id) {
        id_nodo = id;
        reloj = 0;
        msg_recieved = 0;
    }

    public static Evento_Mssg arma_Evento(int oper, int var, double val){
        return new Evento_Mssg(0, id_nodo, reloj, oper, var, val);
    }

    public static void aumenta_Received(){
        msg_recieved ++;

        if(msg_recieved == 2){
            quita_Evento();
        }
    }

    public static void agrega_Evento(Evento_Mssg evt) throws IOException {
        switch(evt.getTipo()){
            case 0: //local
                evt.setTiempo( reloj + 1);
                messages_queue.add(evt);
                manejador.mandaInstrucciones(evt);
                msg_recieved = 0;
                break;
            case 1:
                reloj = Math.max(reloj, evt.getTiempo()) + 1;
                messages_queue.add(evt);
                break;
        }

    }


 //sacar eventos de la cola y pedirle al SMServer que ejecute


    public static void quita_Evento() throws IOException {
        // Get the next event from the queue
        Evento_Mssg evt = messages_queue.poll();

        // Execute the event
        if (evt != null) {
            // If it's a local event, execute it directly
            if (evt.getTipo() == 0) {
               
            }
            // If it's a remote event, update the clock and then execute it
            else if (evt.getTipo() == 1) {
                reloj = Math.max(reloj, evt.getTiempo()) + 1;
                // execute remote event here
            }

            // Recursively process any remaining events in the queue
            quita_Evento();
        }
    }


}