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
        spawnAdvBlobs();
        spawnAvatar();
    }

    public void compute(Set<Input> input, long delta_t) {
        if (avatar != null) avatar.updatePosition(input, delta_t, framerate, field);
        for(AdvBlob blob: advBlobs) {
            Set<Input> inputAI = null; //TODO: Make null into smart input (AI)
            blob.updatePosition(inputAI,  delta_t, framerate, field);
            findCollision(blob);
        }
        if (avatar != null) findCollision(avatar);
    }

    private void findCollision (AdvBlob blob) {
        //TODO: findCollision can have blobs target themselves, right now disabled for avatar. Make cooldown after split?
        //for (AdvBlob iterABlob: advBlobs) {
        for (int i = 0; advBlobs.size() > i; i++) {
            Iterator<Blob> blobIterator = advBlobs.get(i).getBlobIterator();
            while (blobIterator.hasNext()) {
                if (blob.collideAndEat(blobIterator.next())) {
                    blobIterator.remove();
                }
            }
            if (advBlobs.get(i).isEmpty()) {
                advBlobs.remove(advBlobs.get(i));
                i--;
            }
        }

        for (int i = 0; baseBlobs.size() > i; i++) {
            if (blob.collideAndEat(baseBlobs.get(i).getBlob())) {
                baseBlobs.remove(baseBlobs.get(i));
                i--;
            }
        }

        if (avatar != null && avatar != blob) {
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
            baseBlobs.add(blob);
        }
    }

    //Does NOT spawn Avatar.
    private void spawnAdvBlobs () {
        int maxAdvBlobs = 5; //TODO: Come up with genius way to decide this number
        int radius = 10;
        for (int i = 0; maxAdvBlobs > i; i++) {
            AdvBlob advBlob = new AdvBlob(generateSpawnPoint(radius), radius);
            advBlobs.add(advBlob);
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
        ArrayList<Blob> result = new ArrayList<Blob>();
        for (BaseBlob bB : baseBlobs) {
            result.add(bB.getBlob());
        }
        for (AdvBlob aB : advBlobs) {
            result.addAll(aB.getBlobs());
        }
        if (avatar != null) result.addAll(avatar.getBlobs());
        return result;
    }

    //Generates a random spawn point within field and not too close to other Blobs.
    private Point generateSpawnPoint(int radius) {
        Random rand = new Random();
        int testY;
        int testX;
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
            if (blob == null) return false;
            if (!(Math.abs(yCoord - blob.getCenter().getY()) <= radius + blob.getRadius()+10)) return false;
        }
        return true;
    }

    //Helpfunction to generateSpawnPoint
    private boolean isTooCloseX (int xCoord, int radius) {
        ArrayList<Blob> circles = circlesToDraw();
        if (circles.isEmpty()) return false;
        for (Blob blob : circles) {
            if (blob == null) return false;
            if (!(Math.abs(xCoord - blob.getCenter().getX()) <= radius + blob.getRadius()+10)) return false;
        }
        return true;
    }

    public String getMessage() {
        if (avatar == null) return "";
        return Double.toString(avatar.getBlobs().get(0).getMaxSpeed());
    }
}

//TODO: Removing while iterating over something. Not legally removing.