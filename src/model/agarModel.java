package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
//implement AgarModel

public class AgarModel {
    private AdvBlob avatar;
	private ArrayList<BaseBlob> baseBlobs;
	private ArrayList<AdvBlob> advBlobs;
    private Dimension field;
    private int framerate;

    public AgarModel(int framerate) {
        this.field = new Dimension(1024, 768);
        this.framerate = framerate;
        baseBlobs = new ArrayList<BaseBlob>();
        advBlobs = new ArrayList<AdvBlob>();
        spawnBaseBlobs();
        //System.out.println("HIT 8"); //TODO 8
        //spawnAvatar();
    }

    public void compute(Set<Input> input, long delta_t) {
        avatar.updatePosition(input, delta_t, framerate, field);
        for(AdvBlob blob: advBlobs) {
            Set<Input> inputAI = null; //TODO: Make null into smart input (AI)
            blob.updatePosition(inputAI,  delta_t, framerate, field);
            findCollision(blob);
        }
        findCollision(avatar);
    }

    private void findCollision (AdvBlob blob) {
        //for (AdvBlob iterABlob: advBlobs) {
        for (int i = 0; advBlobs.size() > i; i++) {
            Iterator<Blob> blobIterator = advBlobs.get(i).getBlobIterator();
            while (blobIterator.hasNext()) {
                if (blob.collideAndEat(blobIterator.next())) {
                    blobIterator.remove();
                }
            }
            if (advBlobs.get(i).isEmpty()) advBlobs.remove(advBlobs.get(i));
            i--;
        }

        for (int i = 0; baseBlobs.size() > i; i++) {
            if (blob.collideAndEat(baseBlobs.get(i).getBlob())) {
                baseBlobs.remove(baseBlobs.get(i));
                i--;
            }
        }

        if (avatar != null) {
            Iterator<Blob> avatarIterator = avatar.getBlobIterator();
            while (avatarIterator.hasNext()) {
                if (blob.collideAndEat(avatarIterator.next())) {
                    avatarIterator.remove();
                }
            }
            if (avatar.isEmpty()) avatar = null;
        }
    }

    private void spawnBaseBlobs () {
        int maxBaseBlobs = (int)(field.getHeight() * field.getWidth() / 10000);
        int radius = 5;
        for (int i = 0; maxBaseBlobs > i; i++) {
            //Compensated to stay within field boundary.
            BaseBlob blob = new BaseBlob(generateSpawnPoint(radius), radius);
            //System.out.println("HIT 5"); //TODO 5
            baseBlobs.add(blob);
        }
    }

    private void spawnAvatar () {
        int radius = 10;
        avatar = new AdvBlob(generateSpawnPoint(radius), radius);
    }

    public Dimension getFieldSize() {
        return field;
    }

    public ArrayList<Blob> circlesToDraw () {
        //System.out.println("HIT 2"); //TODO: 2
        ArrayList<Blob> result = new ArrayList<Blob>();
        for (BaseBlob bB : baseBlobs) {
            //System.out.println("HIT 6"); //TODO 6
            result.add(bB.getBlob());
        }
        for (AdvBlob aB : advBlobs) {
            //System.out.println("HIT 6"); //TODO 7
            result.addAll(aB.getBlobs());
        }
        if (avatar != null) result.addAll(avatar.getBlobs());
        //System.out.println("HIT 3"); //TODO: 3
        return result;
    }

    //Generates a random spawn point within field and not too close to other Blobs.
    private Point generateSpawnPoint(int radius) {
        Random rand = new Random();
        int testY;
        int testX;
        //System.out.println("Hit 1"); //TODO: 1
        do {
            testY = rand.nextInt((int)field.getHeight()+1-2*radius) + radius;
        } while (isTooCloseY(testY, radius));

        do {
            testX = rand.nextInt((int)field.getWidth()+1-2*radius) + radius;
        } while (isTooCloseX(testX, radius));
        return new Point(testX, testY);
    }

    //Helpfunction to generateSpawnPoint
    private boolean isTooCloseY (int yCoord, int radius) {
        ArrayList<Blob> circles = circlesToDraw();
        if (circles.isEmpty()) return false;

        for (Blob blob : circles) {
            //System.out.println("HIT 10"); //TODO 10
            if (blob == null) return false;
            if (!(Math.abs(yCoord - blob.getCenter().getY()) >= radius + blob.getRadius()+10)) return false;
        }
        return true;
    }

    //Helpfunction to generateSpawnPoint
    private boolean isTooCloseX (int xCoord, int radius) {
        ArrayList<Blob> circles = circlesToDraw();
        if (circles.isEmpty()) return false;

        for (Blob blob : circles) {
            //System.out.println("HIT 9"); //TODO 9
            if (blob == null) return false;
            if (!(Math.abs(xCoord - blob.getCenter().getX()) >= radius + blob.getRadius()+10)) return false;
        }
        return true;
    }

    public String getMessage() {
        return Double.toString(avatar.getMaxSpeed());
    }
}

//TODO: Removing while iterating over something. Not legally removing.