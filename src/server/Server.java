package server;

import common.commands.Command;
import common.network.CommandRequest;
import common.network.CommandResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private static final int PORT = 5000;
    private final String fileName;
    private DatagramChannel channel;
    private Selector selector;
    private CommandManager commandManager;

    public Server(String fileName) {
        this.fileName = fileName;
    }

    public void start() {
        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(PORT));
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);

            DataProvider dataProvider = new DataProvider();
            CollectionManager collectionManager = new CollectionManager(
                    dataProvider, fileName, LocalDateTime.now()
            );
            Scanner scanner = new Scanner(System.in);
            this.commandManager = new CommandManager(collectionManager, dataProvider, fileName, scanner);

            logger.info("Сервер запущен на порту {}", PORT);

            ByteBuffer buffer = ByteBuffer.allocate(65536);
            while (true) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (key.isReadable()) {
                        handleClientRequest(buffer);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Ошибка сервера: {}", e.getMessage(), e);
        } finally {
            closeResources();
        }
    }

    private void handleClientRequest(ByteBuffer buffer) {
        buffer.clear();
        try {
            SocketAddress clientAddress = channel.receive(buffer);
            if (clientAddress != null) {
                buffer.flip();
                CommandRequest request = deserializeRequest(buffer);

                logger.info("Получена команда: {} от клиента {}", request.getCommandName(), clientAddress);

                String response = processCommand(request);
                sendResponse(response, clientAddress);
                logger.info("Ответ отправлен клиенту {}", clientAddress);
            }
        } catch (Exception e) {
            logger.error("Ошибка обработки запроса: {}", e.getMessage(), e);
        }
    }

    private CommandRequest deserializeRequest(ByteBuffer buffer)
            throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(buffer.array(), 0, buffer.limit());
             ObjectInputStream in = new ObjectInputStream(byteIn)) {
            return (CommandRequest) in.readObject();
        }
    }

    private String processCommand(CommandRequest request) {
        try {
            String commandName = request.getCommandName();
            Object argument = request.getArgument();

            Command command = commandManager.getCommands().get(commandName);
            if (command == null) {
                logger.warn("Получена неизвестная команда: {}", commandName);
                return "Неизвестная команда: " + commandName;
            }

            return command.execute(argument);
        } catch (Exception e) {
            logger.error("Ошибка выполнения команды: {}", e.getMessage(), e);
            return "Ошибка выполнения команды: " + e.getMessage();
        }
    }
    private void sendResponse(String responseText, SocketAddress clientAddress) throws IOException {
        CommandResponse response = new CommandResponse(responseText);
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(byteOut)) {

            out.writeObject(response);
            byte[] responseBytes = byteOut.toByteArray();
            ByteBuffer responseBuffer = ByteBuffer.wrap(responseBytes);
            channel.send(responseBuffer, clientAddress);
        }
    }

    private void closeResources() {
        try {
            if (selector != null) selector.close();
            if (channel != null) channel.close();
            logger.info("Сервер завершил работу.");
        } catch (IOException e) {
            logger.error("Ошибка при закрытии ресурсов: {}", e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Укажите путь к файлу данных как аргумент командной строки");
            return;
        }
        new Server(args[0]).start();
    }
}