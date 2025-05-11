package common.commands;

import client.FlatBuilder;
import common.models.Flat;
import server.CollectionManager;

import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;

/**
 * Команда для добавления элемента, если он меньше минимального в коллекции по площади.
 */
public class AddIfMin implements Command {
    private final CollectionManager collectionManager;
    private final FlatBuilder flatBuilder;

    /**
     * Конструктор команды AddIfMin
     *
     * @param collectionManager менеджер коллекции
     */
    public AddIfMin(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
        this.flatBuilder = new FlatBuilder(new Scanner(System.in), collectionManager);
    }

    @Override
    public String execute(Object args) {
        try {
            StringBuilder result = new StringBuilder("\n=== Добавление элемента, если он меньше минимального (по площади) ===");

            Flat newFlat = flatBuilder.buildFlat();

            // Находим минимальный элемент по площади
            Optional<Flat> minFlat = collectionManager.getFlats().stream()
                    .min(Comparator.comparingLong(Flat::getArea));

            // Сравниваем и добавляем
            if (minFlat.isEmpty() || newFlat.getArea() < minFlat.get().getArea()) {
                collectionManager.addFlat(newFlat);
                return "Элемент добавлен (ID: " + newFlat.getId() + ")";
            } else {
                return "Элемент не добавлен — его площадь не меньше минимальной.";
            }

        } catch (IllegalArgumentException e) {
            return "Ошибка ввода: " + e.getMessage();
        } catch (Exception e) {
            return "Ошибка: " + e.getMessage();
        }
    }

    @Override
    public String getDescription() {
        return "добавить элемент, если его площадь меньше минимальной в коллекции";
    }
}