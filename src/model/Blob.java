package model;

import java.awt.*;

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

    public void setMaxSpeed() {
        this.maxSpeed = 10 - 0.001 * getArea();
    }

    public void retardate(double frametime) {
        setxSpeed(getxSpeed() * 3 / 4 * frametime);
        setySpeed(getySpeed() * 3 / 4 * frametime);
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

    public void setArea(double area) {
        circle.setRadius(Math.sqrt(area/Math.PI));
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

    public boolean collides(Blob blob) {
        if (this.getCenter().distance(blob.getCenter()) < this.getRadius()+ blob.getRadius()) {
            return true;
        }
        return false;
    }

    //Magic number is the amount of relative distance allowed between blobs in a cluster
    public boolean isClustered (Blob blob) {
        int magicNumber = 1;
        if  (Math.abs(this.getCenter().distance(blob.getCenter())-(this.getRadius() + blob.getRadius())) > magicNumber) {
            return false;
        }
        return true;
    }

    public boolean eatBlob(Blob toBeEaten){
        if (this.getArea()/toBeEaten.getArea() > 4/3){
            this.setRadius(Math.sqrt((toBeEaten.getArea() + this.getArea()) / Math.PI));
            return true;
        }
        return false;
    }

    public String toString () {
        return circle.getCenter().toString() + circle.getRadius();
    }
}