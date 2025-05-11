package common.commands;

import server.CollectionManager;

/**
 * Команда remove_by_id : удалить элемент из коллекции по его id.
 */
public class RemoveById implements Command {
    private final CollectionManager collectionManager;

    public RemoveById(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        if (!(argument instanceof String) || ((String) argument).trim().isEmpty()) {
            return "Ошибка: необходимо указать id. Пример: remove_by_id 3";
        }

        try {
            int id = Integer.parseInt(((String) argument).trim());
            boolean removed = collectionManager.removeById(id);
            if (removed) {
                return "Элемент с id " + id + " удалён.";
            } else {
                return "Элемент с таким id не найден.";
            }
        } catch (NumberFormatException e) {
            return "Ошибка: id должен быть числом.";
        }
    }

    @Override
    public String getDescription() {
        return "удалить элемент из коллекции по его id";
    }
}