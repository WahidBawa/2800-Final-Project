import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyMouseListener implements MouseListener {
    private Canvas3D canvas_3D;
    private PickTool pickTool;

    public MyMouseListener(Canvas3D canvas_3D, PickTool pickTool) {
        this.canvas_3D = canvas_3D;
        this.pickTool = pickTool;
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        int x = event.getX();
        int y = event.getY();           // mouse coordinates
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
            Shape3D car = (Shape3D) pickResult.getNode(PickResult.SHAPE3D);
            if (car == null) {
                //IF CAR IS NULL, the object clicked was not a sphere and doesnt need to change
                System.out.println("working NULLL!!!");
            } else {
                if ((int) car.getUserData() == 1) {               // retrieve 'UserData'
                    Sounds.playSound(4);                      // set 'UserData' to a new value
                }
            }
        }
    }

    public Canvas3D getCanvas_3D() {
        return canvas_3D;
    }

    public void setCanvas_3D(Canvas3D canvas_3D) {
        this.canvas_3D = canvas_3D;
    }

    public PickTool getPickTool() {
        return pickTool;
    }

    public void setPickTool(PickTool pickTool) {
        this.pickTool = pickTool;
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}