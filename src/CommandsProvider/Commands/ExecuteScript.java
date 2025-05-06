package CommandsProvider.Commands;

import CommandsProvider.*;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Команда execute_script : считать и исполнить команды из указанного файла.
 */
public class ExecuteScript implements Command {
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;
    private final Scanner scanner;
    private static final Set<String> executingScripts = new HashSet<>();

    public ExecuteScript(CommandManager commandManager, CollectionManager collectionManager, Scanner scanner) {
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
        this.scanner = scanner;
    }

    @Override
    public void execute(String args) {
        if (args == null || args.trim().isEmpty()) {
            System.out.println("Ошибка: необходимо указать имя файла. Пример: execute_script script.txt");
            return;
        }

        String scriptFileName = args.trim();

        if (executingScripts.contains(scriptFileName)) {
            System.out.println("Ошибка: обнаружена рекурсивная попытка выполнения скрипта '" + scriptFileName + "'.");
            return;
        }

        executingScripts.add(scriptFileName); // помечаем файл как "исполняющийся"

        try (BufferedReader reader = new BufferedReader(new FileReader(scriptFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    System.out.println(">> Выполняется команда из скрипта: " + line);
                    commandManager.executeCommand(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении скрипта: " + e.getMessage());
        } finally {
            executingScripts.remove(scriptFileName); // после выполнения убираем из множества
        }
    }

    @Override
    public String getDescription() {
        return " считать и исполнить скрипт из указанного файла";
    }
}