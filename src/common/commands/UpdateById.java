package common.commands;

import client.FlatBuilder;
import common.models.*;
import server.CollectionManager;

import java.util.Scanner;

/**
 * Команда для обновления элемента коллекции по его id.
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
    public String execute(Object argument) {
        if (!(argument instanceof String) || ((String) argument).trim().isEmpty()) {
            return "Ошибка: необходимо указать id. Пример: update 4";
        }

        try {
            int id = Integer.parseInt(((String) argument).trim());
            Flat flatToUpdate = collectionManager.getFlats().stream()
                    .filter(f -> f.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (flatToUpdate == null) {
                return "Элемент с таким id не найден.";
            }

            StringBuilder response = new StringBuilder("Элемент с id " + id + " найден. Введите новые значения:\n");

            response.append("Введите новое название квартиры:\n");
            flatToUpdate.setName(flatBuilder.readNonEmptyString());

            response.append("Введите координату x (она должна быть больше -349):\n");
            double x = flatBuilder.readDoubleGreaterThan("-349");

            Long y = flatBuilder.readLong("Введите значение y (не может быть null):");
            flatToUpdate.setCoordinates(new Coordinates(x, y));

            response.append("Введите площадь квартиры (должна быть больше 0):\n");
            flatToUpdate.setArea(flatBuilder.readLongGreaterThan(0));

            response.append("Введите количество комнат (должно быть больше 0 и меньше 9):\n");
            flatToUpdate.setNumberOfRooms(flatBuilder.readLongInRange(1, 8));

            response.append("Выберите вариант отделки (DESIGNER, NONE, BAD, LITTLE):\n");
            flatToUpdate.setFurnish(flatBuilder.readEnum(Furnish.class));

            response.append("Выберите вид из окна (STREET, YARD, NORMAL):\n");
            flatToUpdate.setView(flatBuilder.readEnum(View.class));

            response.append("Введите транспорт (FEW, NONE, ENOUGH) или оставьте поле пустым:\n");
            flatToUpdate.setTransport(flatBuilder.readOptionalEnum(Transport.class));

            response.append("Введите название дома (можно оставить пустым):\n");
            String houseName = scanner.nextLine().trim();
            if (!houseName.isEmpty()) {
                response.append("Введите год постройки дома (должен быть больше 0 и меньше 197):\n");
                Long year = flatBuilder.readLongInRange(1, 197);

                response.append("Введите количество квартир на этаже:\n");
                long flatsOnFloor = flatBuilder.readLongGreaterThan(0);

                flatToUpdate.setHouse(new House(houseName, year, flatsOnFloor));
            } else {
                flatToUpdate.setHouse(null);
            }

            response.append("Элемент успешно обновлён.");
            return response.toString();

        } catch (NumberFormatException e) {
            return "Ошибка: id должен быть числом.";
        } catch (Exception e) {
            return "Ошибка обновления: " + e.getMessage();
        }
    }

    @Override
    public String getDescription() {
        return "обновить элемент в коллекции по его id";
    }
}