package model;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Adam on 2015-06-29.
 */
public class Blob {
    private Circle circle;
    private double xSpeed;
    private double ySpeed;
    private double maxSpeed;

    public Blob(Point blobCenterPoint, int radius) {
        circle = new Circle(blobCenterPoint, radius);
        xSpeed = 0;
        ySpeed = 0;
        maxSpeed = 0;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(double xSpeed) {
        this.xSpeed = xSpeed;
    }

    public double getySpeed() {
        return ySpeed;
    }

    public void setySpeed(double ySpeed) {
        this.ySpeed = ySpeed;
    }

    public void setRadius(double radius) {
        this.circle.setRadius(radius);
    }

    public void setCenter(Point center) {
        circle.setCenter(center);
    }

    public double getArea() {
        return Math.pow(circle.getRadius(), 2) * Math.PI;
    }

    public Point getCenter () {
        return circle.getCenter();
    }

    public double getRadius() {
        return circle.getRadius();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return false;
    }

    //TODO: Make this work for an array of circles vs an array of circles.
    public boolean collides(Blob blob) {
        if (this.getCenter().distance(blob.getCenter()) < this.getRadius()+ blob.getRadius()) {
            return true;
        }
        return false;
    }

    public boolean eatBlob(Blob toBeEaten){
        if (this.getArea()/toBeEaten.getArea() > 4/3){
            this.setRadius(Math.sqrt((toBeEaten.getArea() + this.getArea()) / Math.PI));
            return true;
        }
        return false;
    }
}

//TODO: Make all circles of the same blob go together and join up.