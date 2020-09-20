package task2;

public class Point {
    double x;
    double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static double distance (Point p1, Point p2) {
        double xDistance = Math.abs(p1.x - p2.x);
        double yDistance = Math.abs(p1.y - p2.y);

        return Math.sqrt(xDistance * xDistance + yDistance * yDistance);
    }
}
