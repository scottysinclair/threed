package scott.threed;

import java.util.LinkedList;
import java.util.List;

class FaceSetCoardsBase implements FaceSetCoards {
    private FaceSet faceSet;
    private List<Vector> coards;

    public FaceSetCoardsBase(FaceSet faceset, List<Vector> coards) {
        this.faceSet = faceset;
        this.coards = coards;
    }

    public List<Face> getFaces() {
        return faceSet.getFaces();
    }

    public List<Vector> getCoards() {
        return coards;
    }

    public List<FaceCoards> getFaceCoards() {
        List<FaceCoards> list = new LinkedList<FaceCoards>();
        for (Face face: faceSet.getFaces()) {
            list.add(new FaceCoards(face, coards));
        }
        return list;
    }

    public FaceSetCoardsBase extend() {
        List<Vector> copyCoards = new LinkedList<Vector>();
        for (Vector coard: coards) {
            copyCoards.add( new Vector( coard ) );
        }
        return new FaceSetCoardsBase(this, copyCoards);
    }

}