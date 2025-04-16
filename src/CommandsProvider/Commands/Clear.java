package CommandsProvider.Commands;

import CommandsProvider.Command;
import CommandsProvider.CollectionManager;

/**
 * Команда clear : очистить коллекцию.
 */
public class Clear implements Command {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute() {
        collectionManager.clearFlats();
        System.out.println("Коллекция очищена.");
    }

    @Override
    public String getDescription() {
        return " очистить коллекцию";
    }
}