package CommandsProvider.Commands;

import CommandsProvider.*;

import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;

/**
 * Команда для добавления элемента, если он больше максимального в коллекции по площади.
 */
public class AddIfMax implements Command {
    private final CollectionManager collectionManager;
    private final Scanner scanner;

    /**
     * Конструктор команды AddIfMax
     * @param collectionManager менеджер коллекции
     * @param scanner объект для ввода данных
     */
    public AddIfMax(CollectionManager collectionManager, Scanner scanner) {
        this.collectionManager = collectionManager;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        try {
            System.out.println("\n=== Добавление элемента, если он больше максимального (по площади) ===");

            // Используем FlatBuilder для создания нового объекта
            FlatBuilder flatBuilder = new FlatBuilder(scanner, collectionManager);
            Flat newFlat = flatBuilder.buildFlat();

            // Находим максимальный элемент по площади
            Optional<Flat> maxFlat = collectionManager.getFlats().stream()
                    .max(Comparator.comparingLong(Flat::getArea));

            // Сравнение и добавление
            if (maxFlat.isEmpty() || newFlat.getArea() > maxFlat.get().getArea()) {
                collectionManager.addFlat(newFlat);
                System.out.println("Элемент добавлен (ID: " + newFlat.getId() + ")");
            } else {
                System.out.println("Элемент не добавлен — его площадь не больше максимальной.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "добавить элемент, если его площадь больше, чем у максимального в коллекции";
    }
}