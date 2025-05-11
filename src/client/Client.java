package client;

import common.network.CommandRequest;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final int SERVER_PORT = 5000;
    private static final String SERVER_ADDRESS = "localhost";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (DatagramSocket socket = new DatagramSocket()) {
            while (true) {
                System.out.print("Введите команду: ");
                String commandName = scanner.nextLine().trim();

                if (commandName.equals("exit")) {
                    System.out.println("Клиент завершает работу.");
                    break;
                }

                // пока без аргументов
                CommandRequest request = new CommandRequest(commandName, null);

                // сериализация
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(byteOut);
                out.writeObject(request);
                byte[] data = byteOut.toByteArray();

                // отправка
                DatagramPacket packet = new DatagramPacket(
                        data,
                        data.length,
                        InetAddress.getByName(SERVER_ADDRESS),
                        SERVER_PORT
                );
                socket.send(packet);

                // приём ответа (ожидаем не больше 4 КБ)
                byte[] buffer = new byte[4096];
                DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(responsePacket);

                // десериализация ответа
                ByteArrayInputStream byteIn = new ByteArrayInputStream(responsePacket.getData());
                ObjectInputStream in = new ObjectInputStream(byteIn);
                Object responseObj = in.readObject();
                System.out.println("Ответ от сервера: " + responseObj);

            }
        } catch (Exception e) {
            System.err.println("Ошибка клиента: " + e.getMessage());
        }
    }
}