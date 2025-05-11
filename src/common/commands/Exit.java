package common.commands;

public class Exit implements Command {
    @Override
    public String execute(Object argument) {
        System.out.println("Завершение работы программы");
        System.exit(0); // Завершение программы без ошибок
        return "Программа завершена."; // Никогда не выполнится, но требуется для компиляции
    }

    @Override
    public String getDescription() {
        return "завершить программу без сохранения";
    }
}