package common.commands;

import common.models.Flat;
import server.CollectionManager;

import java.time.LocalDateTime;

/**
 * Команда для добавления нового элемента в коллекцию
 */
public class Add implements Command {

    private final CollectionManager collectionManager;

    public Add(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object args) {
        if (!(args instanceof Flat)) {
            return "Ошибка: объект Flat не передан.";
        }

        Flat flat = (Flat) args;
        flat.setId(generateUniqueId());
        flat.setCreationDate(LocalDateTime.now());

        collectionManager.addFlat(flat);
        return "Квартира успешно добавлена в коллекцию. ID: " + flat.getId();
    }

    @Override
    public String getDescription() {
        return "добавить новый объект Flat в коллекцию";
    }

    private int generateUniqueId() {
        return collectionManager.getFlats().stream()
                .mapToInt(Flat::getId)
                .max()
                .orElse(0) + 1;
    }
}