import CommandsProvider.CollectionManager;
import CommandsProvider.CommandManager;
import CommandsProvider.Flat;
import Data.DataProvider;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.File;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Главный класс приложения для управления коллекцией объектов Flat
 */
public class Main {
    private static final AtomicBoolean running = new AtomicBoolean(true);

    public static void main(String[] args) {
        // Установка обработчика для SIGINT (Command+C на Mac, Ctrl+C на других ОС)
        try {
            Signal.handle(new Signal("INT"), signal -> {
                System.out.println("\nПолучен сигнал завершения (Command+C/Ctrl+C)");
                running.set(false);
                System.exit(0); // Немедленный выход
            });
        } catch (IllegalArgumentException e) {
            System.err.println("WARN: Не удалось зарегистрировать обработчик сигналов (работает только в Oracle JDK)");
        }

        if (args.length < 1) {
            System.out.println("Ошибка: укажите имя файла с коллекцией как аргумент командной строки.");
            System.exit(1);
        }

        String fileName = args[0];
        File file = new File(fileName);

        // Проверка прав доступа к файлу
        if (file.exists() && (!file.canRead() || !file.canWrite())) {
            System.out.println("Ошибка: недостаточно прав для чтения/записи файла");
            System.exit(1);
        }

        // Создаём файл, если он не существует
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("Файл не найден. Создан новый файл: " + fileName);
                }
            } catch (Exception e) {
                System.out.println("Ошибка при создании файла: " + e.getMessage());
                System.exit(1);
            }
        }

        DataProvider dataProvider = new DataProvider();
        TreeSet<Flat> loadedFlats = dataProvider.load(fileName);
        CollectionManager collectionManager = new CollectionManager(loadedFlats);
        CommandManager commandManager = new CommandManager(collectionManager, dataProvider, fileName);

        System.out.println("Добро пожаловать! Введите 'help' для просмотра доступных команд.");
        System.out.println("Для выхода используйте Command+C (Mac) или Ctrl+D");

        Scanner scanner = new Scanner(System.in);
        while (running.get()) {
            try {
                System.out.print("> "); // Приглашение для ввода
                if (!scanner.hasNextLine()) {
                    // Обработка Ctrl+D (EOF)
                    System.out.println("\nОбнаружен конец ввода (Ctrl+D). Завершение работы...");
                    break;
                }

                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("exit")) {
                    break;
                }
                if (!input.isEmpty()) {
                    commandManager.executeCommand(input);
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }

        // Корректное завершение
        System.out.println("Работа программы завершена.");
        System.exit(0);
    }
}