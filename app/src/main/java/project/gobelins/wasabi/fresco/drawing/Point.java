package project.gobelins.wasabi.fresco.drawing;

/**
 * Created by thiron on 27/02/2015.
 */
public class Point {
    public float x, y;
    public float dx, dy;

    @Override
    public String toString() {
        return x + ", " + y;
    }

    public void set(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void set(double x, double y)
    {
        this.x = (float) x;
        this.y = (float) y;
    }
}