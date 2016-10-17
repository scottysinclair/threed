package scott.threed;

import java.util.List;

/**
 * Set of points
 */
class Face {
    private int points[];
    public Face(int[] points) {
        this.points = points;
    }
    public int size() {
        return points.length;
    }
    public Vector getCoard(int pos, List<Vector> coards) {
        return coards.get( points[ pos ] );
    }
}