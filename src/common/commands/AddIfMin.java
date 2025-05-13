package common.commands;

import common.models.Flat;
import server.CollectionManager;

import java.util.Comparator;
import java.util.Optional;

public class AddIfMin implements Command {
    private final CollectionManager collectionManager;

    public AddIfMin(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        if (!(argument instanceof Flat)) {
            return "Ошибка: ожидается объект Flat";
        }

        Flat newFlat = (Flat) argument;
        Optional<Flat> minFlat = collectionManager.getFlats().stream()
                .min(Comparator.comparingLong(Flat::getArea));

        if (minFlat.isEmpty() || newFlat.getArea() < minFlat.get().getArea()) {
            newFlat.setId(collectionManager.generateNewId());
            newFlat.setCreationDate(java.time.LocalDateTime.now());
            collectionManager.addFlat(newFlat);
            return "Элемент добавлен (ID: " + newFlat.getId() + ")";
        }
        return "Элемент не добавлен - его площадь не меньше минимальной в коллекции";
    }

    @Override
    public String getDescription() {
        return "добавить элемент, если его площадь меньше минимальной в коллекции";
    }
}