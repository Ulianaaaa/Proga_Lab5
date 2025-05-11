package common.commands;

import server.CommandManager;

import java.util.Map;

public class Help implements Command {
    private static final long serialVersionUID = 1L;

    private final CommandManager commandManager;

    public Help(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public String execute(Object argument) {
        StringBuilder sb = new StringBuilder("Доступные команды:\n");
        for (Map.Entry<String, Command> entry : commandManager.getCommands().entrySet()) {
            sb.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue().getDescription())
                    .append("\n");
        }
        return sb.toString();
    }

    @Override
    public String getDescription() {
        return "вывести справку по доступным командам";
    }
}