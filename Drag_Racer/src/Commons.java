import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

public class Commons extends JPanel implements MouseListener {
    private static final long serialVersionUID = 1L;
    public final static Color3f Red = new Color3f(1.0f, 0.0f, 0.0f);
    public final static Color3f Green = new Color3f(0.0f, 1.0f, 0.0f);
    public final static Color3f Blue = new Color3f(0.0f, 0.0f, 1.0f);
    public final static Color3f Yellow = new Color3f(1.0f, 1.0f, 0.0f);
    public final static Color3f Cyan = new Color3f(0.0f, 1.0f, 1.0f);
    public final static Color3f Orange = new Color3f(1.0f, 0.5f, 0.0f);
    public final static Color3f Magenta = new Color3f(1.0f, 0.0f, 1.0f);
    public final static Color3f White = new Color3f(1.0f, 1.0f, 1.0f);
    public final static Color3f Grey = new Color3f(0.5f, 0.5f, 0.5f);
    public final static Color3f[] Clrs = {Blue, Green, Red, Yellow,
            Cyan, Orange, Magenta, Grey};
    public final static int clr_num = 8;

    private static SimpleUniverse su = null;
    public static Camera cam;
    public static MyMouseListener mouseListener;
    private static Canvas3D canvas_3D;
    private static JFrame frame;
    private static Point3d eye = new Point3d(1.35, 0.35, 2.0);
    public static PickTool pickTool;

    /* a function to create a rotation behavior and refer it to 'my_TG' */
    public static RotationInterpolator rotateBehavior(int r_num, TransformGroup my_TG) {

        my_TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D yAxis = new Transform3D();
        Alpha rotationAlpha = new Alpha(-1, r_num);
        RotationInterpolator rot_beh = new RotationInterpolator(
                rotationAlpha, my_TG, yAxis, 0.0f, (float) Math.PI * 2.0f);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        rot_beh.setSchedulingBounds(bounds);
        return rot_beh;
    }
    public static RotationInterpolator rotateBehaviorx(int r_num, TransformGroup my_TG) {

        my_TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D xAxis = new Transform3D();
        xAxis.rotX(Math.PI/2);
        Alpha rotationAlpha = new Alpha(-1, r_num);
        RotationInterpolator rot_beh = new RotationInterpolator(
                rotationAlpha, my_TG, xAxis, 0.0f, (float) Math.PI * 2.0f);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        rot_beh.setSchedulingBounds(bounds);
        return rot_beh;
    }
    public static RotationInterpolator rotateBehaviorz(int r_num, TransformGroup my_TG) {

        my_TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D zAxis = new Transform3D();
        zAxis.rotZ(Math.PI/2);
        Alpha rotationAlpha = new Alpha(-1, r_num);
        RotationInterpolator rot_beh = new RotationInterpolator(
                rotationAlpha, my_TG, zAxis, 0.0f, (float) Math.PI * 2.0f);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        rot_beh.setSchedulingBounds(bounds);
        return rot_beh;
    }

    /* a function to position viewer to 'eye' location */
    public static void defineViewer(SimpleUniverse su) {

        TransformGroup viewTransform = su.getViewingPlatform().getViewPlatformTransform();
        Point3d center = new Point3d(0, 0, 0);               // define the point where the eye looks at
        Vector3d up = new Vector3d(0, 1, 0);                 // define camera's up direction
        Transform3D view_TM = new Transform3D();
        view_TM.lookAt(eye, center, up);
        view_TM.invert();
        viewTransform.setTransform(view_TM);                 // set the TransformGroup of ViewingPlatform
    }

    public static SimpleUniverse getSimpleU() {
        return su;
    }

    /* a function to build the content branch and attach to 'scene' */
    private static BranchGroup createScene() {
        BranchGroup scene = new BranchGroup();

        TransformGroup content_TG = new TransformGroup();    // create a TransformGroup (TG)
        content_TG.addChild(new ColorCube(0.4f));
        scene.addChild(content_TG);	                         // add TG to the scene BranchGroup
        scene.addChild(rotateBehavior(10000, content_TG));   // make TG continuously rotating

        return scene;
    }

    public static void setEye(Point3d eye_position) {
        eye = eye_position;
    }

    /* a constructor to set up and run the application */
    public Commons(BranchGroup sceneBG) {
        cam = new Camera(eye);
        Canvas3D canvas_3D = cam.getCanvas3D();

        mouseListener = new MyMouseListener(canvas_3D, pickTool);
        canvas_3D.addMouseListener(mouseListener);
        SimpleUniverse su = cam.getSu();   // create a SimpleUniverse

        sceneBG.compile();
        su.addBranchGraph(sceneBG);                          // attach the scene to SimpleUniverse

        setLayout(new BorderLayout());
        add("Center", canvas_3D);
        frame.setSize(600, 600);                             // set the size of the JFrame
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        frame = new JFrame("XY's Commons");                  // call constructor with 'createScene()'
        frame.getContentPane().add(new Commons(createScene()));
    }

    public static void createUniverse() {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas_3D = new Canvas3D(config);
        su = new SimpleUniverse(canvas_3D);   // create a SimpleUniverse
        defineViewer(su);                                    // set the viewer's location
    }

    public static class MyGUI extends JFrame {
        private static final long serialVersionUID = 1L;
        public MyGUI(BranchGroup branchGroup, String title) {
            frame = new JFrame(title);                       // call constructor with 'branchGroup'
            frame.getContentPane().add(new Commons(branchGroup));
            pack();
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        int x = event.getX(); int y = event.getY();           // mouse coordinates
        Point3d point3d = new Point3d(), center = new Point3d();
        canvas_3D.getPixelLocationInImagePlate(x, y, point3d);   // obtain AWT pixel in ImagePlate coordinates
        canvas_3D.getCenterEyeInImagePlate(center);              // obtain eye's position in IP coordinates

        Transform3D transform3D = new Transform3D();          // matrix to relate ImagePlate coordinates~
        canvas_3D.getImagePlateToVworld(transform3D);            // to Virtual World coordinates
        transform3D.transform(point3d);                       // transform 'point3d' with 'transform3D'
        transform3D.transform(center);                        // transform 'center' with 'transform3D'

        Vector3d mouseVec = new Vector3d();
        mouseVec.sub(point3d, center);
        mouseVec.normalize();
        pickTool.setShapeRay(point3d, mouseVec);              // send a PickRay for intersection
        System.out.println("listening to mouse");

        if (pickTool.pickClosest() != null) {
            System.out.println("working!!!");
            PickResult pickResult = pickTool.pickClosest();   // obtain the closest hit
            Shape3D car = (Shape3D) pickResult.getNode(PickResult.PRIMITIVE);
            if (car == null) {
                //IF CAR IS NULL, the object clicked was not a sphere and doesnt need to change
            }
            else {
                if ((int) car.getUserData() == 1) {               // retrieve 'UserData'
                    Sounds.playSound(4);                      // set 'UserData' to a new value
                }
            }
        }
    }

    public void mouseEntered(MouseEvent arg0) { }
    public void mouseExited(MouseEvent arg0) { }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
}