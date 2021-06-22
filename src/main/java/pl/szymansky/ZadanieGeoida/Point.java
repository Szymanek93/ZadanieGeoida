package pl.szymansky.ZadanieGeoida;

public class Point {
    private double x;
    private double y;
    private Operation operation;


    public Point(double x, double y, Operation operation) {
        this.x = x;
        this.y = y;
        this.operation = operation;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Operation getOperation() {
        return operation;
    }

}
