package client;

import common.models.Flat;
import common.network.CommandRequest;
import common.network.CommandResponse;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final int SERVER_PORT = 5000;
    private static final String SERVER_ADDRESS = "localhost";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(3000); // Таймаут 3 секунды

            while (true) {
                System.out.print("Введите команду: ");
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+", 2);
                String commandName = parts[0];
                Object argument = null;

                if (commandName.equals("exit")) {
                    System.out.println("Клиент завершает работу.");
                    break;
                }

                else if (commandName.equals("update") && parts.length > 1) {
                    try {
                        int id = Integer.parseInt(parts[1]);

                        // Шаг 1: проверка наличия ID
                        CommandRequest checkRequest = new CommandRequest("update", id);
                        ByteArrayOutputStream checkOut = new ByteArrayOutputStream();
                        ObjectOutputStream checkObj = new ObjectOutputStream(checkOut);
                        checkObj.writeObject(checkRequest);
                        byte[] checkData = checkOut.toByteArray();

                        DatagramPacket checkPacket = new DatagramPacket(
                                checkData,
                                checkData.length,
                                InetAddress.getByName(SERVER_ADDRESS),
                                SERVER_PORT
                        );
                        socket.send(checkPacket);

                        // Получение ответа на проверку ID
                        byte[] checkBuffer = new byte[65507];
                        DatagramPacket checkResponsePacket = new DatagramPacket(checkBuffer, checkBuffer.length);
                        try {
                            socket.receive(checkResponsePacket);
                        } catch (SocketTimeoutException e) {
                            System.out.println("Сервер временно недоступен. Повторите попытку позже.");
                            continue;
                        }

                        ByteArrayInputStream checkIn = new ByteArrayInputStream(
                                checkResponsePacket.getData(), 0, checkResponsePacket.getLength());
                        ObjectInputStream checkResponseObj = new ObjectInputStream(checkIn);
                        CommandResponse checkResponse = (CommandResponse) checkResponseObj.readObject();

                        if (!checkResponse.getResponseText().equals("OK")) {
                            System.out.println("Ответ от сервера: " + checkResponse.getResponseText());
                            continue;
                        }

                        // Шаг 2: если ID найден, запрашиваем новые данные
                        System.out.println("Введите новые данные для квартиры:");
                        FlatBuilder builder = new FlatBuilder(scanner);
                        Flat updatedFlat = builder.buildFlat();
                        argument = new Object[]{id, updatedFlat};

                        // Отправка основного запроса на обновление
                        CommandRequest updateRequest = new CommandRequest("update", argument);
                        ByteArrayOutputStream updateOut = new ByteArrayOutputStream();
                        ObjectOutputStream updateObj = new ObjectOutputStream(updateOut);
                        updateObj.writeObject(updateRequest);
                        byte[] updateData = updateOut.toByteArray();

                        DatagramPacket updatePacket = new DatagramPacket(updateData,
                                updateData.length,
                                InetAddress.getByName(SERVER_ADDRESS),
                                SERVER_PORT
                        );
                        socket.send(updatePacket);

                        // Получение финального ответа
                        byte[] updateBuffer = new byte[65507];
                        DatagramPacket finalResponsePacket = new DatagramPacket(updateBuffer, updateBuffer.length);
                        try {
                            socket.receive(finalResponsePacket);
                        } catch (SocketTimeoutException e) {
                            System.out.println("Сервер временно недоступен. Повторите попытку позже.");
                            continue;
                        }

                        ByteArrayInputStream finalIn = new ByteArrayInputStream(
                                finalResponsePacket.getData(), 0, finalResponsePacket.getLength());
                        ObjectInputStream finalObj = new ObjectInputStream(finalIn);
                        CommandResponse finalResponse = (CommandResponse) finalObj.readObject();

                        System.out.println("Ответ от сервера: " + finalResponse.getResponseText());
                        continue;

                    } catch (NumberFormatException e) {
                        System.out.println("Ошибка: id должен быть числом");
                        continue;
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Ошибка при обработке update: " + e.getMessage());
                        continue;
                    }
                }

                else if (commandName.equals("add")) {
                    FlatBuilder builder = new FlatBuilder(scanner);
                    argument = builder.buildFlat();
                }

                else if (commandName.equals("remove_lower")) {
                    System.out.println("Введите данные квартиры для сравнения:");
                    FlatBuilder builder = new FlatBuilder(scanner);
                    argument = builder.buildFlat();
                }

                else if (commandName.equals("add_if_min")) {
                    System.out.println("Введите данные квартиры для добавления (если её площадь минимальна):");
                    FlatBuilder builder = new FlatBuilder(scanner);
                    argument = builder.buildFlat();
                }

                else if (commandName.equals("add_if_max")) {
                    System.out.println("Введите данные квартиры для добавления (если её площадь максимальна):");
                    FlatBuilder builder = new FlatBuilder(scanner);
                    argument = builder.buildFlat();
                }

                else if (parts.length > 1) {
                    argument = parts[1];
                }

                // Отправка запроса
                try {
                    CommandRequest request = new CommandRequest(commandName, argument);
                    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                    ObjectOutputStream out = new ObjectOutputStream(byteOut);
                    out.writeObject(request);
                    byte[] data = byteOut.toByteArray();

                    DatagramPacket packet = new DatagramPacket(
                            data,
                            data.length,
                            InetAddress.getByName(SERVER_ADDRESS),
                            SERVER_PORT
                    );
                    socket.send(packet);

                    // Получение ответа
                    byte[] buffer = new byte[65507];
                    DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(responsePacket);

                    ByteArrayInputStream byteIn = new ByteArrayInputStream(
                            responsePacket.getData(), 0, responsePacket.getLength());
                    ObjectInputStream in = new ObjectInputStream(byteIn);
                    CommandResponse response = (CommandResponse) in.readObject();

                    System.out.println("Ответ от сервера: " + response.getResponseText());
                } catch (SocketTimeoutException e) {
                    System.out.println("Сервер временно недоступен. Повторите попытку позже.");
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Ошибка при получении ответа: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка клиента: " + e.getMessage());
            e.printStackTrace();
        }
    }
}