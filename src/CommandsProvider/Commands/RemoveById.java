package CommandsProvider.Commands;

import CommandsProvider.Command;
import CommandsProvider.CollectionManager;

import java.util.Scanner;

/**
 * Команда remove_by_id : удалить элемент из коллекции по его id.
 */
public class RemoveById implements Command {
    private final CollectionManager collectionManager;
    private final java.util.Scanner scanner;

    public RemoveById(CollectionManager collectionManager, Scanner scanner) {
        this.collectionManager = collectionManager;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Введите id: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            boolean removed = collectionManager.removeById(id);
            if (removed) {
                System.out.println("Элемент с id " + id + " удалён.");
            } else {
                System.out.println("Элемент с таким id не найден.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: id должен быть числом.");
        }
    }

    @Override
    public String getDescription() {
        return " удалить элемент из коллекции по его id";
    }
}