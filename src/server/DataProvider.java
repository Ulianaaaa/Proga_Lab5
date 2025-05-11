package server;

import common.models.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataProvider {
    // Формат даты для сохранения и загрузки
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void save(TreeSet<Flat> flats, String fileName) {
        File file = new File(fileName);

        if (file.exists() && !file.canWrite()) {
            System.err.println("Ошибка: нет прав на запись в файл " + fileName);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            for (Flat flat : flats) {
                writer.write(toCsvLine(flat));
                writer.newLine();
            }

            System.out.println("[SUCCESS] Коллекция сохранена (" + flats.size() + " элементов)");
        } catch (IOException e) {
            System.err.println("[ERROR] Ошибка при сохранении: " + e.getMessage());
        }
    }

    private String toCsvLine(Flat flat) {
        return String.join(",",
                String.valueOf(flat.getId()),
                escapeCsv(flat.getName()),
                String.valueOf(flat.getCoordinates().getX()),
                String.valueOf(flat.getCoordinates().getY()),
                DATE_FORMAT.format(flat.getCreationDate()),
                String.valueOf(flat.getArea()),
                String.valueOf(flat.getNumberOfRooms()),
                flat.getFurnish().name(),
                flat.getView().name(),
                flat.getTransport() != null ? flat.getTransport().name() : "",
                flat.getHouse() != null ? escapeCsv(flat.getHouse().getName()) : "",
                flat.getHouse() != null ? String.valueOf(flat.getHouse().getYear()) : "",
                flat.getHouse() != null ? String.valueOf(flat.getHouse().getNumberOfFlatsOnFloor()) : ""
        );
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        return value.contains(",") ? "\"" + value + "\"" : value;
    }

    public TreeSet<Flat> load(String fileName) {
        TreeSet<Flat> flats = new TreeSet<>();
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("[INFO] Файл не существует, будет создан при сохранении");
            return flats;
        }

        if (file.length() == 0) {
            System.out.println("[INFO] Файл пуст");
            return flats;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    if (line.trim().isEmpty()) continue;

                    Flat flat = parseCsvLine(line);
                    if (flat != null) {
                        flats.add(flat);
                    }
                } catch (Exception e) {
                    System.err.println("[WARN] Ошибка парсинга строки: " + line);
                    System.err.println("Причина: " + e.getMessage());
                }
            }

            System.out.println("[SUCCESS] Загружено " + flats.size() + " элементов");
        } catch (Exception e) {
            System.err.println("[ERROR] Ошибка чтения файла: " + e.getMessage());
        }

        return flats;
    }

    private Flat parseCsvLine(String line) throws Exception {
        String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        if (tokens.length < 10) {
            throw new IllegalArgumentException("Недостаточно полей в строке CSV");
        }

        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].replace("\"", "").trim();
        }

        LocalDateTime creationDate = LocalDateTime.parse(tokens[4], DATE_FORMAT);

        return new Flat(Integer.parseInt(tokens[0]),
                tokens[1],
                new Coordinates(
                        Double.parseDouble(tokens[2]),
                        Long.parseLong(tokens[3])
                ),
                creationDate,
                Long.parseLong(tokens[5]),
                Long.parseLong(tokens[6]),
                Furnish.valueOf(tokens[7]),
                View.valueOf(tokens[8]),
                tokens[9].isEmpty() ? null : Transport.valueOf(tokens[9]),
                tokens.length > 10 && !tokens[10].isEmpty() ?
                        new House(
                                tokens[10],
                                Long.parseLong(tokens[11]),
                                Long.parseLong(tokens[12])
                        ) : null
        );
    }
}