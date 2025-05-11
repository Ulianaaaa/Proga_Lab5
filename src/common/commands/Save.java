package common.commands;

import server.CollectionManager;
import server.DataProvider;

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
    public String execute(Object argument) {
        try {
            dataProvider.save(collectionManager.getFlats(), fileName);
            return "Коллекция успешно сохранена в файл.";
        } catch (Exception e) {
            return "Ошибка при сохранении коллекции: " + e.getMessage();
        }
    }

    @Override
    public String getDescription() {
        return "сохранить коллекцию в файл";
    }
}