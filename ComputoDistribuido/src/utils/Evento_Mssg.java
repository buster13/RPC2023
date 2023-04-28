package utils;

import org.json.JSONObject;

public class Evento_Mssg implements Comparable<Evento_Mssg>{
    public static final String RESPONSE_ACCEPT = "ACCEPT";
    public static final String RESPONSE_REJECT = "REJECT";

    public static final int TYPE_LOCAL = 0;
    public static final int TYPE_REMOTE = 1;

    private int tipo;
    private int id_sender;

    private int tiempo;

    public int oper;
    public String key;
    public double val;
    public float result;

    public int resp_In;
    public int resp_Out;
    public String content;
    public boolean isProcessed;


    public Evento_Mssg(int tipo, int id_sender, int tiempo, int oper, String key, double val){
        this.tipo = tipo;
        this.id_sender = id_sender;

        this.oper = oper;
        this.key = key;
        this.val = val;
        this.tiempo = tiempo;
    }

    public int getId_sender() {
        return id_sender;
    }

    public int[] getPrioridad(){
        int[] prioridad = new int[2];

        prioridad[0] = id_sender;
        prioridad[1] = tiempo;

        return prioridad;
    }

    public void setTiempo(int tiempo){
        this.tiempo = tiempo;
    }
    public int getTiempo(){
        return this.tiempo;
    }
    public int getTipo(){
        return this.tipo;
    }

    public String toString(){
        JSONObject temp = new JSONObject();

        temp.put("key", key);
        temp.put("oper", oper);
        temp.put("value", val);
        temp.put("sender", id_sender);
        temp.put("TS", tiempo);

        return temp.toString();
    }

    @Override
    public int compareTo(Evento_Mssg evt) {

        int[] otro_Prioridad = evt.getPrioridad();

        if(otro_Prioridad[0] == this.id_sender && otro_Prioridad[1] == this.tiempo){
            return 0;
        } else if (otro_Prioridad[0] > this.id_sender || otro_Prioridad[1] > this.tiempo) {
            return 1;
        } else {
            return -1;
        }
    }
}
