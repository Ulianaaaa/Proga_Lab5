package CommandsProvider;

public class Coordinates {
    private double x; // Значение поля должно быть больше -349
    private Long y;   // Поле не может быть null

    // Конструктор, принимающий x и y
    public Coordinates(double x, Long y) {
        if (x <= -349) {
            throw new IllegalArgumentException("Значение x не может быть меньше или равно -349");
        }
        if (y == null) {
            throw new IllegalArgumentException("Значение y не может быть null");
        }
        this.x = x;
        this.y = y;
    }

    // Геттеры и сеттеры
    public double getX() {
        return x;
    }

    public void setX(double x) {
        if (x <= -349) {
            throw new IllegalArgumentException("Значение x не может быть меньше или равно -349");
        }
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        if (y == null) {
            throw new IllegalArgumentException("Значение y не может быть null");
        }
        this.y = y;
    }
}