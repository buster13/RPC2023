package utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ManejadorSockets_Serv extends Thread{

    @Override
    public void run()
    {
        System.out.println("Server sockets ready");
        try {
            // server is listening on port 5056
            ServerSocket ss = new ServerSocket(5056);

            // running infinite loop for getting
            // client request
            while (true)
            {
                Socket s = null;
                try
                {
                    // socket object to receive incoming client requests
                    System.out.println("Esperando conexión");
                    s = ss.accept();

                    System.out.println("Se conectó alguien nuevo : " + s);

                    // obtaining input and out streams
                    DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
                    DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());

                    System.out.println("Asignando hilo a nuevo cliente");

                    // create a new thread object
                    Thread t = new Escucha_Clientes(s, dataInputStream, dataOutputStream);

                    // Invoking the start() method
                    t.start();

                }
                catch (Exception e){
                    s.close();
                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            //Logger.getLogger(SocketGeekForSeek.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
