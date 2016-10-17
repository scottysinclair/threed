package scott.threed;

import java.util.List;

/**
 * A set of faces with corresponding coards.
 */
interface FaceSetCoards extends FaceSet {
    public List<FaceCoards> getFaceCoards();
    public List<Vector> getCoards();
    public FaceSetCoards extend();
}