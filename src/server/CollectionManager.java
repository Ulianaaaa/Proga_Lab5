package server;

import common.models.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CollectionManager {
    private final TreeSet<Flat> flats = new TreeSet<>();
    private final DataProvider dataProvider;
    private final String fileName;
    private final LocalDateTime initializationDate;

    public CollectionManager(DataProvider dataProvider, String fileName, LocalDateTime initializationDate) {
        this.dataProvider = dataProvider;
        this.fileName = fileName;
        this.initializationDate = initializationDate;
        loadFromFile(fileName);
    }

    public boolean addFlat(Flat flat){
        return flats.add(flat);
    }
    public boolean removeById(long id){
        return flats.removeIf(flat -> flat.getId()==id);
    }

    public void clearFlats(){
        flats.clear();
    }

    public LocalDateTime getInitializationDate(){
        return initializationDate;
    }

    public TreeSet<Flat> getFlats() {
        return flats;
    }

    public void loadFromFile(String fileName) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Flat flat = parseCSVLine(line);
                    flats.add(flat);
                } catch (Exception e) {
                    System.err.println("Ошибка при разборе строки: " + line);
                    e.printStackTrace();
                    System.err.println("Причина: " + e.getMessage());
                }
            }
            System.out.println("Коллекция успешно загружена. Элементов: " + flats.size());
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    private Flat parseCSVLine(String line) throws Exception {
        String[] tokens = line.split(",", -1); // -1 сохраняет пустые значения

        if (tokens.length < 13) {
            throw new IllegalArgumentException("Ожидается минимум 13 полей, получено: " + tokens.length);
        }

        int id = Integer.parseInt(tokens[0].trim());
        String name = tokens[1].trim();
        double x = Double.parseDouble(tokens[2].trim());
        Long y = Long.parseLong(tokens[3].trim());
        Coordinates coordinates = new Coordinates(x, y);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime creationDate = LocalDateTime.parse(tokens[4].trim(), formatter);

        Long area = Long.parseLong(tokens[5].trim());
        long numberOfRooms = Long.parseLong(tokens[6].trim());

        Furnish furnish = tokens[7].isEmpty() ? null : Furnish.valueOf(tokens[7].trim());
        View view = tokens[8].isEmpty() ? null : View.valueOf(tokens[8].trim());
        Transport transport = tokens[9].isEmpty() ? null : Transport.valueOf(tokens[9].trim());

        House house = null;
        if (!tokens[10].isEmpty() && !tokens[11].isEmpty() && !tokens[12].isEmpty()) {
            String houseName = tokens[10].trim();
            Long houseYear = Long.parseLong(tokens[11].trim());
            long flatsOnFloor = Long.parseLong(tokens[12].trim());
            house = new House(houseName, houseYear, flatsOnFloor);
        }

        return new Flat(id, name, coordinates, creationDate, area, numberOfRooms, furnish, view, transport, house);
    }

    public void saveToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {
            for (Flat flat : flats) {
                writer.write(toCSV(flat));
                writer.newLine();
            }
            System.out.println("Коллекция успешно сохранена.");
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении файла: " + e.getMessage());
        }
    }

    private String toCSV(Flat flat) {
        return String.join(",",
                String.valueOf(flat.getId()),
                flat.getName(),
                String.valueOf(flat.getCoordinates().getX()),
                String.valueOf(flat.getCoordinates().getY()),
                flat.getCreationDate().toString(),
                String.valueOf(flat.getArea()),
                String.valueOf(flat.getNumberOfRooms()),
                flat.getFurnish() == null ? "" : flat.getFurnish().name(),
                flat.getView() == null ? "" : flat.getView().name(),
                flat.getTransport() == null ? "" : flat.getTransport().name(),
                flat.getHouse() == null ? "" : flat.getHouse().getName(),
                flat.getHouse() == null ? "" : flat.getHouse().getYear().toString(),
                flat.getHouse() == null ? "" : String.valueOf(flat.getHouse().getNumberOfFlatsOnFloor())
        );
    }
}
