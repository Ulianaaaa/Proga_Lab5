package CommandsProvider;

public class Coordinates {
    private double x;
    private Long y;

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

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}