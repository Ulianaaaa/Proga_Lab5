package common.commands;

import common.models.Flat;
import server.CollectionManager;

import java.util.Comparator;
import java.util.Optional;

/**
 * Команда min_by_furnish : вывести элемент с минимальным значением поля furnish.
 */
public class MinByFurnish implements Command {
    private final CollectionManager collectionManager;

    public MinByFurnish(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        Optional<Flat> minFlat = collectionManager.getFlats().stream()
                .min(Comparator.comparing(Flat::getFurnish));

        if (minFlat.isPresent()) {
            return "Элемент с минимальной отделкой:\n" + minFlat.get();
        } else {
            return "Коллекция пуста.";
        }
    }

    @Override
    public String getDescription() {
        return "вывести элемент с минимальным значением поля furnish";
    }
}