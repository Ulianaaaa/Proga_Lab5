package CommandsProvider.Commands;

import CommandsProvider.*;

import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;

/**
 * Команда для добавления элемента, если он меньше минимального в коллекции по площади.
 */
public class AddIfMin implements Command {
    private final CollectionManager collectionManager;
    private final Scanner scanner;

    /**
     * Конструктор команды AddIfMin
     * @param collectionManager менеджер коллекции
     * @param scanner объект для ввода данных
     */
    public AddIfMin(CollectionManager collectionManager, Scanner scanner) {
        this.collectionManager = collectionManager;
        this.scanner = scanner;
    }

    @Override
    public void execute(String args) {
        try {
            System.out.println("\n=== Добавление элемента, если он меньше минимального (по площади) ===");

            // Используем FlatBuilder для создания объекта
            FlatBuilder flatBuilder = new FlatBuilder(scanner, collectionManager);
            Flat newFlat = flatBuilder.buildFlat();

            // Находим минимальный элемент по площади
            Optional<Flat> minFlat = collectionManager.getFlats().stream()
                    .min(Comparator.comparingLong(Flat::getArea));

            // Сравниваем и добавляем
            if (minFlat.isEmpty() || newFlat.getArea() < minFlat.get().getArea()) {
                collectionManager.addFlat(newFlat);
                System.out.println("Элемент добавлен (ID: " + newFlat.getId() + ")");
            } else {
                System.out.println("Элемент не добавлен — его площадь не меньше минимальной.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка ввода: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "добавить элемент, если его площадь меньше минимальной в коллекции";
    }
}