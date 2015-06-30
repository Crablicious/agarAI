package model;

import java.awt.*;

/**
 * Created by Adam on 2015-06-29.
 */
public class Circle {
    private double radius;
    private Point circleCenterPoint;

    public Circle(Point circleCenterPoint, double radius) {
        this.circleCenterPoint = circleCenterPoint;
        this.radius = radius;
    }

    public Point getCenter() {
        return circleCenterPoint;
    }

    public void setCenter (Point newCenter) {
        this.circleCenterPoint = newCenter;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Circle)) return false;

        Circle circle = (Circle) o;

        if (Double.compare(circle.radius, radius) != 0) return false;
        return !(circleCenterPoint != null ? !circleCenterPoint.equals(circle.circleCenterPoint) : circle.circleCenterPoint != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(radius);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (circleCenterPoint != null ? circleCenterPoint.hashCode() : 0);
        return result;
    }
}
