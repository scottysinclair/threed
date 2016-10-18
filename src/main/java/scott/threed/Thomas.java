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

public class Thomas extends ModelFactory {
    public static void main(String args[]) {
        try {
            final int width = 1920;
            final int height = 1080;
            TDWorld world = new TDWorld();
            final TDView view = new TDView(vector(0, 0, 0), vector(0, 0, 0), world);
            view.initView(35.5f, width, height);

            view.getPos().setX(-1500f);
            view.getPos().setY(-1000f);
            view.getPos().setZ(2000f);
            view.getAngle().setY(40);
//            view.getPos().setX(-500);
//            view.getAngle().setY(90);

            TDObject thomas = thomasHead(vector(0, 0, 0), vector(0, 0, 0));
            world.add(thomas);

            int widthOf1 = 1400;
            int posX = -widthOf1;
            for (int i=0; i<4; i++) {
                TDObject carriage = carriage(vector(posX, 0, 0), vector(0, 0, 0));
                world.add(carriage);
                posX -= widthOf1;
            }




            WindowControl wc = new WindowControl(width, height, view);

            // Create a window for full-screen mode; add a button to leave full-screen mode
            try {
                while(true) {
                    Graphics g = wc.getGraphics();
                    g.setColor(Color.white);
                    g.fillRect(0, 0, wc.getWidth(), wc.getHeight());

                    g.setColor(Color.BLACK);

                    view.render(g);

//                    wheel1.getAngle().setY( wheel1.getAngle().getY() + 1);
//                    wheel2.getAngle().setY( wheel2.getAngle().getY() + 1);

                    g.dispose();
                    wc.show();

                    Thread.sleep(40);

                    thomas.getPos().setX( thomas.getPos().getX() + 20);
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


    private static TDObject thomasHead(Vector pos, Vector angle) {
        TDObject body = rect(600, 300, 300, vector(0, 0, 0), vector(0, 0, 0));

        TDObject head = wheel(250, 200, 20, vector(660, 0, 0), vector(0, 90, 0));
        body.addPart(head);

        final int wheelThickness = 50;
        final int wheelPointsOnCircumference = 40;

        TDObject wheel1 = wheel(190, wheelThickness, wheelPointsOnCircumference, vector(-300, -500, 300), vector(0, 0, 0));
        body.addPart(wheel1);

        TDObject wheel2 = wheel(190, wheelThickness, wheelPointsOnCircumference, vector(300, -500, 300), vector(0, 0, 0));
        body.addPart(wheel2);

        TDObject wheel3 = wheel(190, wheelThickness, wheelPointsOnCircumference, vector(-300, -500, -300), vector(0, 0, 0));
        body.addPart(wheel3);

        TDObject wheel4 = wheel(190, wheelThickness, wheelPointsOnCircumference, vector(300, -500, -300), vector(0, 0, 0));
        body.addPart(wheel4);

        body.setPos(pos);
        body.setAngle(angle);

        return body;
    }

    private static TDObject carriage(Vector pos, Vector angle) {
        TDObject body = rect(600, 300, 300, vector(0, 0, 0), vector(0, 0, 0));

        final int wheelThickness = 50;
        final int wheelPointsOnCircumference = 40;

        TDObject wheel1 = wheel(190, wheelThickness, wheelPointsOnCircumference, vector(-300, -500, 300), vector(0, 0, 0));
        body.addPart(wheel1);

        TDObject wheel2 = wheel(190, wheelThickness, wheelPointsOnCircumference, vector(300, -500, 300), vector(0, 0, 0));
        body.addPart(wheel2);

        TDObject wheel3 = wheel(190, wheelThickness, wheelPointsOnCircumference, vector(-300, -500, -300), vector(0, 0, 0));
        body.addPart(wheel3);

        TDObject wheel4 = wheel(190, wheelThickness, wheelPointsOnCircumference, vector(300, -500, -300), vector(0, 0, 0));
        body.addPart(wheel4);

        body.setPos(pos);
        body.setAngle(angle);

        return body;

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
