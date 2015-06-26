package model;

import java.awt.*;

public class BaseBlob {
	double radius;
	Point blobCenterPoint;

	public BaseBlob(Point blobCenterPoint, int radius) {
        this.blobCenterPoint = blobCenterPoint;
        this.radius = radius;
    }
	
	public Point getBlobCenterPoint() {
		return blobCenterPoint;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public double getArea() {
		return Math.pow(radius, 2) * Math.PI;
	}

    public boolean collides(BaseBlob blob) {
        if (this.getBlobCenterPoint().distance(blob.getBlobCenterPoint()) < this.getRadius()+ blob.getRadius()) {
            return true;
        }
        return false;
    }
}