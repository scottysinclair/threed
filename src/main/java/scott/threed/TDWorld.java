package scott.threed;

import java.util.LinkedList;
import java.util.List;

class TDWorld extends CoardinateSystem {
    private List<TDObject> objects;
    public TDWorld() {
        objects = new LinkedList<TDObject>();
    }

    public void add(TDObject object) {
        objects.add( object );
    }

    public FaceSetCoards getFaceSet() {
        CompositeFaceSetCoards cfs = new CompositeFaceSetCoards();
        for (TDObject object: objects) {
            FaceSetCoards faceSet = object.getFaceSet();
            List<Vector> coards = faceSet.getCoards();
            transform(coards, object.getAngle());
            translate(coards, object.getPos());
            cfs.add(faceSet);
        }
        return cfs.extend(); //extend to keep a copy of the coards
    }
}