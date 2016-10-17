package scott.threed;

import java.util.LinkedList;
import java.util.List;

class ModelFactory  {
    public static Vector vector(int x, int y, int z) {
        return new Vector((float)x, (float)y, (float)z);
    }


    public static TDObject plane(int length, Vector pos, Vector angle) {
        FaceSetBase face = new FaceSetBase(new int[][]{{0, 1, 2, 3}});
        List<Vector> points = new LinkedList<Vector>();
        points.add(vector(-length, length, 0));
        points.add(vector(length, length, 0));
        points.add(vector(-length, -length, 0));
        points.add(vector(-length, -length, 0));
        FaceSetCoardsBase plane = new FaceSetCoardsBase(face, points);
        return new TDObject(plane, pos, angle);
    }

    public static TDObject rect(int lengthx, int lengthy, int lengthz, Vector pos, Vector angle) {
        FaceSetBase cubeFaces = new FaceSetBase(new int[][]{
                {0, 1, 2, 3}, //f
                {5, 4, 7, 6}, //back
                {4, 0, 3, 7}, //l
                {1, 5, 6, 2}, //r
                {4, 5, 1, 0}, //t
                {3, 2, 6, 7} //bottom

        });

        List<Vector> cubePoints = new LinkedList<Vector>();
        cubePoints.add(vector(-lengthx, lengthy, -lengthz));
        cubePoints.add(vector(lengthx, lengthy, -lengthz));
        cubePoints.add(vector(lengthx, -lengthy, -lengthz));
        cubePoints.add(vector(-lengthx, -lengthy, -lengthz));

        cubePoints.add(vector(-lengthx, lengthy, lengthz));
        cubePoints.add(vector(lengthx, lengthy, lengthz));
        cubePoints.add(vector(lengthx, -lengthy, lengthz));
        cubePoints.add(vector(-lengthx, -lengthy, lengthz));
        FaceSetCoardsBase cube = new FaceSetCoardsBase(cubeFaces, cubePoints);
        return new TDObject(cube, pos, angle);
    }

    public static TDObject cube(int length, Vector pos, Vector angle) {
        FaceSetBase cubeFaces = new FaceSetBase(new int[][]{
                {0, 1, 2, 3}, //f
                {5, 4, 7, 6}, //back
                {4, 0, 3, 7}, //l
                {1, 5, 6, 2}, //r
                {4, 5, 1, 0}, //t
                {3, 2, 6, 7} //bottom

        });

        List<Vector> cubePoints = new LinkedList<Vector>();
        cubePoints.add(vector(-length, length, -length));
        cubePoints.add(vector(length, length, -length));
        cubePoints.add(vector(length, -length, -length));
        cubePoints.add(vector(-length, -length, -length));

        cubePoints.add(vector(-length, length, length));
        cubePoints.add(vector(length, length, length));
        cubePoints.add(vector(length, -length, length));
        cubePoints.add(vector(-length, -length, length));
        FaceSetCoardsBase cube = new FaceSetCoardsBase(cubeFaces, cubePoints);
        return new TDObject(cube, pos, angle);
    }
}