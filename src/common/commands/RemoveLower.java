package common.commands;

import common.models.Flat;
import server.CollectionManager;

public class RemoveLower implements Command {
    private final CollectionManager collectionManager;

    public RemoveLower(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        if (!(argument instanceof Flat)) {
            return "Ошибка: ожидается объект Flat";
        }

        Flat referenceFlat = (Flat) argument;
        int initialSize = collectionManager.getFlats().size();

        // Удаляем элементы с площадью меньше заданной
        collectionManager.getFlats().removeIf(flat ->
                flat.getArea() < referenceFlat.getArea()
        );

        int removedCount = initialSize - collectionManager.getFlats().size();
        return removedCount > 0
                ? "Удалено " + removedCount + " элемент(ов) с площадью меньше " + referenceFlat.getArea()
                : "Нет элементов с площадью меньше " + referenceFlat.getArea();
    }

    @Override
    public String getDescription() {
        return "удалить все элементы из коллекции, меньшие по площади, чем заданный элемент";
    }
}