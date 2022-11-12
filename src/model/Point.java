package model;

public class Point {

    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(double x, double y) {
        this.x = (int) Math.round(x);
        this.y = (int) Math.round(y);
    }
    /**
     * Get X
     * @return {int} x
     */
    public int getX() {
        return (int) Math.round(this.x);
    }

    /**
     * Get Y
     * @return {int} x
     */
    public int getY() {
        return (int) Math.round(this.y);
    }
}
