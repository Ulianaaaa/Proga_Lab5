package CommandsProvider.Commands;

import CommandsProvider.Command;
import CommandsProvider.CommandManager;

import java.util.Map;

public class Help implements Command {
    private final CommandManager commandManager; //это чтобы help мог получить список всех доступных команд
    public Help(CommandManager commandManager){ //конструктор вызывается при создании объекта help и
        //CommandManager commandManager принимаеи объект СМ для доступа к списку команд
        this.commandManager = commandManager; //this.commandManager обращаемся к полю сМ текущего объекта Help
        // c commandManager присваиваем переданный в конструктор объект
    }

    @Override
    public void execute() {
        System.out.println("Доступные команды");
        for (Map.Entry<String, Command> entry : commandManager.getCommands().entrySet()){
            // через for перебираем элементы коллекции
            //Map.Entry<String, Command> - это вложенный интерфейс Map.Entry,ипредставляющий пару: ключ - значение
            //string - название команды, а command - объект команды
            // в entry переменаая в которой хранится текущая пара "ключ-значение" во время каждой итерации
            //commandManager.getCommands() возвращает map со всеми командами
            //entrySet - преобразует мэп в набор сет вче пар ключ-значение для удобного перебора
            System.out.println(entry.getKey() + ":" + entry.getValue().getDescription());
            //entry.getKey() - получаем название команды (стринг)
            //entry.getValue() - получаем объект команды (команд)


        }
    }

    @Override
    public String getDescription() {
        return " вывести справку по доступным командам";
    }
}
