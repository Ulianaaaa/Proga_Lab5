package CommandsProvider.Commands;

import CommandsProvider.*;

import java.util.Scanner;

/**
 * Команда для обновления элемента коллекции по его id
 */
public class UpdateById implements Command {
    private final CollectionManager collectionManager;
    private final Scanner scanner;
    private final FlatBuilder flatBuilder;

    public UpdateById(CollectionManager collectionManager, Scanner scanner) {
        this.collectionManager = collectionManager;
        this.scanner = scanner;
        this.flatBuilder = new FlatBuilder(scanner, collectionManager);
    }

    @Override
    public void execute(String args) {
        if (args == null || args.trim().isEmpty()) {
            System.out.println("Ошибка: необходимо указать id. Пример: update 4");
            return;
        }

        try {
            int id = Integer.parseInt(args.trim()); // теперь мы используем args!
            Flat flatToUpdate = collectionManager.getFlats().stream()
                    .filter(f -> f.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (flatToUpdate != null) {
                System.out.println("Элемент с id " + id + " найден. Введите новые значения:");

                System.out.println("Введите новое название квартиры:");
                flatToUpdate.setName(flatBuilder.readNonEmptyString());

                System.out.println("Введите координату x (она должно быть больше -349):");
                double x = flatBuilder.readDoubleGreaterThan("-349");

                Long y = flatBuilder.readLong("Введите значение y (не может быть null):");
                flatToUpdate.setCoordinates(new Coordinates(x, y));

                System.out.println("Введите площадь квартиры (должна быть больше 0):");
                flatToUpdate.setArea(flatBuilder.readLongGreaterThan(0));

                System.out.println("Введите количество комнат (должно быть больше 0 и меньше 9):");
                flatToUpdate.setNumberOfRooms(flatBuilder.readLongInRange(1, 8));

                System.out.println("Выберите вариант отделки (DESIGNER, NONE, BAD, LITTLE):");
                flatToUpdate.setFurnish(flatBuilder.readEnum(Furnish.class));

                System.out.println("Выберите вид из окна (STREET, YARD, NORMAL):");
                flatToUpdate.setView(flatBuilder.readEnum(View.class));

                System.out.println("Введите транспорт (FEW, NONE, ENOUGH) или оставьте поле пустым:");
                flatToUpdate.setTransport(flatBuilder.readOptionalEnum(Transport.class));

                System.out.println("Введите название дома (можно оставить пустым):");
                String houseName = scanner.nextLine();
                if (!houseName.isEmpty()) {
                    System.out.println("Введите год постройки дома (должен быть больше 0 и меньше 197):");
                    Long year = flatBuilder.readLongInRange(1, 197);
                    System.out.println("Введите количество квартир на этаже: ");
                    long flatsOnFloor = flatBuilder.readLongGreaterThan(0);
                    flatToUpdate.setHouse(new House(houseName, year, flatsOnFloor));
                } else {
                    flatToUpdate.setHouse(null);
                }

                System.out.println("Элемент успешно обновлён.");
            } else {
                System.out.println("Элемент с таким id не найден.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: id должен быть числом.");
        }
    }

    @Override
    public String getDescription() {
        return " обновить элемент в коллекции по его id";
    }
}