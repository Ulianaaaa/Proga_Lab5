package CommandsProvider.Commands;

import CommandsProvider.Command;
import CommandsProvider.CollectionManager;
import CommandsProvider.Flat;

import java.util.TreeSet;

/**
 * Команда show : выводит все элементы коллекции в строковом представлении.
 */
public class Show implements Command {
    private final CollectionManager collectionManager;

    public Show(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute() {
        TreeSet<Flat> flats = collectionManager.getFlats();
        if (flats.isEmpty()) {
            System.out.println("Коллекция пуста.");
        } else {
            System.out.println("Элементы коллекции:");
            for (Flat flat : flats) {
                System.out.println(flat); // toString() у Flat должен быть реализован
            }
        }
    }

    @Override
    public String getDescription() {
        return " вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}