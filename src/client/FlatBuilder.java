package client;

import common.models.*;

import java.util.Scanner;

public class FlatBuilder {
    private final Scanner scanner;

    public FlatBuilder(Scanner scanner) {
        this.scanner = scanner;
    }

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

        // ID и creationDate установит сервер
        return new Flat(0, name, coordinates, null, area, numberOfRooms, furnish, view, transport, house);
    }

    // Методы чтения ввода — без изменений
    public String readNonEmptyString() {
        while (true) {
            String input = scanner.nextLine();
            if (!input.isEmpty()) return input;
            System.out.println("Поле не может быть пустым. Попробуйте снова:");
        }
    }

    public double readDoubleGreaterThan(String minValue) {
        while (true) {
            try {
                double input = Double.parseDouble(scanner.nextLine());
                if (input > Double.parseDouble(minValue)) return input;
                System.out.println("Значение должно быть больше " + minValue + ". Попробуйте снова:");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка ввода. Попробуйте снова:");
            }
        }
    }

    public Long readLong(String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка ввода. Попробуйте снова:");
            }
        }
    }

    public Long readLongGreaterThan(long minValue) {
        while (true) {
            try {
                long input = Long.parseLong(scanner.nextLine());
                if (input > minValue) return input;
                System.out.println("Значение должно быть больше " + minValue + ". Попробуйте снова:");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка ввода. Попробуйте снова:");
            }
        }
    }

    public long readLongInRange(long minValue, long maxValue) {
        while (true) {
            try {
                long input = Long.parseLong(scanner.nextLine());
                if (input >= minValue && input <= maxValue) return input;
                System.out.println("Значение должно быть в диапазоне от " + minValue + " до " + maxValue + ". Попробуйте снова:");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка ввода.Попробуйте снова:");
            }
        }
    }

    public <T extends Enum<T>> T readEnum(Class<T> enumClass) {
        while (true) {
            String input = scanner.nextLine().toUpperCase();
            try {
                return Enum.valueOf(enumClass, input);
            } catch (IllegalArgumentException e) {
                System.out.println("Неверное значение. Возможные значения:");
                for (T value : enumClass.getEnumConstants()) {
                    System.out.println("- " + value.name());
                }
            }
        }
    }

    public <T extends Enum<T>> T readOptionalEnum(Class<T> enumClass) {
        String input = scanner.nextLine().toUpperCase();
        if (input.isEmpty()) return null;
        try {
            return Enum.valueOf(enumClass, input);
        } catch (IllegalArgumentException e) {
            System.out.println("Неверное значение. Возвращаю null.");
            return null;
        }
    }
}