package utils;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Escucha_Clientes extends Thread{


        final DataInputStream dis;
        final DataOutputStream dos;
        final Socket s;


        // Constructor
        public Escucha_Clientes(Socket s, DataInputStream dataInputStream, DataOutputStream dataOutputStream){
            this.s = s;
            this.dis = dataInputStream;
            this.dos = dataOutputStream;
        }

        @Override
        public void run()
        {
            String received;
            JSONObject jsonObject;
            Evento_Mssg evt;
            while (true)
            {

                try {
                // Recibiendo algo de otro servidor
                    received = dis.readUTF();

                    if(received.equals("Exit"))
                    {
                        System.out.println("El cliente " + this.s + " termina la conexión");
                        this.s.close();
                        break;
                    }

                    if(received.contains("release")){
                        System.out.println("Solicitud de release");

                        Lamport.quita_Evento();

                    }else{
                        if(received.contains("acknowledge")){
                            Lamport.aumenta_Received();

                        }else{
                            /*jsonObject = new JSONObject(received);
                            ci = new ClaseInstrucciones(Integer.parseInt(jsonObject.get("sender").toString()),
                                    Integer.parseInt(jsonObject.get("time").toString()),
                                    jsonObject.get("action").toString()+jsonObject.get("target").toString(),
                                    Integer.parseInt(jsonObject.get("value").toString()));
                            LOG.agregaInstruccion(ci);
                            */
                            jsonObject = new JSONObject(received);

                            evt = new Evento_Mssg(1, Integer.parseInt(jsonObject.get("id_sender").toString()),
                                    Integer.parseInt(jsonObject.get("tiempo").toString()),
                                    Integer.parseInt(jsonObject.get("oper").toString()),
                                    Integer.parseInt(jsonObject.get("var").toString()),
                                    Integer.parseInt(jsonObject.get("val").toString()));

                            Lamport.agrega_Evento(evt);


                            //Agregar al LOG la instrucción recibida

                            //System.out.println("Estatus actual de la cola de prioridad");
                            //System.out.println(log.toString());
                        }
                    }

                } catch (IOException e) {
                    //System.out.println(e.toString());

                }


            }

            try
            {
                // closing resources
                this.dis.close();
                this.dos.close();

            }catch(IOException e){
                e.printStackTrace();
            }
        }

}
