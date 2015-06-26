package model;

import java.awt.*;

public class BaseBlob {
	double radius;
	Point blobCenterPoint;
	
	public BaseBlob(Point blobCenterPoint) {
		this.blobCenterPoint = blobCenterPoint;
		radius = 10;		
	}

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
}