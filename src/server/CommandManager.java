package server;

import common.commands.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();
    private final CollectionManager collectionManager;
    private final Scanner scanner;

    public CommandManager(CollectionManager collectionManager,
                          DataProvider dataProvider,
                          String fileName,
                          Scanner scanner) {
        this.collectionManager = collectionManager;
        this.scanner = scanner;
        registerCommands(dataProvider, fileName);
    }

    private void registerCommands(DataProvider dataProvider, String fileName) {
        // Основные команды
        commands.put("help", new Help(this));
        commands.put("info", new Info(collectionManager));
        commands.put("show", new Show(collectionManager));
        commands.put("add", new Add(collectionManager));
        commands.put("clear", new Clear(collectionManager));
        commands.put("remove_by_id", new RemoveById(collectionManager));
        commands.put("update", new UpdateById(collectionManager));


        // Команды условного добавления (исправленные)
        commands.put("add_if_max", new AddIfMax(collectionManager));
        commands.put("add_if_min", new AddIfMin(collectionManager));

        commands.put("remove_lower", new RemoveLower(collectionManager));
        commands.put("min_by_furnish", new MinByFurnish(collectionManager));
        commands.put("count_greater_than_number_of_rooms", new NumberOfRooms(collectionManager));
        commands.put("filter_by_transport", new FilterByTransport(collectionManager));

        // Системные команды
        commands.put("save", new Save(collectionManager, dataProvider, fileName));
        commands.put("execute_script", new ExecuteScript(this, collectionManager));
        commands.put("exit", new Exit());
    }

    public void executeCommand(String commandLine) {
        if (commandLine == null || commandLine.trim().isEmpty()) {
            System.out.println("Ошибка: пустая команда");
            return;
        }

        String[] parts = commandLine.split("\\s+", 2);
        String commandName = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1].trim() : null;

        Command command = commands.get(commandName);
        if (command != null) {
            try {
                String result = command.execute(args);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println("Ошибка выполнения команды '" + commandName + "': " + e.getMessage());
            }
        } else {
            System.out.println("Неизвестная команда: " + commandName);
            System.out.println("Введите 'help' для списка доступных команд");
        }
    }

    public String executeCommandFromScript(String commandLine, int depth) {
        if (commandLine == null || commandLine.trim().isEmpty()) {
            return "[Ошибка] Пустая команда в скрипте";
        }

        String[] parts = commandLine.split("\\s+", 2);
        String commandName = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1].trim() : null;

        Command command = commands.get(commandName);
        if (command != null) {
            try {
                if ("execute_script".equals(commandName)) {
                    ((ExecuteScript) command).setCurrentDepth(depth);
                }
                return command.execute(args);
            } catch (Exception e) {
                return "[Ошибка выполнения] " + e.getMessage();
            }
        }
        return "[Ошибка] Неизвестная команда: " + commandName;
    }

    public Map<String, Command> getCommands() {
        return new HashMap<>(commands);
    }
}