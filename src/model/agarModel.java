package model;

import java.awt.*;
import java.util.ArrayList;
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
        spawnAvatar();
    }

    public void compute(Set<Input> input, long delta_t) {
        avatar.updatePosition(input, delta_t, framerate, field);
        for(AdvBlob blob: advBlobs) {
            Set<Input> inputAI = null;
            blob.updatePosition(inputAI,  delta_t, framerate, field);
            findCollision(blob);
        }
        findCollision(avatar);
    }

    //TODO: Make this method work. Current problem: collideAndEat, what argument?
    //TODO: Two methods maybe? AdvBlob does not belong to Blob. Argument: ArrayList<Blob> after BaseBlob fix.
    private void findCollision (AdvBlob blob) {
        for (AdvBlob iterABlob: advBlobs) {
            if (blob.collideAndEat(iterABlob)) {
                advBlobs.remove(iterABlob);
            }
        }

        for (BaseBlob iterBBlob: baseBlobs){
            if (blob.collides(iterBBlob)) {
                if (blob.eatBlob(iterBBlob)) {
                    baseBlobs.remove(iterBBlob);
                }
            }
        }

        if (blob.collides(avatar)) {
            if (blob.eatBlob(avatar)) {
                //TODO: eat avatar
            }
        }
    }

    private void spawnBaseBlobs () {
        int maxBaseBlobs = (int)(field.getHeight() * field.getWidth() / 10000);
        Random rand = new Random();
        int radius = 5;
        for (int i = 0; maxBaseBlobs > i; i++) {
            //Compensated to stay within field boundary.
            BaseBlob blob = new BaseBlob(generateSpawnPoint(radius), radius);
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

    public ArrayList<BaseBlob> circlesToDraw () {
        ArrayList<BaseBlob> result = new ArrayList<BaseBlob>();
        result.addAll(baseBlobs);
        result.addAll(advBlobs);
        result.add(avatar);
        return result;
    }

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


    private boolean isTooCloseY (int yCoord, int radius) {
        for (BaseBlob blob : circlesToDraw()) {
            if (blob == null) return false;
            if (!(Math.abs(yCoord - blob.getBlobCenterPoint().getY()) >= radius + blob.getRadius()+10)) return false;
        }
        return true;
    }

    private boolean isTooCloseX (int xCoord, int radius) {
        for (BaseBlob blob : circlesToDraw()) {
            if (blob == null) return false;
            if (!(Math.abs(xCoord - blob.getBlobCenterPoint().getX()) >= radius + blob.getRadius()+10)) return false;
        }
        return true;
    }

    public String getMessage() {
        return Double.toString(avatar.getMaxSpeed());
    }
}