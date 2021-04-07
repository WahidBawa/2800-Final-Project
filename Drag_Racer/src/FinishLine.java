import org.jdesktop.j3d.examples.collision.Box;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

public class FinishLine {

    private static final Point3d pt_zero = new Point3d(0d, 0d, 0d);

    public static TransformGroup createFinishLine(double scale, Vector3d pos) {
        Transform3D transM = new Transform3D();
        transM.set(scale, pos);  // Create base TG with 'scale' and 'position'
        Transform3D transT = new Transform3D();
        transT.setTranslation(new Vector3f(0.0f, 0.0f, -300.0f));

        Transform3D trfm = new Transform3D();
        trfm.mul(transM);
        trfm.mul(transT);

        TransformGroup baseTG = new TransformGroup();
        baseTG.setTransform(trfm);


        Shape3D shape = new Box(14.0, 0.01, 0.1);
        baseTG.addChild(shape); // Create a column as a box and add to 'baseTG'

        Appearance app = GroundAndBackground.setApp("flagC");
        TransparencyAttributes tat = new TransparencyAttributes();
        tat.setTransparency(1);
        tat.setTransparencyMode(TransparencyAttributes.FASTEST);
        app.setTransparencyAttributes(tat);
        app.setCapability(Appearance.ALLOW_TEXTURE_WRITE);

        shape.setAppearance(app);

        FinishLineBoundry cd = new FinishLineBoundry(shape);
        cd.setSchedulingBounds(new BoundingSphere(pt_zero, 10d)); // detect column's collision

        baseTG.addChild(cd); // add column with behavior of CollisionDector
        return baseTG;
    }
}
