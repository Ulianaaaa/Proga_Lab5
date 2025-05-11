package common.commands;

import server.CollectionManager;

/**
 * Команда clear : очистить коллекцию.
 */
public class Clear implements Command {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object args) {
        collectionManager.clearFlats();
        return "Коллекция очищена.";
    }

    @Override
    public String getDescription() {
        return " очистить коллекцию";
    }
}