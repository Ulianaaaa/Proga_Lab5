package common.network;// CommandRequest.java
import java.io.Serializable;

public class CommandRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String commandName;
    private Object argument;

    public CommandRequest(String commandName, Object argument) {
        this.commandName = commandName;
        this.argument = argument;
    }

    public String getCommandName(){
        return commandName;
    }
    public Object getArgument(){
        return argument;
    }
}