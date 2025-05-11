package common.commands;

import server.CollectionManager;
import server.CommandManager;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Команда execute_script: считать и исполнить команды из указанного файла.
 */
public class ExecuteScript implements Command {
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;
    private static final Set<String> executingScripts = new HashSet<>();

    public ExecuteScript(CommandManager commandManager, CollectionManager collectionManager) {
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        if (argument == null || !(argument instanceof String)) {
            return "Ошибка: необходимо указать имя файла. Пример: execute_script script.txt";
        }

        String scriptFileName = (String) argument;

        if (executingScripts.contains(scriptFileName)) {
            return "Ошибка: обнаружена рекурсивная попытка выполнения скрипта '" + scriptFileName + "'.";
        }

        executingScripts.add(scriptFileName); // Помечаем файл как "исполняющийся"

        try (BufferedReader reader = new BufferedReader(new FileReader(scriptFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    System.out.println(">> Выполняется команда из скрипта: " + line);
                    commandManager.executeCommand(line);
                }
            }
        } catch (FileNotFoundException e) {
            return "Ошибка: файл скрипта не найден: " + scriptFileName;
        } catch (IOException e) {
            return "Ошибка при чтении скрипта: " + e.getMessage();
        } finally {
            executingScripts.remove(scriptFileName); // После выполнения убираем из множества
        }

        return "Скрипт " + scriptFileName + " выполнен успешно.";
    }

    @Override
    public String getDescription() {
        return "Считать и исполнить команды из указанного файла";
    }
}