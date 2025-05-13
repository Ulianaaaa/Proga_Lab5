package common.commands;

import common.models.Flat;
import server.CollectionManager;

import java.util.Comparator;
import java.util.Optional;

public class AddIfMax implements Command {
    private final CollectionManager collectionManager;

    public AddIfMax(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        if (!(argument instanceof Flat)) {
            return "Ошибка: ожидается объект Flat";
        }

        Flat newFlat = (Flat) argument;
        Optional<Flat> maxFlat = collectionManager.getFlats().stream()
                .max(Comparator.comparingLong(Flat::getArea));

        if (maxFlat.isEmpty() || newFlat.getArea() > maxFlat.get().getArea()) {
            newFlat.setId(collectionManager.generateNewId());
            newFlat.setCreationDate(java.time.LocalDateTime.now());
            collectionManager.addFlat(newFlat);
            return "Элемент добавлен (ID: " + newFlat.getId() + ")";
        }
        return "Элемент не добавлен - его площадь не больше максимальной в коллекции";
    }

    @Override
    public String getDescription() {
        return "добавить элемент, если его площадь больше максимальной в коллекции";
    }
}