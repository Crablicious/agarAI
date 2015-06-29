package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

//TODO: Add ability to split.
public class AdvBlob{
	private double maxSpeed;
    private ArrayList<Blob> blobs;

	public AdvBlob(Point blobCenterPoint, int radius) {
        blobs = new ArrayList<Blob>();
        blobs.add(new Blob(blobCenterPoint, radius));
        updateMaxSpeed();
	}

    public void updateMaxSpeed() {
        for (Blob blob : blobs) {
            blob.setMaxSpeed(10 - 0.001 * blob.getArea());
        }
    }

	public void updateSpeed(Set<Input> input, double frametime) {
        //TODO: Add speed towards closest blob?
        updateMaxSpeed();
        //(0,0) top left corner
        if (input.contains(new Input(Input.Dir.EAST))){
            for (Blob blob : blobs) {
                blob.setxSpeed(blob.getxSpeed() + maxSpeed/10 * frametime);
            }
        }
        if (input.contains(new Input(Input.Dir.WEST))){
            for (Blob blob : blobs) {
                blob.setxSpeed(blob.getxSpeed() - maxSpeed/10 * frametime);
            }
        }
        if (input.contains(new Input(Input.Dir.NORTH))){
            for (Blob blob : blobs) {
                blob.setySpeed(blob.getySpeed() - maxSpeed / 10 * frametime);
            }
        }
        if (input.contains(new Input(Input.Dir.SOUTH))){
            for (Blob blob : blobs) {
                blob.setySpeed(blob.getySpeed() + maxSpeed/10 * frametime);
            }
        }

        //Speed can't be greater than maxSpeed
        for (Blob blob : blobs) {
            if (Math.abs(blob.getxSpeed()) > maxSpeed) {
                blob.setxSpeed(blob.getxSpeed() / Math.abs(blob.getxSpeed()) * maxSpeed);
            }
            if (Math.abs(blob.getySpeed()) > maxSpeed) {
                blob.setySpeed(blob.getySpeed() / Math.abs(blob.getySpeed()) * maxSpeed);
            }
        }
	}

    //TODO: Test how all blobs in one AdvBlob react when stuffed together.
    /* Is responsible for boundaries */
    public void move(double frametime, Dimension field) {
        for (Blob blob : blobs) {
            blob.getCenter().x += (int)(blob.getxSpeed() * frametime);
            blob.getCenter().y += (int)(blob.getySpeed() * frametime);

            if ((blob.getCenter().x + blob.getRadius()) > field.getWidth()) {
                blob.getCenter().x = (int)(field.getWidth() - blob.getRadius());
            }else if (blob.getCenter().x - blob.getRadius() < 0) {
                blob.getCenter().x = (int)(0 + blob.getRadius());
            }

            if ((blob.getCenter().y + blob.getRadius()) > field.getHeight()) {
                blob.getCenter().y = (int)(field.getHeight() - blob.getRadius());
            }else if (blob.getCenter().y - blob.getRadius() < 0) {
                blob.getCenter().y = (int)(0 + blob.getRadius());
            }

            //Collisiondetection for all "blobs" in AdvBlob after the move
            for (Blob collisionBlob : blobs) {
                if (collisionBlob != blob){
                    if (blob.collides(collisionBlob)){
                        //First determine what quadrant we should search in.
                        boolean[] isPositive = {false, false}; // x,y
                        if (blob.getCenter().x >= collisionBlob.getCenter().x) isPositive[0] = true;
                        if (blob.getCenter().y >= collisionBlob.getCenter().y) isPositive[1] = true;
                        int quadrant = 0;
                        if (isPositive[0] && isPositive[1]) quadrant = 1;
                        else if (!isPositive[0] && isPositive[1]) quadrant = 2;
                        else if (!isPositive[0] && !isPositive[1]) quadrant = 3;
                        else if (isPositive[0] && !isPositive[1]) quadrant = 4;

                        int granularity = 10;
                        Point closestColEdgePoint = collisionBlob.getCenter();
                        double testAngle;
                        int testX;
                        int testY;
                        double corrAngle = 0;
                        for (int i = 0; granularity > i; i++){
                            testAngle = i*Math.PI/2/granularity;
                            testX = (int) (collisionBlob.getRadius() * Math.sin(testAngle));
                            testY = (int) (collisionBlob.getRadius() * Math.cos(testAngle));
                            switch (quadrant) {
                                case 1:
                                    break;
                                case 2:
                                    testAngle += Math.PI/2;
                                    testX = -testX;
                                    break;
                                case 3:
                                    testAngle += Math.PI;
                                    testX = -testX;
                                    testY = -testY;
                                    break;
                                case 4:
                                    testAngle += Math.PI*3/2;
                                    testY = -testY;
                                default: System.out.println("Quadrant never initialized");
                                    break;
                            }
                            if (new Point(collisionBlob.getCenter().x + testX,
                                    collisionBlob.getCenter().y + testY).distance(blob.getCenter()) <
                                    closestColEdgePoint.distance(blob.getCenter())) {
                                closestColEdgePoint = new Point(collisionBlob.getCenter().x + testX,
                                        collisionBlob.getCenter().y + testY);
                                corrAngle = testAngle;
                            }
                        }
                        testX = (int)((blob.getRadius() - closestColEdgePoint.distance(blob.getCenter()))
                                * Math.sin(corrAngle));
                        testY = (int)((blob.getRadius() - closestColEdgePoint.distance(blob.getCenter()))
                                * Math.cos(corrAngle));
                        Point closestBlobEdgePoint = new Point(testX, testY);
                        blob.getCenter().x -= (closestBlobEdgePoint.x - closestColEdgePoint.x);
                        blob.getCenter().y -= (closestBlobEdgePoint.y - closestColEdgePoint.y);
                    }
                }
            }
        }
    }

    public void updatePosition(Set<Input> input, long delta_t, int framerate, Dimension field) {
        double frametime = delta_t/(1000/framerate);
        //Retardation
        for (Blob blob : blobs) {
            blob.setxSpeed(blob.getxSpeed() * 3 / 4 * frametime);
            blob.setySpeed(blob.getySpeed() * 3 / 4 * frametime);
        }
        updateSpeed(input, frametime);
        move(frametime, field);
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    //Returns true if "blob" is eaten.
    public boolean collideAndEat(Blob blob){
        for (Blob thisBlob : blobs) {
            if (thisBlob.getCenter().distance(blob.getCenter()) < thisBlob.getRadius()+ blob.getRadius()) {
                if (thisBlob.getArea()/blob.getArea() > 4/3){
                    thisBlob.setRadius(Math.sqrt((blob.getArea() + thisBlob.getArea()) / Math.PI));
                    return true;
                }
            }
        }
        return false;
    }

    public Iterator<Blob>getBlobIterator (){
        return blobs.iterator();
    }

    public ArrayList<Blob>getBlobs () {
        return blobs;
    }

    public boolean isEmpty(){
        return blobs.isEmpty();
    }
}
