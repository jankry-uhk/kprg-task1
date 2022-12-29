package model;

import java.util.ArrayList;

public class Polygon {

    public ArrayList points = new ArrayList<>();

    /**
     * Add point to point list
     * @param point
     */
    public void addPoint(Point point) {
        points.add(point);
    }

    /**
     * Get first added point from point list
     * @return Point first item of list
     */
    public Point getFirstPoint() {
        return (Point)points.get(0);
    }

    /**
     * Get last added point from point list
     * @return Point last added item
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

    public Point getPointByIndex(int index) {
        return (Point) points.get(index);
    }

    /**
     * Get list size
     * @return int size
     */
    public int getSize() {
        return points.size();
    }

    /**
     * Clear all points
     */
    public void clear() {
        points.clear();
    }
}
