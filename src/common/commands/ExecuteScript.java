package common.commands;

import server.CollectionManager;
import server.CommandManager;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class ExecuteScript implements Command {
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;
    private static final Set<Path> executingScripts = new HashSet<>();
    private static final int MAX_SCRIPT_DEPTH = 10;
    private int currentDepth = 0;

    public ExecuteScript(CommandManager commandManager, CollectionManager collectionManager) {
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
    }

    public void setCurrentDepth(int depth) {
        this.currentDepth = depth;
    }

    @Override
    public String execute(Object argument) {
        if (argument == null || !(argument instanceof String)) {
            return "Ошибка: необходимо указать имя файла (пример: execute_script script.txt)";
        }

        String scriptFileName = ((String) argument).trim();
        Path scriptPath = Paths.get(scriptFileName).toAbsolutePath().normalize();

        if (executingScripts.contains(scriptPath)) {
            return "Ошибка: рекурсивный вызов скрипта '" + scriptFileName + "'";
        }

        if (currentDepth >= MAX_SCRIPT_DEPTH) {
            return "Ошибка: превышена максимальная глубина вложенности (" + MAX_SCRIPT_DEPTH + ")";
        }

        executingScripts.add(scriptPath);
        currentDepth++;

        try (BufferedReader reader = new BufferedReader(new FileReader(scriptPath.toFile()))) {
            StringBuilder output = new StringBuilder();
            output.append(">>> Начало выполнения скрипта: ").append(scriptFileName).append("\n");

            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty() || line.startsWith("//")) {
                    continue;
                }

                output.append("[Строка ").append(lineNumber).append("] ").append(line).append("\n");
                String result = commandManager.executeCommandFromScript(line, currentDepth);
                output.append(result).append("\n");
            }

            output.append("<<< Выполнение скрипта завершено\n");
            return output.toString();

        } catch (FileNotFoundException e) {
            return "Ошибка: файл не найден - " + scriptFileName;
        } catch (IOException e) {
            return "Ошибка чтения файла: " + e.getMessage();
        } finally {
            executingScripts.remove(scriptPath);
            currentDepth--;
        }
    }

    @Override
    public String getDescription() {
        return "execute_script file_name : выполнить скрипт из файла";
    }
}