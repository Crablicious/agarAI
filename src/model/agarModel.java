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

    private void findCollision (AdvBlob blob) {
        for (AdvBlob iterABlob: advBlobs) {
            if (blob.collides(iterABlob)) {
                if (blob.eatBlob(iterABlob)) {
                    advBlobs.remove(iterABlob);
                }
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
        //TODO: Maybe needs to space them out so they won't spawn at the same place.
        // Density of base blobs is 50 pixels per base blob
        int maxBaseBlobs = 10;//TODO: (int)(field.getHeight() * field.getWidth() / 50);
        Random rand = new Random();
        int radius = 10;
        for (int i = 0; maxBaseBlobs > i; i++) {
            //Compensated to stay within field boundary.
            int y = rand.nextInt((int)field.getHeight()+1-2*radius) + radius;
            int x = rand.nextInt((int)field.getWidth()+1-2*radius) + radius;
            BaseBlob blob = new BaseBlob(new Point(x,y), radius);
            baseBlobs.add(blob);
        }
    }

    private void spawnAvatar () {
        int radius = 20;
        Random rand = new Random();
        int y = rand.nextInt((int)field.getHeight()+1-2*radius) + radius;
        int x = rand.nextInt((int)field.getWidth()+1-2*radius) + radius;

        avatar = new AdvBlob(new Point(x,y), radius);
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
}