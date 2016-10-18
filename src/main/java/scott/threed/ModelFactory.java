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


    public static TDObject wheel(int radius, int thickness, int nPoints, Vector pos, Vector angle) {


        //front + back + side segments
        int numberOfFaces = 2 + nPoints;
        //int numberOfFaces = 2;

        //front face
        int faces[][] = new int[numberOfFaces][];
        faces[0] = new int[nPoints];
        for (int i=0; i<nPoints; i++) {
            faces[0][i] = i;
        }

        //back face
        faces[1] = new int[nPoints];
        for (int i=0; i<nPoints; i++) {
            faces[1][i] = nPoints + i;
        }

        //side faces going around the side of the circle
        {
            for (int i=2; i<numberOfFaces; i++) {
                faces[i] = new int[4];
                if (i < numberOfFaces-1) {
                    faces[i][0] = nPoints + (i-2);
                    faces[i][1] = nPoints + 1 + (i-2);
                    faces[i][2] = 1 + (i-2);
                    faces[i][3] = 0 + (i-2);
                }
                else {
                    faces[i][0] = nPoints;
                    faces[i][1] = nPoints + (i-2);
                    faces[i][2] = 0 + (i-2);
                    faces[i][3] = 0;
                }

            }
        }
        FaceSetBase faceSet = new FaceSetBase(faces);


        List<Vector> points = new LinkedList<Vector>();
        //front face points
        {
            float degreesPerSegment = 360f / nPoints;
            float bangle = 0;
            while (bangle < 360f) {
                double x = Math.sin(CoardinateSystem.toRadians(bangle)) * radius;
                double y = Math.cos(CoardinateSystem.toRadians(bangle)) *  radius;
                points.add(vector((int)Math.round(x), (int)Math.round(y), thickness));
                bangle += degreesPerSegment;
            }
        }
        //back face points
        {
            float degreesPerSegment = 360f / nPoints;
            float bangle = 0;
            while (bangle < 360f) {
                double x = Math.sin(CoardinateSystem.toRadians(bangle)) * radius;
                double y = Math.cos(CoardinateSystem.toRadians(bangle)) *  radius;
                points.add(vector((int)Math.round(x), (int)Math.round(y), -thickness));
                bangle += degreesPerSegment;
            }
        }


        FaceSetCoardsBase base = new FaceSetCoardsBase(faceSet, points);
        return new TDObject(base, pos, angle);
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