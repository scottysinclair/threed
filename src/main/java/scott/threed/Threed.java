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

import javax.swing.JFrame;

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
