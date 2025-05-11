package server;

import common.commands.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    public CommandManager(CollectionManager collectionManager, DataProvider dataProvider, String fileName, Scanner scanner) {
        registerCommands(collectionManager, dataProvider, fileName, scanner);
    }

    private void registerCommands(CollectionManager collectionManager, DataProvider dataProvider, String fileName, Scanner scanner) {
        commands.put("help", new Help(this));
        commands.put("info", new Info(collectionManager));
        commands.put("exit", new Exit());
        commands.put("show", new Show(collectionManager));
        commands.put("add", new Add(collectionManager));
        commands.put("clear", new Clear(collectionManager));
        commands.put("save", new Save(collectionManager, dataProvider, fileName));
        commands.put("remove_by_id", new RemoveById(collectionManager));
        commands.put("update", new UpdateById(collectionManager, scanner)); // Передаем scanner
        commands.put("add_if_max", new AddIfMax(collectionManager));
        commands.put("add_if_min", new AddIfMin(collectionManager));
        commands.put("remove_lower", new RemoveLower(collectionManager, scanner)); // Передаем scanner
        commands.put("min_by_furnish", new MinByFurnish(collectionManager));
        commands.put("count_greater_than_number_of_rooms", new NumberOfRooms(collectionManager));
        commands.put("filter_by_transport", new FilterByTransport(collectionManager));
        commands.put("execute_script", new ExecuteScript(this, collectionManager));
    }

    public Command getCommand(String commandName) {
        return commands.get(commandName);
    }

    public void executeCommand(String commandLine) {
        String[] parts = commandLine.split(" ", 2);  // Разделяем команду и аргументы
        String commandName = parts[0];
        String args = parts.length > 1 ? parts[1] : null;

        Command command = getCommand(commandName);
        if (command != null) {
            String result = command.execute(args);
            if (result != null && !result.isEmpty()){
                System.out.println(result);
            }
        } else {
            System.out.println("Неизвестная команда: " + commandName);
        }
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}