package model;

public class Camera {

    private static final int MAX_ANGLE = 360;

    private double angle;
    private int x, y;

    public Camera() {
        angle = 0;
        x = (int) (Floormap.WIDTH / 2.0);
        y = (int) (Floormap.HEIGHT / 2.0);
    }

    public double getAngle() {
        return angle;
    }

    public void rotate(double degrees) {
        angle += degrees;
        if (angle < 0) {
            angle += MAX_ANGLE;
        }
        angle %= MAX_ANGLE;
    }

    public void set(int angle) {
        this.angle = angle;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int posX(int step) {
        return x * step;
    }

    public int posY(int step) {
        return y * step;
    }
}
