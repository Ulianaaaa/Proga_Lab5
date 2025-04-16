package CommandsProvider.Commands;

import CommandsProvider.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Команда execute_script : считать и исполнить команды из указанного файла.
 */
public class ExecuteScript implements Command {
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;
    private final Scanner scanner;

    public ExecuteScript(CommandManager commandManager, CollectionManager collectionManager, Scanner scanner) {
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Введите имя файла скрипта: ");
        String scriptFileName = scanner.nextLine().trim();

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
        }
    }

    @Override
    public String getDescription() {
        return " считать и исполнить скрипт из указанного файла";
    }
}