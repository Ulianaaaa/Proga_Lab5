package CommandsProvider.Commands;

import CommandsProvider.*;

import java.util.Scanner;

/**
 * Команда remove_lower : удалить все элементы из коллекции, меньшие чем заданный элемент по площади.
 */
public class RemoveLower implements Command {
    private final CollectionManager collectionManager;
    private final Scanner scanner;

    public RemoveLower(CollectionManager collectionManager, Scanner scanner) {
        this.collectionManager = collectionManager;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        try {
            System.out.println("\n=== Удаление элементов, меньших заданного (по площади) ===");

            // Используем FlatBuilder для создания объекта
            FlatBuilder flatBuilder = new FlatBuilder(scanner, collectionManager);
            Flat inputFlat = flatBuilder.buildFlat();

            long referenceArea = inputFlat.getArea();
            int initialSize = collectionManager.getFlats().size();

            // Удаляем все элементы, у которых площадь меньше заданной
            collectionManager.getFlats().removeIf(flat -> flat.getArea() < referenceArea);

            int removedCount = initialSize - collectionManager.getFlats().size();
            if (removedCount > 0) {
                System.out.println("Удалено " + removedCount + " элемент(ов), меньших по площади.");
            } else {
                System.out.println("Нет элементов, меньших по площади.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "удалить все элементы из коллекции, меньшие по площади, чем заданный элемент";
    }
}