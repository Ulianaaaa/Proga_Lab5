package server;

import common.commands.Command;
import common.network.CommandRequest;
import common.network.CommandResponse;
import common.models.Flat;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.time.LocalDateTime;  // Добавьте этот импорт
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

public class Server {
    private static final int PORT = 5000;
    private String fileName;

    // Конструктор для передачи пути к файлу
    public Server(String fileName) {
        this.fileName = fileName;
    }

    public void start() {
        try {
            // Настройка канала
            DatagramChannel channel = DatagramChannel.open();
            channel.bind(new InetSocketAddress(PORT));
            channel.configureBlocking(false);

            Selector selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);

            System.out.println("Сервер запущен на порту " + PORT);

            // Буфер для приёма данных
            ByteBuffer buffer = ByteBuffer.allocate(4096);

            // Инициализация компонентов
            DataProvider dataProvider = new DataProvider();

            // Получаем текущую дату для инициализации
            LocalDateTime initializationDate = LocalDateTime.now();

            // Передаем инициализационную дату в CollectionManager
            CollectionManager collectionManager = new CollectionManager(dataProvider, fileName, initializationDate);
            Scanner scanner = new Scanner(System.in);
            CommandManager commandManager = new CommandManager(collectionManager, dataProvider, fileName, scanner);

            // Основной цикл
            while (true) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (key.isReadable()) {
                        buffer.clear();
                        SocketAddress clientAddress = channel.receive(buffer);

                        if (clientAddress != null) {
                            buffer.flip();

                            ByteArrayInputStream byteIn = new ByteArrayInputStream(buffer.array(), 0, buffer.limit());
                            ObjectInputStream in = new ObjectInputStream(byteIn);
                            CommandRequest request = (CommandRequest) in.readObject();

                            System.out.println("Получена команда: " + request.getCommandName());

                            Command command = commandManager.getCommand(request.getCommandName());
                            String result;
                            if (command != null) {
                                result = command.execute(request.getArgument());
                            } else {
                                result = "Команда не найдена: " + request.getCommandName();
                            }

                            CommandResponse response = new CommandResponse(result);

                            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                            ObjectOutputStream out = new ObjectOutputStream(byteOut);
                            out.writeObject(response);
                            byte[] responseBytes = byteOut.toByteArray();

                            ByteBuffer responseBuffer = ByteBuffer.wrap(responseBytes);
                            channel.send(responseBuffer, clientAddress);

                            System.out.println("Ответ отправлен клиенту.");
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
            e.printStackTrace();
        }
    }
}