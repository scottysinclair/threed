package scott.threed;

import java.util.LinkedList;
import java.util.List;

class FaceSetBase implements FaceSet {
    private List<Face> faces;
    public FaceSetBase(int faceData[][]) {
        faces = new LinkedList<Face>();
        for (int points[]: faceData) {
            faces.add( new Face( points ) );
        }
    }

    public FaceSetBase(List<Face> faces) {
        this.faces = faces;
    }

    public List<Face> getFaces() {
        return faces;
    }
}