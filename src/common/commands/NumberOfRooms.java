package common.commands;

import server.CollectionManager;

/**
 * Команда count_greater_than_number_of_rooms : подсчитать количество элементов с количеством комнат больше заданного значения.
 */
public class NumberOfRooms implements Command {
    private final CollectionManager collectionManager;

    public NumberOfRooms(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        if (!(argument instanceof String)) {
            return "Ошибка: аргумент должен быть строкой, содержащей число.";
        }

        try {
            long numberOfRooms = Long.parseLong(((String) argument).trim());

            long count = collectionManager.getFlats().stream()
                    .filter(flat -> flat.getNumberOfRooms() > numberOfRooms)
                    .count();

            return "Количество квартир с количеством комнат больше " + numberOfRooms + ": " + count;
        } catch (NumberFormatException e) {
            return "Ошибка: количество комнат должно быть числом.";
        }
    }

    @Override
    public String getDescription() {
        return "подсчитать количество элементов с количеством комнат больше заданного значения";
    }
}