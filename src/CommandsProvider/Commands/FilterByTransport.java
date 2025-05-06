package CommandsProvider.Commands;

import CommandsProvider.*;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Команда filter_by_transport : фильтровать элементы по полю transport.
 */
public class FilterByTransport implements Command {
    private final CollectionManager collectionManager;
    private final Scanner scanner;

    public FilterByTransport(CollectionManager collectionManager, Scanner scanner) {
        this.collectionManager = collectionManager;
        this.scanner = scanner;
    }

    @Override
    public void execute(String args) {
        // Запрашиваем у пользователя тип транспорта
        System.out.print("Введите тип транспорта (FEW, NONE, ENOUGH): ");
        String transportInput = scanner.nextLine().trim();

        // Проверяем, не пустое ли введенное значение
        if (transportInput.isEmpty()) {
            System.out.println("Ошибка: поле не может быть пустым.");
            return;
        }

        try {
            // Преобразуем введенное значение в перечисление Transport
            Transport transport = Transport.valueOf(transportInput.toUpperCase());

            // Фильтруем объекты Flat по выбранному типу транспорта
            List<Flat> filteredFlats = collectionManager.getFlats().stream()
                    .filter(flat -> transport.equals(flat.getTransport())) // фильтрация по transport
                    .collect(Collectors.toList());

            // Выводим отфильтрованные объекты
            if (filteredFlats.isEmpty()) {
                System.out.println("Нет квартир с указанным типом транспорта.");
            } else {
                System.out.println("Квартиры с типом транспорта " + transport + ":");
                filteredFlats.forEach(System.out::println);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: введен неправильный тип транспорта. Возможные значения: FEW, NONE, ENOUGH.");
        }
    }

    @Override
    public String getDescription() {
        return " фильтровать элементы по полю transport";
    }
}