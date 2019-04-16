package $1;

public class Point {
    private double x;
    private double y;
    public int id;
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(double x, double y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
