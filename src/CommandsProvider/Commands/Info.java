package CommandsProvider.Commands;

import CommandsProvider.CollectionManager;
import CommandsProvider.Command;
import java.util.Date;
import java.util.TreeSet;

public class Info implements Command {

    private final CollectionManager collectionManager;

    public Info(CollectionManager collectionManager) {
        this.collectionManager = collectionManager; //также конструктор, который принимает СМ для работы с колекцией
        //и переданный объект сохранянется в переменную collectionManager
    }

    @Override
    public void execute() {
        System.out.println("Информация о коллекции:");
        System.out.println("Тип: " + collectionManager.getFlats().getClass().getName());
        //getFlats возвращает коллекцию объектов Flat которая хранится в CollectionManager
        //getClass возвращает класс коллекции (те TreeSet)
        //getName получает полное имя класса в формате java.util.TreeSet
        System.out.println("Дата инициализации: " + collectionManager.getInitializationDate());
        //getInitializationDate метод который возвращает дату и время, когда коллекция была создана
        System.out.println("Количество элементов: " + collectionManager.getFlats().size());
        //в начала возвращет коллекцию объектов Flat а потом возвращает количество элементов в коллекции с помощью size
    }

    @Override
    public String getDescription() {
        return " вывести информацию о коллекции";
    }
}