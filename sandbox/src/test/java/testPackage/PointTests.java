package testPackage;

import org.testng.Assert;
import org.testng.annotations.Test;
import task2.Point;

public class PointTests {

    @Test
    public void testTwoZeroPoints() {
        Point p1 = new Point(0, 0);
        Point p2 = new Point(0, 0);
        Assert.assertEquals(p1.distanceFromPoint(p2), 0);
    }

    @Test
    public void testZeroPointWithNotZeroPoint() {
        Point p1 = new Point(0, 0);
        Point p2 = new Point(1, 1);
        Assert.assertEquals(p1.distanceFromPoint(p2), Math.sqrt(2));
    }

    @Test
    public void testTwoPointsWithSameYAxisValue() {
        Point p1 = new Point(2, 1);
        Point p2 = new Point(1, 1);
        Assert.assertEquals(p1.distanceFromPoint(p2), 1);
    }

    @Test
    public void testIntegerPointWithDoublePoint() {
        Point p1 = new Point(1, 5);
        Point p2 = new Point(0.5, 10.5);
        Assert.assertEquals(p1.distanceFromPoint(p2), Math.sqrt(30.5));
    }
}
