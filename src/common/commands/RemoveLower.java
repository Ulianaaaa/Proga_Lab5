package common.commands;

import client.FlatBuilder;
import common.models.Flat;
import server.CollectionManager;

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
    public String execute(Object argument) {
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
            return removedCount > 0
                    ? "Удалено " + removedCount + " элемент(ов), меньших по площади."
                    : "Нет элементов, меньших по площади.";

        } catch (IllegalArgumentException e) {
            return "Ошибка ввода: " + e.getMessage();
        } catch (Exception e) {
            return "Ошибка: " + e.getMessage();
        }
    }

    @Override
    public String getDescription() {
        return "удалить все элементы из коллекции, меньшие по площади, чем заданный элемент";
    }
}