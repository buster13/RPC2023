package utils;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class EnviaSockets extends Thread {
    private ArrayList<Nodo> nodos;

    public EnviaSockets(){
        this.nodos = new ArrayList();



        this.nodos.add(new Nodo("148.205.36.212",5000, 1)); //Vianey
        this.nodos.add(new Nodo("148.205.36.214",5000, 2)); //Fernando
        //this.nodos.add(new Nodo("148.205.36.211",5000, 3)); //Dario
    }

    public void mandaInstrucciones(Evento_Mssg msg) throws IOException {
        Nodo n;
        for (int i = 0; i < nodos.size(); i++) {
            n = nodos.get(i);
            mandaInstruccionANodo(msg, n);
        }
    }

    public void mandaInstruccionANodo(Evento_Mssg msg, Nodo node){
        Socket socket;
        DataOutputStream out;
        String mensaje;
        String target;
        String action;
        try {

            socket = new Socket(node.getHost(), node.getPort());
            out = new DataOutputStream(socket.getOutputStream());


            //Mensaje a enviar
            mensaje = msg.toString();

            System.out.println("Enviando mensaje a :"+node.getHost());
            System.out.println("Mensaje a enviar: "+mensaje);
            out.writeUTF(mensaje);
            System.out.println("Mensaje enviado");
            //in.close();
            out.close();
            socket.close();
        } catch (Exception ex) {
            //Logger.getLogger(ManejadorSockets.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.toString());
        }

    }

    public void mandaRelease() throws IOException {
        Nodo n;
        for (int i = 0; i < nodos.size(); i++) {
            n = nodos.get(i);
            mandaReleaseANodo(n);
        }
    }

    public void mandaReleaseANodo(Nodo node){
        Socket socket;
        DataOutputStream out;
        String mensaje;
        String target;
        String action;
        try {

            socket = new Socket(node.getHost(), node.getPort());
            out = new DataOutputStream(socket.getOutputStream());


            //Mensaje a enviar
            mensaje = "release";

            System.out.println("Enviando mensaje a :"+node.getHost());
            System.out.println("Mensaje a enviar: "+mensaje);
            out.writeUTF(mensaje);
            System.out.println("Mensaje enviado");
            //in.close();
            out.close();
            socket.close();
        } catch (Exception ex) {
            //Logger.getLogger(ManejadorSockets.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.toString());
        }

    }

    public void mandaAck(int id_sender){
        Socket socket;
        DataOutputStream out;
        String mensaje;
        String target;
        String action;

        Nodo node = nodos.get(0);
        for (int i = 0; i < nodos.size(); i++) {
            if(nodos.get(i).getId()==id_sender)
                node = nodos.get(i);
        }

        try {

            socket = new Socket(node.getHost(), node.getPort());
            out = new DataOutputStream(socket.getOutputStream());


            //Mensaje a enviar
            mensaje = "acknowledge";

            System.out.println("Enviando mensaje a :"+node.getHost());
            System.out.println("Mensaje a enviar: "+mensaje);
            out.writeUTF(mensaje);
            System.out.println("Mensaje enviado");
            //in.close();
            out.close();
            socket.close();
        } catch (Exception ex) {
            //Logger.getLogger(ManejadorSockets.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.toString());
        }

    }

}
