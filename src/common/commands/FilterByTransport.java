package common.commands;

import common.models.Flat;
import common.models.Transport;
import server.CollectionManager;

import java.util.List;
import java.util.stream.Collectors;

public class FilterByTransport implements Command {
    private final CollectionManager collectionManager;

    public FilterByTransport(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        if (argument == null || !(argument instanceof String)) {
            return "Ошибка: необходимо передать строковой аргумент типа транспорта (FEW, NONE, ENOUGH).";
        }

        String transportInput = ((String) argument).trim().toUpperCase();

        if (transportInput.isEmpty()) {
            return "Ошибка: поле не может быть пустым.";
        }

        try {
            Transport transport = Transport.valueOf(transportInput);

            List<Flat> filteredFlats = collectionManager.getFlats().stream()
                    .filter(flat -> transport.equals(flat.getTransport()))
                    .collect(Collectors.toList());

            if (filteredFlats.isEmpty()) {
                return "Нет квартир с указанным типом транспорта.";
            } else {
                StringBuilder result = new StringBuilder("Квартиры с типом транспорта " + transport + ":\n");
                filteredFlats.forEach(flat -> result.append(flat).append("\n"));
                return result.toString();
            }
        } catch (IllegalArgumentException e) {
            return "Ошибка: введен неправильный тип транспорта. Возможные значения: FEW, NONE, ENOUGH.";
        }
    }

    @Override
    public String getDescription() {
        return "фильтровать элементы по полю transport";
    }
}