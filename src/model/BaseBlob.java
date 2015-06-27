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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(radius);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + (blobCenterPoint != null ? blobCenterPoint.hashCode() : 0);
        return result;
    }
}