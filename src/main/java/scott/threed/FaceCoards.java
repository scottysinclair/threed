package scott.threed;

import java.util.List;

/*
  A face and it's coardinates
*/
class FaceCoards {
    private Face face;
    private List<Vector> coards;

    public FaceCoards(Face face, List<Vector> coards) {
        this.face = face;
        this.coards = coards;
    }

    public List<Vector> getCoards() {
        return coards;
    }

    public int size() {
        return face.size();
    }

    public Vector getCoard(int pos) {
        return face.getCoard(pos, coards);
    }

}