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
        if (argument == null || argument.toString().trim().isEmpty()) {
            return "Ошибка: необходимо указать тип транспорта (FEW, NONE, ENOUGH)";
        }

        String transportInput = argument.toString().trim().toUpperCase();

        try {
            Transport transport = Transport.valueOf(transportInput);

            List<Flat> filteredFlats = collectionManager.getFlats().stream()
                    .filter(flat -> flat.getTransport() == transport)
                    .collect(Collectors.toList());

            if (filteredFlats.isEmpty()) {
                return "Нет квартир с указанным типом транспорта: " + transport;
            }

            StringBuilder result = new StringBuilder();
            result.append("Найдено ").append(filteredFlats.size())
                    .append(" квартир с транспортом ").append(transport).append(":\n");

            filteredFlats.forEach(flat -> result.append(flat).append("\n"));
            return result.toString();

        } catch (IllegalArgumentException e) {
            return "Ошибка: '" + transportInput + "' не является допустимым типом транспорта. " +
                    "Допустимые значения: FEW, NONE, ENOUGH";
        }
    }

    @Override
    public String getDescription() {
        return "filter_by_transport {FEW|NONE|ENOUGH} : вывести элементы с указанным транспортом";
    }
}