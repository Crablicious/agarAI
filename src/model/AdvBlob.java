package model;

import java.awt.*;
import java.util.Set;

//TODO: Add ability to split.
public class AdvBlob extends BaseBlob {
	private double maxSpeed;
	private double xSpeed;
    private double ySpeed;

	public AdvBlob(Point blobCenterPoint, int radius) {
        super(blobCenterPoint, radius);
        calculateMaxSpeed();
        xSpeed = 0;
        ySpeed = 0;
	}

    public void calculateMaxSpeed() {
        maxSpeed = 10; //TODO: daw- getArea()/300;
    }

	public void updateSpeed(Set<Input> input, double frametime) {
        calculateMaxSpeed();
        //TODO: Might need some acceleration instead of constant setting.

        //(0,0) top left corner
        if (input.contains(new Input(Input.Dir.EAST))){
            xSpeed += maxSpeed/10 * frametime;
        }
        if (input.contains(new Input(Input.Dir.WEST))){
            xSpeed -= maxSpeed/10 * frametime;
        }
        if (input.contains(new Input(Input.Dir.NORTH))){
            ySpeed -= maxSpeed/10 * frametime;
        }
        if (input.contains(new Input(Input.Dir.SOUTH))){
            ySpeed += maxSpeed/10 * frametime;
        }

        //Speed can't be greater than maxSpeed
        if (Math.abs(xSpeed) > maxSpeed) {
            xSpeed = xSpeed/Math.abs(xSpeed)*maxSpeed;
        }
        if (Math.abs(ySpeed) > maxSpeed) {
            ySpeed = ySpeed/Math.abs(ySpeed)*maxSpeed;
        }
	}

    /* Is responsible for boundaries */
    public void move(double frametime, Dimension field) {
        blobCenterPoint.x += (xSpeed * frametime);
        blobCenterPoint.y += (ySpeed * frametime);

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

    public void updatePosition(Set<Input> input, long delta_t, int framerate, Dimension field) {
        double frametime = delta_t/(1000/framerate);
        //Retardation
        //TODO: Why is avatar retarding different on - and + sides?
        xSpeed = xSpeed * 0.75 * frametime;
        ySpeed = ySpeed * 0.75 * frametime;

        if (input != null) updateSpeed(input, frametime);
        move(frametime, field);
        System.out.println(xSpeed);
        //System.out.println(ySpeed);
    }

    public boolean eatBlob(BaseBlob toBeEaten){
        if (this.getArea()/toBeEaten.getArea() > 4/3){
            this.radius = Math.sqrt((toBeEaten.getArea()+this.getArea())/Math.PI);
            return true;
        }
        return false;
    }
}
