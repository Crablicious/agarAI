package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

//TODO: Add ability to split.
//TODO: Multiple blobs and their "move" isn't working correctly.
public class AdvBlob{
    private ArrayList<Blob> blobs;

	public AdvBlob(Point blobCenterPoint, int radius) {
        blobs = new ArrayList<Blob>();
        blobs.add(new Blob(blobCenterPoint, radius));
        blobs.add(new Blob(new Point(blobCenterPoint.x+2*radius, blobCenterPoint.y+2*radius), radius));
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
                blob.setxSpeed(blob.getxSpeed() + blob.getMaxSpeed()/10 * frametime);
            }
        }
        if (input.contains(new Input(Input.Dir.WEST))){
            for (Blob blob : blobs) {
                blob.setxSpeed(blob.getxSpeed() - blob.getMaxSpeed()/10 * frametime);
            }
        }
        if (input.contains(new Input(Input.Dir.NORTH))){
            for (Blob blob : blobs) {
                blob.setySpeed(blob.getySpeed() - blob.getMaxSpeed() / 10 * frametime);
            }
        }
        if (input.contains(new Input(Input.Dir.SOUTH))){
            for (Blob blob : blobs) {
                blob.setySpeed(blob.getySpeed() + blob.getMaxSpeed()/10 * frametime);
            }
        }

        //Speed can't be greater than maxSpeed
        for (Blob blob : blobs) {
            if (Math.abs(blob.getxSpeed()) > blob.getMaxSpeed()) {
                blob.setxSpeed(blob.getxSpeed() / Math.abs(blob.getxSpeed()) * blob.getMaxSpeed());
            }
            if (Math.abs(blob.getySpeed()) > blob.getMaxSpeed()) {
                blob.setySpeed(blob.getySpeed() / Math.abs(blob.getySpeed()) * blob.getMaxSpeed());
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
                        Point collisionVector = getCollisionVector(blob, collisionBlob);
                        blob.getCenter().translate(collisionVector.x, collisionVector.y);
                    }
                }
            }
        }
    }

    //Returns a vector that subBlob is supposed to move. Everything is from domBlob's perspective.
    //blob = subBlob, collisionBlob = domBlob.
    private Point getCollisionVector(Blob subBlob, Blob domBlob) {
        int quadrant = findQuadrant(subBlob.getCenter(), domBlob.getCenter());

        double closestAngle = 42;
        Point closestToSub = domBlob.getCenter();
        int granularity = 10;
        //Checks [0, PI/2], [PI/2, PI], [PI, 3/2PI], [3/2PI, 2PI].
        for (int i = 0; granularity >= i; i++) {
            double testAngle = i*Math.PI/2/granularity + (quadrant-1)*Math.PI/2;
            int testX = (int) (domBlob.getRadius() * Math.cos(testAngle));
            int testY = (int) (domBlob.getRadius() * Math.sin(testAngle));
            Point testPoint = new Point(domBlob.getCenter().x + testX, domBlob.getCenter().y + testY);
            if (testPoint.distance(subBlob.getCenter()) < closestToSub.distance(subBlob.getCenter())){
                closestToSub = testPoint;
                closestAngle = testAngle;
            }
        }
        double relativeDistance = Math.abs(subBlob.getRadius() - closestToSub.distance(subBlob.getCenter()));
        Point collisionVector = new Point((int) (relativeDistance * Math.cos(closestAngle)), (int)
                (relativeDistance * Math.sin(closestAngle)));
        return collisionVector;
    }

    private int findQuadrant (Point subCircle, Point domCircle) {
        boolean[] isPositive = {false, false}; // x,y
        if (subCircle.x >= domCircle.x) isPositive[0] = true;
        if (subCircle.y >= domCircle.y) isPositive[1] = true;
        int quadrant = 0;
        if (isPositive[0] && isPositive[1]) quadrant = 1;
        else if (!isPositive[0] && isPositive[1]) quadrant = 2;
        else if (!isPositive[0] && !isPositive[1]) quadrant = 3;
        else if (isPositive[0] && !isPositive[1]) quadrant = 4;
        return quadrant;
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

    //Returns true if "blob" is eaten. Callers responsibility to remove said blob.
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
