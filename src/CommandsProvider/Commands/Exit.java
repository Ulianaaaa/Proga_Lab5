package CommandsProvider.Commands;

import CommandsProvider.Command;

public class Exit implements Command {
    @Override
    public void execute(String args) { //execute выполняет логику выхода из программы
        System.out.println("Завершение работы программы");
        System.exit(0); //0- типо ошибок нет

    }

    @Override
    public String getDescription() { //возвращает описание команды
        return " завершить программу без сохранения";
    }
}
