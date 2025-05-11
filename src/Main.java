import server.Server;
import sun.misc.Signal;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    private static final AtomicBoolean running = new AtomicBoolean(true);

    public static void main(String[] args) {
        // Обработчик SIGINT (Ctrl+C / Command+C)
        try {
            Signal.handle(new Signal("INT"), signal -> {
                System.out.println("\nПолучен сигнал завершения (Command+C/Ctrl+C)");
                running.set(false);
                System.exit(0);
            });
        } catch (IllegalArgumentException e) {
            System.err.println("WARN: Не удалось зарегистрировать обработчик сигналов (работает только в Oracle JDK)");
        }

        // Проверка аргумента командной строки (путь к файлу)
        if (args.length < 1) {
            System.out.println("Ошибка: укажите путь к файлу с коллекцией как аргумент командной строки.");
            System.exit(1);
        }

        String fileName = args[0]; // Получаем путь к файлу из аргумента

        // Запуск сервера
        Thread serverThread = new Thread(() -> {
            Server server = new Server(fileName); // Передаем путь к файлу в конструктор
            server.start();
        });
        serverThread.start();

        // Ожидание завершения работы
        while (running.get()) {
            try {
                Thread.sleep(1000); // Пауза, чтобы не перегружать процессор
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Работа программы завершена.");
        System.exit(0);
    }
}