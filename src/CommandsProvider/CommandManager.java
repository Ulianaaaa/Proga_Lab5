package CommandsProvider;

import CommandsProvider.Commands.*;
import Data.DataProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandManager {

    private final Map<String, Command> commands = new HashMap<>();
    private final CollectionManager collectionManager;

    public CommandManager(CollectionManager collectionManager, DataProvider dataProvider, String fileName) {
        this.collectionManager = collectionManager;
        registerCommands(dataProvider, fileName);
    }

    private void registerCommands(DataProvider dataProvider, String fileName) {
        commands.put("help", new Help(this));
        commands.put("info", new Info(collectionManager));
        commands.put("exit", new Exit());
        commands.put("show", new Show(collectionManager));
        commands.put("add", new Add(collectionManager));
        commands.put("clear", new Clear(collectionManager));
        commands.put("save", new Save(collectionManager, dataProvider, fileName));
        commands.put("remove_by_id", new RemoveById(collectionManager, new Scanner(System.in)));
        commands.put("update", new UpdateById(collectionManager, new Scanner(System.in)));
        commands.put("execute_script", new ExecuteScript(this, collectionManager, new Scanner(System.in)));
        commands.put("add_if_max", new AddIfMax(collectionManager, new Scanner(System.in)));
        commands.put("add_if_min", new AddIfMin(collectionManager, new Scanner(System.in)));
        commands.put("remove_lower", new RemoveLower(collectionManager, new Scanner(System.in)));
        commands.put("min_by_furnish", new MinByFurnish(collectionManager));
        commands.put("count_greater_than_number_of_rooms", new NumberOfRooms(collectionManager, new Scanner(System.in)));
        commands.put("filter_by_transport", new FilterByTransport(collectionManager, new Scanner(System.in)));
    }

    public void executeCommand(String input) {
        String[] parts = input.trim().split("\\s+", 2);
        String commandName = parts[0];
        String args = parts.length > 1 ? parts[1] : "";

        Command command = commands.get(commandName);
        if (command != null) {
            command.execute(args); // передаём аргументы
        } else {
            System.out.println("Неизвестная команда. Введите 'help' для просмотра списка команд.");
        }
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}