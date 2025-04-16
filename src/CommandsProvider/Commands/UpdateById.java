package CommandsProvider.Commands;

import CommandsProvider.Command;
import CommandsProvider.CollectionManager;
import CommandsProvider.*;
import java.util.Scanner;

/**
 * Команда для обновления элемента коллекции по его id
 */
public class UpdateById implements Command {
    private final CollectionManager collectionManager;
    private final Scanner scanner;

    /**
     * Конструктор команды UpdateById
     * @param collectionManager менеджер коллекции
     * @param scanner сканер для ввода данных
     */
    public UpdateById(CollectionManager collectionManager, Scanner scanner) {
        this.collectionManager = collectionManager;
        this.scanner = scanner;
    }

    /**
     * Выполняет обновление элемента по id
     */
    @Override
    public void execute() {
        System.out.print("Введите id элемента для обновления: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Flat flatToUpdate = null;

            // Поиск элемента по id
            for (Flat flat : collectionManager.getFlats()) {
                if (flat.getId() == id) {
                    flatToUpdate = flat;
                    break;
                }
            }

            if (flatToUpdate != null) {
                System.out.println("Элемент с id " + id + " найден. Обновите его данные.");

                // Обновление всех полей
                System.out.print("Введите новое имя квартиры: ");
                flatToUpdate.setName(scanner.nextLine());

                System.out.print("Введите координату x (должна быть > -349): ");
                double x = Double.parseDouble(scanner.nextLine());
                System.out.print("Введите координату y: ");
                long y = Long.parseLong(scanner.nextLine());
                flatToUpdate.setCoordinates(new Coordinates(x, y));

                System.out.print("Введите новую площадь квартиры (должна быть > 0): ");
                flatToUpdate.setArea(Long.parseLong(scanner.nextLine()));

                System.out.print("Введите новое количество комнат (1-8): ");
                flatToUpdate.setNumberOfRooms(Long.parseLong(scanner.nextLine()));

                System.out.println("Выберите вариант отделки (DESIGNER, NONE, BAD, LITTLE): ");
                flatToUpdate.setFurnish(Furnish.valueOf(scanner.nextLine().toUpperCase()));

                System.out.println("Выберите вид из окна (STREET, YARD, NORMAL): ");
                flatToUpdate.setView(View.valueOf(scanner.nextLine().toUpperCase()));

                System.out.println("Введите транспорт (FEW, NONE, ENOUGH) или оставьте пустым: ");
                String transportInput = scanner.nextLine();
                flatToUpdate.setTransport(transportInput.isEmpty() ? null :
                        Transport.valueOf(transportInput.toUpperCase()));

                System.out.println("Введите название дома (оставьте пустым, если нет): ");
                String houseName = scanner.nextLine();
                if (!houseName.isEmpty()) {
                    System.out.println("Введите год постройки дома (1-197): ");
                    Long year = Long.parseLong(scanner.nextLine());
                    System.out.println("Введите количество квартир на этаже (>0): ");
                    long flatsOnFloor = Long.parseLong(scanner.nextLine());
                    flatToUpdate.setHouse(new House(houseName, year, flatsOnFloor));
                } else {
                    flatToUpdate.setHouse(null);
                }

                System.out.println("Элемент с id " + id + " был обновлён.");
            } else {
                System.out.println("Элемент с таким id не найден.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введены неверные данные.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Возвращает описание команды
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return " обновить элемент в коллекции по его id";
    }
}