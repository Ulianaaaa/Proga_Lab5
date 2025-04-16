package CommandsProvider;

import CommandsProvider.Commands.*;
import Data.DataProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CommandManager {

    private final Map<String, Command> commands = new HashMap<>();
    //коллекция, где ключ - это стринг с названием команды, а значение - объект класса реализующий интерфейс команд
    //new HashMap<>() оно создает пустой хэшмэп котооый нужен для хранения команд
    private final CollectionManager collectionManager; //это ссылка на объект который управляет коллекцией объектов flat

    public CommandManager(CollectionManager collectionManager, DataProvider dataProvider, String fileName) {
        this.collectionManager = collectionManager;
        registerCommands(dataProvider, fileName); //метод отвечающий за регистрацию всех доступных команд в хэшмэп
    }

    private void registerCommands(DataProvider dataProvider, String fileName) {
        commands.put("help", new Help(this));//доабвляет команду в хэшмэп с ключом хэлп и значением - объектом класса хэлп
        commands.put("info", new Info(collectionManager));
        commands.put("exit", new Exit());
        commands.put("show", new Show(collectionManager));
        commands.put("add", new Add(collectionManager));
        commands.put("clear", new Clear(collectionManager));
        commands.put("save", new Save(collectionManager, dataProvider, fileName));
        commands.put("removebyid", new RemoveById(collectionManager, new Scanner(System.in)));
        commands.put("updatebyid", new UpdateById(collectionManager, new Scanner(System.in)));
        commands.put("executescript", new ExecuteScript(this, collectionManager, new Scanner(System.in)));
        commands.put("addifmax", new AddIfMax(collectionManager, new Scanner(System.in)));
        commands.put("addifmin", new AddIfMin(collectionManager, new Scanner(System.in)));
        commands.put("removelower", new RemoveLower(collectionManager, new Scanner(System.in)));
        commands.put("minbyfurnish", new MinByFurnish(collectionManager));
        commands.put("numberofrooms", new NumberOfRooms(collectionManager, new Scanner(System.in)));
        commands.put("filterbytransport", new FilterByTransport(collectionManager, new Scanner(System.in)));
    }

    public void executeCommand(String commandName) { //принимает строку commandName которая содержит название команды, введенной пользователем
        Command command = commands.get(commandName); //получает объект комманд из хэшмэпа по ключу команднэйм
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Неизвестная команда. Введите 'help' для просмотра списка команд.");
        }
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}