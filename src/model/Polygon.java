package model;

import java.util.ArrayList;

public class Polygon {

    ArrayList points = new ArrayList<>();

    /**
     * Add point to point list
     */
    public void addPoint(Point point) {
        points.add(point);
    }
    /**
     * Get first added point from point list
     */
    public Point getFirstPoint() {
        return (Point)points.get(0);
    }
    /**
     * Get last added point from point list
     */
    public Point getLastAdded() {
        return (Point)points.get(points.size() - 1);
    }
    /**
     * Remove all points for point list
     */
    public void deletePoints() {
        points.clear();
    }
}
