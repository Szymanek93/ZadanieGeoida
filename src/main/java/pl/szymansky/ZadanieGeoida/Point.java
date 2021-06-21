package pl.szymansky.ZadanieGeoida;

public class Point {
    private double x;
    private double y;
    private String operation;

    public Point(double x, double y, String operation) {
        this.x = x;
        this.y = y;
        this.operation = operation;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
