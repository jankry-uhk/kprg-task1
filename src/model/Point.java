package model;

public class Point {

    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Get X
     * @return {int} x
     */
    public int getX() {
        return (int) this.x;
    }

    /**
     * Get Y
     * @return {int} x
     */
    public int getY() {
        return (int) this.y;
    }
}
