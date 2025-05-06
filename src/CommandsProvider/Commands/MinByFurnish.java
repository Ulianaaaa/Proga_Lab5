package CommandsProvider.Commands;

import CommandsProvider.*;

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
    public void execute(String args) {
        // Используем метод min с компаратором, чтобы найти объект с минимальной отделкой
        Optional<Flat> minFlat = collectionManager.getFlats().stream()
                .min(Comparator.comparing(Flat::getFurnish));

        if (minFlat.isPresent()) {
            System.out.println("Элемент с минимальной отделкой: ");
            System.out.println(minFlat.get());
        } else {
            System.out.println("Коллекция пуста.");
        }
    }

    @Override
    public String getDescription() {
        return " вывести элемент с минимальным значением поля furnish";
    }
}