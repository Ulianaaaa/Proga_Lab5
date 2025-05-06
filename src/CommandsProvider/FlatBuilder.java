package CommandsProvider;

import java.util.Scanner;

public class FlatBuilder {
    private final Scanner scanner;
    private final CollectionManager collectionManager;

    // Конструктор теперь принимает и CollectionManager для проверки уникальности ID
    public FlatBuilder(Scanner scanner, CollectionManager collectionManager) {
        this.scanner = scanner;
        this.collectionManager = collectionManager;
    }

    // Метод для создания объекта Flat
    public Flat buildFlat() {
        System.out.println("Введите название квартиры:");
        String name = readNonEmptyString();

        System.out.println("Введите координату х: (она должно быть больше -349)");
        double x = readDoubleGreaterThan("-349");
        Long y = readLong("Введите значение y (не может быть null):");
        Coordinates coordinates = new Coordinates(x, y);

        System.out.println("Введите площадь квартиры (должна быть больше 0):");
        Long area = readLongGreaterThan(0);

        System.out.println("Введите количество комнат (должно быть больше 0 и меньше 9):");
        long numberOfRooms = readLongInRange(1, 8);

        System.out.println("Выберите вариант отделки (DESIGNER, NONE, BAD, LITTLE):");
        Furnish furnish = readEnum(Furnish.class);

        System.out.println("Выберите вид из окна (STREET, YARD, NORMAL):");
        View view = readEnum(View.class);

        System.out.println("Введите транспорт (FEW, NONE, ENOUGH) или оставьте поле пустым:");
        Transport transport = readOptionalEnum(Transport.class);

        System.out.println("Введите название дома (можно оставить пустым):");
        String houseName = scanner.nextLine();
        House house = null;
        if (!houseName.isEmpty()) {
            System.out.println("Введите год постройки дома (должен быть больше 0 и меньше 197):");
            Long houseYear = readLongInRange(1, 197);
            System.out.println("Введите количество квартир на этаже (должно быть больше 0):");
            long flatsOnFloor = readLongGreaterThan(0);
            house = new House(houseName, houseYear, flatsOnFloor);
        }

        int id = generateUniqueId();
        java.util.Date creationDate = new java.util.Date();

        return new Flat(id, name, coordinates, creationDate, area, numberOfRooms, furnish, view, transport, house);
    }

    // Остальные методы остаются без изменений
    public String readNonEmptyString() {
        String input;
        while (true) {
            input = scanner.nextLine();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Поле не может быть пустым. Попробуйте снова:");
            }
        }
    }

    public double readDoubleGreaterThan(String minValue) {
        double input;
        while (true) {
            try {
                input = Double.parseDouble(scanner.nextLine());
                if (input > Double.parseDouble(minValue)) {
                    return input;
                } else {
                    System.out.println("Значение должно быть больше " + minValue + ". Попробуйте снова:");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка ввода. Попробуйте снова:");
            }
        }
    }

    public Long readLong(String prompt) {
        Long input = null;
        while (input == null) {
            System.out.println(prompt);
            try {
                input = Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка ввода. Попробуйте снова:");
            }
        }
        return input;
    }

    public Long readLongGreaterThan(long minValue) {
        long input;
        while (true) {
            try {
                input = Long.parseLong(scanner.nextLine());
                if (input > minValue) {
                    return input;
                } else {
                    System.out.println("Значение должно быть больше " + minValue + ". Попробуйте снова:");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка ввода. Попробуйте снова:");
            }
        }
    }

    public long readLongInRange(long minValue, long maxValue) {
        long input;
        while (true) {
            try {
                input = Long.parseLong(scanner.nextLine());
                if (input >= minValue && input <= maxValue) {
                    return input;
                } else {
                    System.out.println("Значение должно быть в диапазоне от " + minValue + " до " + maxValue + ". Попробуйте снова:");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка ввода. Попробуйте снова:");
            }
        }
    }

    public  <T extends Enum<T>> T readEnum(Class<T> enumClass) {
        T result = null;
        while (result == null) {
            String input = scanner.nextLine().toUpperCase();
            try {
                result = Enum.valueOf(enumClass, input);
            } catch (IllegalArgumentException e) {
                System.out.println("Неверное значение. Пожалуйста, введите одно из допустимых значений:");
                for (T enumConstant : enumClass.getEnumConstants()) {
                    System.out.println(enumConstant.name());
                }
            }
        }
        return result;
    }

    public  <T extends Enum<T>> T readOptionalEnum(Class<T> enumClass) {
        T result = null;
        String input = scanner.nextLine().toUpperCase();
        if (!input.isEmpty()) {
            try {
                result = Enum.valueOf(enumClass, input);
            } catch (IllegalArgumentException e) {
                System.out.println("Неверное значение для Transport. Попробуйте снова.");
            }
        }
        return result;
    }

    // Новая реализация generateUniqueId
    private int generateUniqueId() {
        if (collectionManager.getFlats().isEmpty()) {
            return 1;
        }

        // Находим максимальный ID в коллекции
        int maxId = collectionManager.getFlats()
                .stream()
                .mapToInt(Flat::getId)
                .max()
                .orElse(0);

        return maxId + 1;
    }
}