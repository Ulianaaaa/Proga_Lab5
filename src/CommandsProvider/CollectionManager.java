package CommandsProvider;

import java.io.*;
import java.util.Iterator;
import java.util.TreeSet;

public class CollectionManager {
    private TreeSet<Flat> flats; // Хранит коллекцию объектов типа Flat
    private final java.util.Date initializationDate; // Дата создания коллекции

    public CollectionManager(TreeSet<Flat> flats) {
        this.flats = new TreeSet<>(flats);
        this.initializationDate = new java.util.Date();
    }

    public TreeSet<Flat> getFlats() {
        return new TreeSet<>(flats);
    }

    public java.util.Date getInitializationDate() {
        return initializationDate;
    }

    public void addFlat(Flat flat) {
        flats.add(flat);
    }

    public void clearFlats() {
        flats.clear();
    }

    // Добавление метода для сохранения коллекции в файл
    public void saveToFile() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("flats_collection.dat"))) {
            oos.writeObject(flats); // Сохраняем коллекцию flats в файл
        }
    }

    // Метод для удаления элемента по его id
    public boolean removeById(int id) {
        Iterator<Flat> iterator = flats.iterator();
        while (iterator.hasNext()) {
            Flat flat = iterator.next();
            if (flat.getId() == id) {
                iterator.remove();
                return true; // Элемент удален
            }
        }
        return false; // Элемент не найден
    }
}