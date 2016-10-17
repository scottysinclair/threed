package scott.threed;

import java.awt.Graphics;
import java.util.List;

class TDView extends CoardinateSystem {
    private Vector pos;
    private Vector angle;
    private float fov;
    private int screenWidth;
    private int screenHeight;
    private TDWorld world;

    public TDView(Vector pos, Vector angle, TDWorld world) {
        this.pos = pos;
        this.angle = angle;
        this.world = world;
    }

    public void initView(float fov,int screenWidth, int screenHeight) {
        this.fov = fov;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }


    public float getFov() {
        return fov;
    }

    public void setFov(float fov) {
        this.fov = fov;
    }

    public Vector getPos() {
        return pos;
    }

    public Vector getAngle() {
        return angle;
    }

    public FaceSetCoards getFaceSet() {
        FaceSetCoards faceSet = world.getFaceSet();
        List<Vector> coards = faceSet.getCoards();
        translate(coards, pos);
        transform(coards, angle);
        return faceSet;
    }

    public void render(Graphics g) {
        FaceSetCoards faceSet = getFaceSet();
        for (Vector coard: faceSet.getCoards()) {
            double x = coard.getX();
            double y = coard.getY();

            //perform the perspective
            double viewHalfLength = Math.tan(fov / 2.0) * coard.getZ();
            double ratioX = viewHalfLength / x; //ratio of point x coard to edge of view
            double ratioY = viewHalfLength / y; //ratio of point y coard to edge of view

            x = (screenWidth / ratioX);
            y = (screenHeight / ratioY);

            x += (screenWidth / 2);
            y += (screenHeight / 2);

            coard.setX( (float)x );
            coard.setY( (float)y );
        }

        for (FaceCoards face: faceSet.getFaceCoards()) {
            drawFace(g, face);
        }
    }

    private void drawFace(Graphics g, FaceCoards face) {
         Vector surfaceNormal = calculateSurfaceNormal(face);
         if (surfaceNormal.getZ() >= 0) {
            return;
         }

        for (int i=0, n=face.size(); i<n; i++) {
            Vector a = face.getCoard(i);
            Vector b = face.getCoard((i+1) % n);
            if (a.getZ() > 0 && b.getZ() > 0) {
//                System.out.println(
//                            Math.round(a.getX()) + " " +
//                            Math.round(a.getY()) + " " +
//                            Math.round(b.getX()) + " " +
//                            Math.round(b.getY()));
                g.drawLine(
                        Math.round(a.getX()),
                        Math.round(a.getY()),
                        Math.round(b.getX()),
                        Math.round(b.getY()));

            }
        }
    }
}