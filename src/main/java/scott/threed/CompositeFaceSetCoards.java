package scott.threed;

import java.util.LinkedList;
import java.util.List;

/**
 * A set of faces and coardinates
 * made up of other sets of faces and coardinates
 */
class CompositeFaceSetCoards implements FaceSetCoards {
    private List<FaceSetCoards> faceSets;

    public CompositeFaceSetCoards() {
        this.faceSets = new LinkedList<FaceSetCoards>();
    }

    public void add(FaceSetCoards faceSet) {
        faceSets.add( faceSet );
    }

    public List<FaceCoards> getFaceCoards() {
        List<FaceCoards> list = new LinkedList<FaceCoards>();
        for (FaceSetCoards fs: faceSets) {
            list.addAll(fs.getFaceCoards());
        }
        return list;
    }

    public List<Vector> getCoards() {
        List<Vector> list = new LinkedList<Vector>();
        for (FaceSetCoards fs: faceSets) {
            list.addAll( fs.getCoards() );
        }
        return list;
    }

    public List<Face> getFaces() {
        List<Face> list = new LinkedList<Face>();
        for (FaceSetCoards fs: faceSets) {
            list.addAll( fs.getFaces() );
        }
        return list;
    }

    public FaceSetCoards extend() {
        CompositeFaceSetCoards cfs = new CompositeFaceSetCoards();
        for (FaceSetCoards fs: faceSets) {
            cfs.add(fs.extend());
        }
        return cfs;
    }

}