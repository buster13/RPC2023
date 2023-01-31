package messages;

import org.json.JSONObject;
public class Response {
    private final JSONObject resp;

    public Response(int type, String value, String status){
        this.resp = new JSONObject();
        this.resp.put("type", type);
        this.resp.put("response", value);
        this.resp.put("status", status);
    }

    @Override
    public String toString() {
        return this.resp.toString();
    }
}
