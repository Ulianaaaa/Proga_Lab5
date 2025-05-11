package common.commands;

import client.FlatBuilder;
import common.models.Flat;
import server.CollectionManager;

import java.util.Scanner;

/**
 * Команда для добавления нового элемента в коллекцию
 */
public class Add implements Command {

    private final CollectionManager collectionManager;
    private final FlatBuilder flatBuilder;

    /**
     * Конструктор команды Add
     * @param collectionManager менеджер коллекции
     */
    public Add(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
        this.flatBuilder = new FlatBuilder(new Scanner(System.in), collectionManager);
    }

    /**
     * Выполняет команду добавления нового элемента
     */
    @Override
    public String execute(Object args) {
        try {
            System.out.println("\n=== Добавление новой квартиры ===");
            Flat newFlat = flatBuilder.buildFlat();
            collectionManager.addFlat(newFlat);
            return "Новый объект Flat успешно добавлен в коллекцию. ID: " + newFlat.getId();
        } catch (Exception e) {
            return "Ошибка при добавлении элемента: " + e.getMessage();
        }
    }

    /**
     * Возвращает описание команды
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return " добавить новый объект Flat в коллекцию";
    }
}