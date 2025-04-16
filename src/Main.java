import CommandsProvider.CollectionManager;
import CommandsProvider.CommandManager;
import CommandsProvider.Flat;
import Data.DataProvider;

import java.io.File;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Главный класс приложения для управления коллекцией объектов Flat
 */
public class Main {
    /**
     * Точка входа в приложение
     * @param args аргументы командной строки (первый аргумент - имя файла с коллекцией)
     */
    public static void main(String[] args) {
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
                boolean created = file.createNewFile();
                if (created) {
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

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                commandManager.executeCommand(input);
            }
        }
    }
}