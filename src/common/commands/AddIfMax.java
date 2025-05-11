package common.commands;

import client.FlatBuilder;
import common.models.Flat;
import server.CollectionManager;

import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;

/**
 * Команда для добавления элемента, если он больше максимального в коллекции по площади.
 */
public class AddIfMax implements Command {
    private final CollectionManager collectionManager;
    private final FlatBuilder flatBuilder;

    /**
     * Конструктор команды AddIfMax
     *
     * @param collectionManager менеджер коллекции
     */
    public AddIfMax(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
        this.flatBuilder = new FlatBuilder(new Scanner(System.in), collectionManager);
    }

    @Override
    public String execute(Object argument) {
        try {
            StringBuilder result = new StringBuilder("\n=== Добавление элемента, если он больше максимального (по площади) ===\n");

            Flat newFlat = flatBuilder.buildFlat();

            Optional<Flat> maxFlat = collectionManager.getFlats().stream()
                    .max(Comparator.comparingLong(Flat::getArea));

            if (maxFlat.isEmpty() || newFlat.getArea() > maxFlat.get().getArea()) {
                collectionManager.addFlat(newFlat);
                result.append("Элемент добавлен (ID: ").append(newFlat.getId()).append(")");
            } else {
                result.append("Элемент не добавлен — его площадь не больше максимальной.");
            }

            return result.toString();

        } catch (IllegalArgumentException e) {
            return "Ошибка ввода: " + e.getMessage();
        } catch (Exception e) {
            return "Ошибка: " + e.getMessage();
        }
    }

    @Override
    public String getDescription() {
        return "добавить элемент, если его площадь больше, чем у максимального в коллекции";
    }
}