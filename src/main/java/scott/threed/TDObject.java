package scott.threed;

import java.util.LinkedList;
import java.util.List;

class TDObject extends CoardinateSystem {
    private List<TDObject> parts;
    private FaceSetCoards faceSet;
    private Vector pos;
    private Vector angle;

    public TDObject(FaceSetCoards faceSet, Vector pos, Vector angle) {
        this.faceSet = faceSet;
        this.pos = pos;
        this.angle = angle;
        this.parts = new LinkedList<TDObject>();
    }

    public void addPart(TDObject part) {
        parts.add( part );
    }

    public FaceSetCoards getFaceSet() {
        if (parts.isEmpty()) {
            return faceSet.extend();
        }
        else {
            CompositeFaceSetCoards cfs = new CompositeFaceSetCoards();
            cfs.add( faceSet.extend() );
            for (TDObject part: parts) {
                FaceSetCoards fs = part.getFaceSet();
                List<Vector> coards = fs.getCoards();
                transform(coards, part.getAngle());
                translate(coards, part.getPos());
                cfs.add(fs);
            }
            return cfs;
        }
    }

    public void setPos(Vector pos) {
        this.pos = pos;
    }

    public Vector getPos() {
        return pos;
    }

    public void setAngle(Vector angle) {
        this.angle = angle;
    }

    public Vector getAngle() {
        return angle;
    }
}