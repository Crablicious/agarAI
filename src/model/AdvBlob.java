package model;

import java.awt.*;


public class AdvBlob extends BaseBlob {
	private double maxSpeed;
	private double westSpeed;
	private double eastSpeed;
	private double northSpeed;
	private double southSpeed;

	public AdvBlob(Point blobCenterPoint, int radius) {
        super(blobCenterPoint, radius);
        calculateMaxSpeed();
        westSpeed = 0;
        eastSpeed = 0;
        northSpeed = 0;
        southSpeed = 0;
	}

    public void calculateMaxSpeed() {
        maxSpeed = 10 - getArea()/300;
    }

	public void updateSpeed(Input input) {
        calculateMaxSpeed();

        if (input.contains(Input.Dir.EAST)){
            eastSpeed = maxSpeed;
        }
        if (input.contains(Input.Dir.WEST)){
            westSpeed = maxSpeed;
        }
        if (input.contains(Input.Dir.NORTH)){
            northSpeed = maxSpeed;
        }
        if (input.contains(Input.Dir.SOUTH)){
            southSpeed = maxSpeed;
        }
	}

    /* Is responsible for boundaries */
    public void move(long frameTime, Dimension field) {
        blobCenterPoint.x += (eastSpeed-westSpeed) * frameTime;
        blobCenterPoint.y += (northSpeed-southSpeed) * frameTime;

        if ((blobCenterPoint.x + radius) > field.getWidth()) {
            blobCenterPoint.x = (int)(field.getWidth() - radius);
        }else if (blobCenterPoint.x - radius < 0) {
            blobCenterPoint.x = (int)(0 + radius);
        }

        if ((blobCenterPoint.y + radius) > field.getHeight()) {
            blobCenterPoint.y = (int)(field.getHeight() - radius);
        }else if (blobCenterPoint.y - radius < 0) {
            blobCenterPoint.y = (int)(0 + radius);
        }
    }

    public void updatePosition(Input input, long delta_t, int framerate, Dimension field) {
        //Retardation
        eastSpeed = eastSpeed - (eastSpeed*3/4) * delta_t/(1/framerate);
        westSpeed = westSpeed - (westSpeed*3/4) * delta_t/(1/framerate);
        northSpeed = northSpeed - (northSpeed*3/4) * delta_t/(1/framerate);
        southSpeed = southSpeed - (southSpeed*3/4) * delta_t/(1/framerate);

        updateSpeed(input);
        move(delta_t/(1/framerate), field);
    }
}
