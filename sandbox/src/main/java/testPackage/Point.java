package testPackage;

public class Point {
    double x;
    double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distanceFromPoint(Point p) {
        double xDistance = Math.abs(this.x - p.x);
        double yDistance = Math.abs(this.y - p.y);

        return Math.sqrt(xDistance * xDistance + yDistance * yDistance);
    }
}
