package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

//TODO: Add ability to split.
public class AdvBlob{
    private ArrayList<Cluster> clusters;

    public class Cluster{
        private ArrayList<Blob> blobs;

        public Cluster(Blob blob) {
            blobs = new ArrayList<Blob>();
            this.blobs.add(blob);
        }

        public double getLargestRadius () {
            double greatest = 0;
            for (Blob blob : blobs) {
                if (blob.getRadius() > greatest) {
                    greatest = blob.getRadius();
                }
            }
            return greatest;
        }

        public void add(Blob blob) {
            blobs.add(blob);
        }

        public void remove(Blob blob) {
            if (blobs.contains(blob)) blobs.remove(blob);
        }

        public boolean contains(Blob blob) {
            if (blobs.contains(blob)) return true;
            return false;
        }

        public int getSize() {
            return blobs.size();
        }

        public Blob get(int i) {
            if (i >= blobs.size()) return null;
            return blobs.get(i);
        }

        public boolean isEmpty() {
            if (blobs.isEmpty()) return true;
            return false;
        }
    }

	public AdvBlob(Point blobCenterPoint, int radius) {
        clusters = new ArrayList<Cluster>();
        clusters.add(new Cluster(new Blob(blobCenterPoint, radius)));
        clusters.get(0).add(new Blob(new Point(blobCenterPoint.x + 4 * radius, blobCenterPoint.y + 2 * radius), radius));
        clusters.get(0).add(new Blob(new Point(blobCenterPoint.x + 8 * radius, blobCenterPoint.y + 2 * radius), radius));
        //clusters.get(0).add(new Blob(new Point(blobCenterPoint.x+12*radius, blobCenterPoint.y+2*radius), radius));
        /*blobs.add(new Blob(blobCenterPoint, radius));
        blobs.add(new Blob(new Point(blobCenterPoint.x+2*radius, blobCenterPoint.y+2*radius), radius));*/
        updateMaxSpeed();
	}

    //Returns the shot baseblobs with the correct size and speed.
    public ArrayList<BaseBlob> shoot(Set<Input> input) {
        Iterator<Blob> blobIterator = this.getBlobIterator();
        ArrayList<BaseBlob> shotBlobs = new ArrayList<>();
        int xOffset;
        int yOffset;
        int magicOffset = 10;
        while (blobIterator.hasNext()) {
            xOffset = 0;
            yOffset = 0;
            Blob current = blobIterator.next();
            if (current.getRadius() > 10){
                Point start = new Point(current.getCenter().x, current.getCenter().y);
                BaseBlob shotOne = new BaseBlob(start, 5);

                //Offset and speed calc
                if (input.contains(new Input(Input.Action.EAST))) {
                    if (xOffset == 0){
                        xOffset = (int)(current.getRadius() + shotOne.getBlob().getRadius() + magicOffset);
                    }else {
                        xOffset += (current.getRadius() + shotOne.getBlob().getRadius() + xOffset / Math.abs(xOffset) * magicOffset);
                    }
                    shotOne.getBlob().setxSpeed(shotOne.getBlob().getxSpeed() + 40);
                }
                if (input.contains(new Input(Input.Action.WEST))) {
                    if (xOffset == 0) {
                        xOffset = -(int)(current.getRadius() + shotOne.getBlob().getRadius() + magicOffset);
                    }else {
                        xOffset -= (current.getRadius() + shotOne.getBlob().getRadius() + xOffset / Math.abs(xOffset) * magicOffset);
                    }
                    shotOne.getBlob().setxSpeed(shotOne.getBlob().getxSpeed() - 40);
                }
                if (input.contains(new Input(Input.Action.NORTH))) {
                    if (yOffset == 0){
                        yOffset = -(int)(current.getRadius() + shotOne.getBlob().getRadius() + magicOffset);
                    }else {
                        yOffset -= (current.getRadius() + shotOne.getBlob().getRadius() + yOffset / Math.abs(yOffset) * magicOffset);
                    }
                    shotOne.getBlob().setySpeed(shotOne.getBlob().getySpeed() - 40);
                }
                if (input.contains(new Input(Input.Action.SOUTH))) {
                    if (yOffset == 0) {
                         yOffset = (int)(current.getRadius() + shotOne.getBlob().getRadius() + magicOffset);
                    }
                    yOffset += (current.getRadius() + shotOne.getBlob().getRadius() + yOffset / Math.abs(yOffset) * magicOffset);
                    shotOne.getBlob().setySpeed(shotOne.getBlob().getySpeed() + 40);
                }
                shotOne.getBlob().getCenter().x += xOffset;
                shotOne.getBlob().getCenter().y += yOffset;

                shotBlobs.add(shotOne);

                current.setArea(current.getArea() - shotOne.getBlob().getArea());
            }
        }
        return shotBlobs;
    }

    public void updatePosition(Set<Input> input, long delta_t, int framerate, Dimension field) {
        double frametime = delta_t/(1000/framerate);
        //Retardation is now done for all blobs in AgarModel.
        /*
        for (Cluster cluster : clusters) {
            for (Blob blob : cluster.blobs) {
                blob.setxSpeed(blob.getxSpeed() * 3 / 4 * frametime);
                blob.setySpeed(blob.getySpeed() * 3 / 4 * frametime);
            }
        }
        */
        updateSpeed(input, frametime);
        move(frametime, field);
    }

    //Returns true if "blob" is eaten. Callers responsibility to remove said blob.
    public boolean collideAndEat(Blob blob){
        for (Cluster cluster : clusters) {
            for (Blob thisBlob : cluster.blobs) {
                if (thisBlob.getCenter().distance(blob.getCenter()) < thisBlob.getRadius() + blob.getRadius()) {
                    if ((thisBlob.getArea() / blob.getArea()) > (5 / 4)) {
                        thisBlob.setRadius(Math.sqrt((blob.getArea() + thisBlob.getArea()) / Math.PI));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void removeBlob (Blob blob) {
        Iterator<Cluster> clusterIterator = clusters.iterator();
        while (clusterIterator.hasNext()) {
            Cluster current = clusterIterator.next();
            if (current.contains(blob)) {
                current.remove(blob);
                if (current.isEmpty()) clusterIterator.remove();
            }
        }
    }

    public Iterator<Blob>getBlobIterator (){
        return new BlobIterator(clusters);
    }

    //Returns an arrayList of all blobs.
    public ArrayList<Blob>getBlobs () {
        ArrayList<Blob> resultBlobs = new ArrayList<Blob>();
        for (Cluster cluster : clusters) {
            for (Blob blob : cluster.blobs){
                resultBlobs.add(blob);
            }
        }
        return resultBlobs;
    }

    public boolean isEmpty(){
        return clusters.isEmpty();
    }

    //TODO: Test how all blobs in one AdvBlob react when stuffed together.
    /* Is responsible for boundaries when moving*/
    private void move(double frametime, Dimension field) {
        for (Cluster cluster : clusters) {
            for (Blob blob : cluster.blobs) {
                blob.getCenter().x += (int) (blob.getxSpeed() * frametime);
                blob.getCenter().y += (int) (blob.getySpeed() * frametime);

                if ((blob.getCenter().x + blob.getRadius()) > field.getWidth()) {
                    blob.getCenter().x = (int) (field.getWidth() - blob.getRadius());
                } else if (blob.getCenter().x - blob.getRadius() < 0) {
                    blob.getCenter().x = (int) (0 + blob.getRadius());
                }

                if ((blob.getCenter().y + blob.getRadius()) > field.getHeight()) {
                    blob.getCenter().y = (int) (field.getHeight() - blob.getRadius());
                } else if (blob.getCenter().y - blob.getRadius() < 0) {
                    blob.getCenter().y = (int) (0 + blob.getRadius());
                }

                //Collisiondetection for all "blobs" in AdvBlob after the move
                for (Blob collisionBlob : cluster.blobs) {
                    if (collisionBlob != blob) {
                        if ((blob.getRadius() < cluster.getLargestRadius()) && !blob.isClustered(collisionBlob)) {
                            Point collisionVector = getCollisionVector(blob, collisionBlob);
                            if (blob.collides(collisionBlob)) {
                                blob.getCenter().translate(collisionVector.x, collisionVector.y);
                            }else { //Blob is away from collisionBlob.
                                blob.getCenter().translate(-collisionVector.x, -collisionVector.y);
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateMaxSpeed() {
        for (Cluster cluster : clusters) {
            for (Blob blob : cluster.blobs) {
                blob.setMaxSpeed();
            }
        }
    }

    private void updateSpeed(Set<Input> input, double frametime) {
        updateMaxSpeed();
        //(0,0) top left corner

        if (input != null) {
            if (input.contains(new Input(Input.Action.EAST))){
                for (Cluster cluster : clusters) {
                    for (Blob blob : cluster.blobs) {
                        blob.setxSpeed(blob.getxSpeed() + blob.getMaxSpeed() / 10 * frametime);
                    }
                }
            }
            if (input.contains(new Input(Input.Action.WEST))){
                for (Cluster cluster : clusters) {
                    for (Blob blob : cluster.blobs) {
                        blob.setxSpeed(blob.getxSpeed() - blob.getMaxSpeed() / 10 * frametime);
                    }
                }
            }
            if (input.contains(new Input(Input.Action.NORTH))){
                for (Cluster cluster : clusters) {
                    for (Blob blob : cluster.blobs) {
                        blob.setySpeed(blob.getySpeed() - blob.getMaxSpeed() / 10 * frametime);
                    }
                }
            }
            if (input.contains(new Input(Input.Action.SOUTH))){
                for (Cluster cluster : clusters) {
                    for (Blob blob : cluster.blobs) {
                        blob.setySpeed(blob.getySpeed() + blob.getMaxSpeed() / 10 * frametime);
                    }
                }
            }
        }
        //Speed can't be greater than maxSpeed
        for (Cluster cluster : clusters) {
            for (Blob blob : cluster.blobs) {
                if (Math.abs(blob.getxSpeed()) > blob.getMaxSpeed()) {
                    blob.setxSpeed(blob.getxSpeed() / Math.abs(blob.getxSpeed()) * blob.getMaxSpeed());
                }
                if (Math.abs(blob.getySpeed()) > blob.getMaxSpeed()) {
                    blob.setySpeed(blob.getySpeed() / Math.abs(blob.getySpeed()) * blob.getMaxSpeed());
                }
            }
        }
    }

    //Returns a vector that subBlob is supposed to move to get close to domBlob. Everything is from domBlob's perspective.
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
}
