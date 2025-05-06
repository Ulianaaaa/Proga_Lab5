package CommandsProvider;

public class House {
    private String name;
    private Long year;
    private long numberOfFlatsOnFloor;

    public House(String name, Long year, long numberOfFlatsOnFloor) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя дома не может быть null или пустым");
        }
        if (year == null || year <= 0 || year > 197) {
            throw new IllegalArgumentException("Год должен быть больше 0 и меньше или равен 197");
        }
        if (numberOfFlatsOnFloor <= 0) {
            throw new IllegalArgumentException("Количество квартир на этаже должно быть больше 0");
        }
        this.name = name;
        this.year = year;
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя дома не может быть null или пустым");
        }
        this.name = name;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        if (year == null || year <= 0 || year > 197) {
            throw new IllegalArgumentException("Год должен быть больше 0 и меньше или равен 197");
        }
        this.year = year;
    }

    public long getNumberOfFlatsOnFloor() {
        return numberOfFlatsOnFloor;
    }

    public void setNumberOfFlatsOnFloor(long numberOfFlatsOnFloor) {
        if (numberOfFlatsOnFloor <= 0) {
            throw new IllegalArgumentException("Количество квартир на этаже должно быть больше 0");
        }
        this.numberOfFlatsOnFloor = numberOfFlatsOnFloor;
    }

    @Override
    public String toString() {
        return "House{name='" + name + "', year=" + year + ", flatsOnFloor=" + numberOfFlatsOnFloor + "}";
    }
}