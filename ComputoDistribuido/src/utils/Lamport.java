package utils;

import org.json.JSONObject;
import server.SMServer;

import java.io.IOException;
import java.util.PriorityQueue;

public class Lamport {

    private static int reloj;
    private static int id_nodo;
    public static PriorityQueue<Evento_Mssg> messages_queue = new PriorityQueue<Evento_Mssg>();
    private static int msg_recieved;
    private static EnviaSockets manejador;
    private static SMServer engine;

    //Variables para sockets
    //private final int Puerto = 1234;
    //private final String HOST = "localhost";
    //protected ServerSocket ss

    public static void initialize(int id, SMServer server) {
        id_nodo = id;
        reloj = 0;
        msg_recieved = 0;
        engine = server;
        manejador = new EnviaSockets();
    }

    public static Evento_Mssg arma_Evento(int oper, int var, double val){
        return new Evento_Mssg(0, id_nodo, reloj, oper, var, val);
    }

    public static void aumenta_Received() throws IOException {
        msg_recieved ++;

    }

    public static void agrega_Evento(Evento_Mssg evt) throws IOException {
        switch(evt.getTipo()){
            case 0: //local
                evt.setTiempo( reloj + 1);
                messages_queue.add(evt);
                System.out.println("llegué aquí");
                manejador.mandaInstrucciones(evt);
                msg_recieved = 0;
                break;
            case 1:
                reloj = Math.max(reloj, evt.getTiempo()) + 1;
                messages_queue.add(evt);
                manejador.mandaAck(evt.getId_sender());
                break;
        }

    }


    public static String revisaEvento() throws IOException {
        int i = 0;
        while(msg_recieved != 2){
            System.out.print("");
        };
        return quita_Evento();
    }

 //sacar eventos de la cola y pedirle al SMServer que ejecute


    public static String quita_Evento() throws IOException {
        // Get the next event from the queue
        Evento_Mssg evt = messages_queue.poll();

        // Execute the event
        if (evt != null) {
            // If it's a local event, execute it directly
            if (evt.getTipo() == 0) {
                JSONObject json = new JSONObject();

                json.put("var", evt.var);
                json.put("oper", evt.oper);
                json.put("value", evt.val);

                if (evt.oper == 0) {
                    manejador.mandaRelease();
                    return engine.get(json.toString());
                } else {
                    manejador.mandaRelease();
                    return engine.update(json.toString());
                }

            }
            // If it's a remote event, update the clock and then execute it
            else if (evt.getTipo() == 1) {
                JSONObject json = new JSONObject();

                json.put("var", evt.var);
                json.put("oper", evt.oper);
                json.put("value", evt.val);

                if (evt.oper == 0) {
                    engine.get(json.toString());
                } else {
                    engine.update(json.toString());
                }
            }

        }
        return "";
    }

}