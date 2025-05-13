package common.network;// CommandResponse.java
import java.io.Serial;
import java.io.Serializable;

public class CommandResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String responseText;

    public CommandResponse(String responseText) {
        this.responseText = responseText;
    }

    public String getResponseText(){
        return responseText;
    }
    @Override
    public String toString(){
        return responseText;
    }
}