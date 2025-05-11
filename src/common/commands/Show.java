package common.commands;

import server.CollectionManager;
import common.models.Flat;

import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Команда show : выводит все элементы коллекции в строковом представлении.
 */
public class Show implements Command {
    private final CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        TreeSet<Flat> flats = collectionManager.getFlats();
        if (flats.isEmpty()) {
            return "Коллекция пуста.";
        } else {
            return flats.stream()
                    .map(Flat::toString)
                    .collect(Collectors.joining("\n", "Элементы коллекции:\n", ""));
        }
    }

    @Override
    public String getDescription() {
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}