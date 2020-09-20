package task2;

public class Start {
    public static void main(String[] args) {
        Point p1 = new Point(0, 0);
        Point p2 = new Point(0, 0);
        printDistance(p1, p2);

        Point p3 = new Point(0, 0);
        Point p4 = new Point(1, 1);
        printDistance(p3, p4);

        Point p5 = new Point(2, 1);
        Point p6 = new Point(1, 1);
        printDistance(p5, p6);

        Point p7 = new Point(1, 5);
        Point p8 = new Point(0.5, 10.5);
        printDistance(p7, p8);
    }

    public static void printDistance(Point p1, Point p2) {
        System.out.println("Distance between (" + p1.x + ", " + p1.y +
                ") and (" + p2.x + ", " + p2.y + ") = " + p1.distanceFromPoint(p2));
    }
}
