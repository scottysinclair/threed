package scott.threed;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

/**
3d value
*/
class Vector {
    private float x;
    private float y;
    private float z;

    public Vector(Vector v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector() {}

    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }

    public void addX(float value) {
        this.x += value;
    }

    public void addY(float value) {
        this.y += value;
    }

    public void addZ(float value) {
        this.z += value;
    }

    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public float getZ() {
        return z;
    }
    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "Vector [x=" + x + ", y=" + y + ", z=" + z + "]";
    }
}

/**
 * Set of points
 */
class Face {
    private int points[];
    public Face(int[] points) {
        this.points = points;
    }
    public int size() {
        return points.length;
    }
    public Vector getCoard(int pos, List<Vector> coards) {
        return coards.get( points[ pos ] );
    }
}

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

/**
 * A set of faces
 */
interface FaceSet {
    public List<Face> getFaces();
}

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

/**
 * A set of faces with corresponding coards.
 */
interface FaceSetCoards extends FaceSet {
    public List<FaceCoards> getFaceCoards();
    public List<Vector> getCoards();
    public FaceSetCoards extend();
}

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



/**
 * Performs calculations, transforms / translates to different
 * coard systems.
 */
class CoardinateSystem {
    public Vector negate(Vector p) {
        return new Vector(p.getX() * -1f, p.getY() * -1f, p.getZ() * -1f);
    }
    public void transform(List<Vector> coards, Vector angle) {
        for (Vector coard: coards) {
            transform(coard, angle);
        }
    }

    private float toRadians(float angle) {
        return 0.0174532925F * angle;
    }

    public void transform(Vector coard, Vector angle) {
        //x and y points rotating around z axis
        float az = toRadians(angle.getZ());
        float pxx = (float)Math.cos(az) * coard.getX();
        float pxy = (float)Math.sin(az) * coard.getX();

        float pyx = (float)Math.sin(az) * coard.getY();
        float pyy = (float)Math.cos(az) * coard.getY();

        coard.setX(pxx + pyx);
        coard.setY(pyy - pxy);

        //x and z points rotating around y axis
        float ay = toRadians(angle.getY());
        pxx = (float)Math.cos(ay) * coard.getX();
        float pxz = (float)Math.sin(ay) * coard.getX();

        float pzx = (float)Math.sin(ay) * coard.getZ();
        float pzz = (float)Math.cos(ay) * coard.getZ();

        coard.setX(pxx + pzx);
        coard.setZ(pzz - pxz);

        //z and y points rotating around x axis
        float ax = toRadians(angle.getX());
        pzz = (float)Math.cos(ax) * coard.getZ();
        float pzy = (float)Math.sin(ax) * coard.getZ();

        float pyz = (float)Math.sin(ax) * coard.getY();
        pyy = (float)Math.cos(ax) * coard.getY();

        coard.setZ(pzz + pyz);
        coard.setY(pyy - pzy);
    }

    public void translate(List<Vector> coards, Vector pos) {
        for (Vector coard: coards) {
            translate(coard, pos);
        }
    }

    public Vector calculateSurfaceNormal(FaceCoards face) {
      Vector normal = new Vector(0f, 0f, 0f);

      for (int i=0, n=face.size(); i<n; i++) {
         Vector vCurrent = face.getCoard(i);
         Vector vNext = face.getCoard((i + 1) % n);

         normal.addX(  (vCurrent.getY() - vNext.getY()) *  (vCurrent.getZ() + vNext.getZ())   );
         normal.addY(  (vCurrent.getZ() - vNext.getZ()) *  (vCurrent.getX() + vNext.getX())  );
         normal.addZ(  (vCurrent.getX() - vNext.getX()) *  (vCurrent.getY() + vNext.getY())  );
      }
      return normalize(normal);
    }

    public Vector normalize(Vector vector) {
        //L = math.sqrt(V[0] * V[0] + V[1] * V[1] + V[2] * V[2])
        //V = (V[0] / L, V[1] / L, V[2] / L)
          double l = Math.sqrt((vector.getX() *  vector.getX()) + (vector.getY() * vector.getY()) + (vector.getZ() * vector.getZ()));
          return new Vector((float)(vector.getX() / l), (float)(vector.getY() / l), (float)(vector.getZ() / l));
        }


    public void translate(Vector coard, Vector pos) {
        coard.setX(coard.getX() + pos.getX());
        coard.setY(coard.getY() + pos.getY());
        coard.setZ(coard.getZ() + pos.getZ());
    }
}


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

public class Threed extends ModelFactory {
    public static void main(String args[]) {
        try {
            final int width = 1920;
            final int height = 1080;
            TDWorld world = new TDWorld();
            final TDView view = new TDView(vector(0, 0, 0), vector(0, 0, 0), world);
            view.initView(35.5f, width, height);
            view.getPos().setY(-300f);
            view.getPos().setZ(3000f);

            TDObject floor = plane(100000, vector(0, 0, 0), vector(90, 0, 0));
            world.add( floor );

            TDObject center = cube(200, vector(0, 950, 0), vector(0, 0, 0));
            TDObject rbase = rect(50, 750, 50, vector(0, 550, -700), vector(0, 0, 0));
            rbase.addPart(center);
//			TDObject rbase = center;

            TDObject r1 = rect(10, 1000, 10, vector(0, 0, 0), vector(0, 0, 0));
            TDObject r2 = rect(10, 1000, 10, vector(0, 0, 0), vector(90, 0, 0));

            center.addPart(r1);
            center.addPart(r2);

            TDObject cube1 = cube(80, vector(0, 1000, 0), vector(0, 0, 0));
            TDObject cube2 = cube(80, vector(0, -1000, 0), vector(0, 0, 0));
            r1.addPart(cube1);
            r1.addPart(cube2);

            TDObject cube3 = cube(80, vector(0, 1000, 0), vector(0, 0, 0));
            TDObject cube4 = cube(80, vector(0, -1000, 0), vector(0, 0, 0));
            r2.addPart(cube3);
            r2.addPart(cube4);

            world.add(rbase);


            TDObject cubea = cube(100, vector(1700, 0, -1700), vector(0, 0, 0));
            TDObject cubeb = cube(100, vector(0, 0, -1700), vector(0, 0, 0));
            TDObject cubec = cube(100, vector(-1700, 0, -1700), vector(0, 0, 0));
            TDObject cubed = cube(100, vector(0, 0, -400), vector(0, 0, 0));
            world.add(cubea);
            world.add(cubeb);
            world.add(cubec);
            world.add(cubed);

            WindowControl wc = new WindowControl(width, height, view);

            // Create a window for full-screen mode; add a button to leave full-screen mode
            try {
                while(true) {
                    Graphics g = wc.getGraphics();
                    g.setColor(Color.white);
                    g.fillRect(0, 0, wc.getWidth(), wc.getHeight());

                    g.setColor(Color.BLACK);

                    view.render(g);

                    g.dispose();
                    wc.show();

                    Thread.sleep(40);

                    rbase.getAngle().addY(1);
                    r1.getAngle().addX(3);
                    r2.getAngle().addX(3);
                    /*

                    cube1.getAngle().addX(3);
                    cube2.getAngle().addX(3);
                    cube3.getAngle().addX(3);
                    cube4.getAngle().addX(3);

                    cube1.getAngle().addY(10);
                    cube2.getAngle().addY(10);
                    cube3.getAngle().addZ(10);
                    cube4.getAngle().addZ(10);

                    cubea.getAngle().setX(cubea.getAngle().getX() + 10);
                    cubeb.getAngle().setY(cubeb.getAngle().getY() + 10);
                    cubec.getAngle().setZ(cubec.getAngle().getZ() + 10);
                    */
                }
            }
            finally {
                wc.close(false);
            }
        }
        catch(Exception x) {
            x.printStackTrace(System.err);
        }
    }

    static class WindowControl  {
        private JFrame frame;
        private GraphicsDevice device;
        private BufferStrategy bufferStrategy;
        public WindowControl(int width, int height, TDView view) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            device = ge.getDefaultScreenDevice();
            GraphicsConfiguration deviceConfig = device.getDefaultConfiguration();

            this.frame = new JFrame( deviceConfig );
            MyMouse mymouse = new MyMouse(this, view);
            frame.addKeyListener(new MyKeys(view, mymouse));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.addMouseListener(mymouse);
            frame.addMouseMotionListener(mymouse);
            frame.setSize(width, height);

            device.setFullScreenWindow(frame);
            frame.validate();
             // Create the back buffer
            int numBuffers = 2;  // Includes front buffer
            frame.createBufferStrategy( numBuffers );
            bufferStrategy = frame.getBufferStrategy();
        }

        public int getWidth() {
            return frame.getWidth();
        }

        public int getHeight() {
            return frame.getHeight();
        }

        public void show() {
            bufferStrategy.show();
        }

        public Graphics getGraphics() {
             return bufferStrategy.getDrawGraphics();
        }


        public void close(boolean andExit) {
            device.setFullScreenWindow(null);
            if (andExit) {
                System.exit(0);
            }
        }
    }

    static class MyKeys extends KeyAdapter {
        private TDView view;
        private MyMouse mymouse;

        public MyKeys(TDView view, MyMouse mymouse) {
            this.view = view;
            this.mymouse = mymouse;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            CoardinateSystem cs = new CoardinateSystem();
            if (e.getKeyChar() == '+') {
                view.setFov( view.getFov() + 0.1f);
            }
            else if (e.getKeyChar() == '-') {
                view.setFov( view.getFov() - 0.1f);
            }
            else if (e.getKeyCode() == 32) {
                mymouse.toggleMotion();
            }
            else if (e.getKeyCode() == 40) {
                Vector p = new Vector(0, 0, 1f);
                cs.transform(p, view.getAngle());
                System.out.println(p);
                view.getPos().setZ(view.getPos().getZ() + (40 * p.getZ()));
                //view.getPos().setY(view.getPos().getY() + (40 * p.getY()));
                view.getPos().setX(view.getPos().getX() - (40 * p.getX()));
            }
            else if (e.getKeyCode() == 38) {
                Vector p = new Vector(0, 0, -1f);
                cs.transform(p, view.getAngle());
                System.out.println(p);
                view.getPos().setZ(view.getPos().getZ() + (40 * p.getZ()));
                //view.getPos().setY(view.getPos().getY() + (40 * p.getY()));
                view.getPos().setX(view.getPos().getX() - (40 * p.getX()));
            }
            else if (e.getKeyCode() == 39) {
                Vector p = new Vector(1f, 0f, 0f);
                cs.transform(p, view.getAngle());
                view.getPos().setZ(view.getPos().getZ() - (40 * p.getZ()));
                view.getPos().setX(view.getPos().getX() + (40 * p.getX()));
            }
            else if (e.getKeyCode() == 37) {
                Vector p = new Vector(-1f, 0f, 0f);
                cs.transform(p, view.getAngle());
                view.getPos().setZ(view.getPos().getZ() - (40 * p.getZ()));
                view.getPos().setX(view.getPos().getX() + (40 * p.getX()));
            }
        }
    }

    static class MyMouse extends MouseAdapter {
        private WindowControl winControl;
        private TDView view;
        private float lastX;
        private float lastY;
        private boolean motion = false;

        public MyMouse(WindowControl winControl, TDView view) {
            this.winControl = winControl;
            this.view = view;
        }
        public void toggleMotion() {
            motion = !motion;
            lastX = lastY = 0;
        }
        public void mousePressed(MouseEvent evt) {
            if (evt.getButton() != MouseEvent.BUTTON1) {
                winControl.close(true);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (!motion) return;
            if (lastX == 0) {
                lastX = e.getX();
            }
            if (lastY == 0) {
                lastY = e.getY();
            }
            view.getAngle().addX((e.getY() - lastY) * 0.25f);
            view.getAngle().addY((e.getX() - lastX) * 0.25f);
            lastX = e.getX();
            lastY = e.getY();
        }
    }

}
