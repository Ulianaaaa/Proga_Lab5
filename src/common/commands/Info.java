package common.commands;

import server.CollectionManager;

public class Info implements Command {

    private final CollectionManager collectionManager;

    public Info(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        StringBuilder info = new StringBuilder();
        info.append("Информация о коллекции:\n");
        info.append("Тип: ").append(collectionManager.getFlats().getClass().getName()).append("\n");
        info.append("Дата инициализации: ").append(collectionManager.getInitializationDate()).append("\n");
        info.append("Количество элементов: ").append(collectionManager.getFlats().size());
        return info.toString();
    }

    @Override
    public String getDescription() {
        return "вывести информацию о коллекции";
    }
}