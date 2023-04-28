package utils;


import org.json.JSONObject;

public class Request {
    private final JSONObject req;

    public Request(String variable, int operation, double value){
        this.req = new JSONObject();
        this.req.put("var", variable);
        this.req.put("oper", operation);
        this.req.put("value", value);
    }

    @Override
    public String toString() {
        return this.req.toString();
    }
}
