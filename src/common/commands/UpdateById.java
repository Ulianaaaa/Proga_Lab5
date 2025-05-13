package common.commands;

import common.models.Flat;
import server.CollectionManager;

public class UpdateById implements Command {
    private final CollectionManager collectionManager;

    public UpdateById(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        // === Шаг 1: Проверка ID (если передан только Integer) ===
        if (argument instanceof Integer idOnly) {
            boolean exists = collectionManager.getFlats().stream()
                    .anyMatch(f -> f.getId() == idOnly);
            return exists
                    ? "OK"
                    : "Ошибка: квартира с ID " + idOnly + " не найдена.";
        }

        // === Шаг 2: Обновление по [id, Flat] ===
        if (!(argument instanceof Object[] args) || args.length != 2) {
            return "Ошибка: ожидается массив [id, Flat]";
        }

        if (!(args[0] instanceof Integer) || !(args[1] instanceof Flat)) {
            return "Ошибка: неверный формат аргументов. Ожидается: update id {element}";
        }

        int id = (int) args[0];
        Flat updatedFlat = (Flat) args[1];

        Flat existingFlat = collectionManager.getFlats().stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElse(null);

        if (existingFlat == null) {
            return "Ошибка: квартира с ID " + id + " не найдена.";
        }

        // Обновление полей
        existingFlat.setName(updatedFlat.getName());
        existingFlat.setCoordinates(updatedFlat.getCoordinates());
        existingFlat.setArea(updatedFlat.getArea());
        existingFlat.setNumberOfRooms(updatedFlat.getNumberOfRooms());
        existingFlat.setFurnish(updatedFlat.getFurnish());
        existingFlat.setView(updatedFlat.getView());
        existingFlat.setTransport(updatedFlat.getTransport());
        existingFlat.setHouse(updatedFlat.getHouse());

        return "Квартира с ID " + id + " успешно обновлена.";
    }

    @Override
    public String getDescription() {
        return "обновить элемент в коллекции по его id";
    }
}