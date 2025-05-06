package CommandsProvider.Commands;

import CommandsProvider.Command;
import CommandsProvider.CollectionManager;
import Data.DataProvider;

/**
 * Команда save : сохранить коллекцию в файл.
 */
public class Save implements Command {
    private final CollectionManager collectionManager;
    private final DataProvider dataProvider;
    private final String fileName;

    public Save(CollectionManager collectionManager, DataProvider dataProvider, String fileName) {
        this.collectionManager = collectionManager;
        this.dataProvider = dataProvider;
        this.fileName = fileName;
    }

    @Override
    public void execute(String args) {
        dataProvider.save(collectionManager.getFlats(), fileName);
    }

    @Override
    public String getDescription() {
        return " сохранить коллекцию в файл";
    }
}