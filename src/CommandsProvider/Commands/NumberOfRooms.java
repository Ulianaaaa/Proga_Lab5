package CommandsProvider.Commands;
import CommandsProvider.*;

import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Команда count_greater_than_number_of_rooms : подсчитать количество элементов с количеством комнат больше заданного значения.
 */

public class NumberOfRooms  implements Command {
    private final CollectionManager collectionManager;
    private final Scanner scanner;
    public NumberOfRooms(CollectionManager collectionManager, Scanner scanner) {
        this.collectionManager = collectionManager;
        this.scanner = scanner;
    }

    @Override
    public void execute(String args) {
        // Запрашиваем количество комнат
        System.out.print("Введите количество комнат: ");
        long numberOfRooms;
        try {
            numberOfRooms = Long.parseLong(scanner.nextLine().trim());

            // Подсчитываем количество элементов, у которых количество комнат больше указанного
            long count = collectionManager.getFlats().stream()
                    .filter(flat -> flat.getNumberOfRooms() > numberOfRooms)
                    .count();

            System.out.println("Количество квартир с количеством комнат больше " + numberOfRooms + ": " + count);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: количество комнат должно быть числом.");
        }
    }

    @Override
    public String getDescription() {
        return " подсчитать количество элементов с количеством комнат больше заданного значения";
    }
}