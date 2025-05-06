package CommandsProvider;

public interface Command {
    void execute(String args);
    String getDescription();
}
