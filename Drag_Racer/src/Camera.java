import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import org.jogamp.java3d.utils.geometry.Cone;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.universe.*;
import org.jogamp.vecmath.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Camera {
    static final int width = 600;                            // size of each Canvas3D
    static final int height = 600;
    private static final long serialVersionUID = 1L;
    private final Vector3f location = new Vector3f(0, 1, 0);
    private final ViewingPlatform vp;
    private final Viewer viewer;
    private final Canvas3D canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
    SimpleUniverse su;

    public Camera(Point3d eye) {
        // a Canvas3D can only be attached to a single Viewer
        viewer = new Viewer(canvas3D);                 // attach a Viewer to its canvas
        vp = new ViewingPlatform(1);       // 1 VP with 1 TG above
        createViewer(canvas3D, "F-L", Commons.Orange, eye.x, eye.y, eye.z);
        su = new SimpleUniverse(vp, viewer); // a SU with one Vp and 3 Viewer

    }

    public void moveCamera(float x, float y, float z) {
        Transform3D t1 = new Transform3D();
        t1.setTranslation(new Vector3f(x, y, z));
        vp.getViewPlatformTransform().setTransform(t1);
    }

    ViewingPlatform createViewer(Canvas3D canvas3D, String name, Color3f clr,
                                 double x, double y, double z) {
        // assign PG to the Viewer
        vp.setPlatformGeometry(labelPlatformGeometry(name));
        viewer.setAvatar(createViewerAvatar(name, clr)); // assign VA to the Viewer

        Point3d center = new Point3d(0, 0.5f, 0);               // define where the eye looks at
        Vector3d up = new Vector3d(0, 1, 0);                 // define camera's up direction
        Transform3D viewTM = new Transform3D();
        Point3d eye = new Point3d(x, y, z);                  // define eye's location
        viewTM.lookAt(eye, center, up);
        viewTM.invert();
        vp.getViewPlatformTransform().setTransform(viewTM);  // set VP with 'viewTG'

        // set TG's capabilities to allow KeyNavigatorBehavior modify the Viewer's position
        vp.getViewPlatformTransform().setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        vp.getViewPlatformTransform().setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        KeyNavigatorBehavior key = new KeyNavigatorBehavior(vp.getViewPlatformTransform());
        key.setSchedulingBounds(new BoundingSphere());          // enable viewer navigation
        key.setEnable(false);
        vp.addChild(key);                                   // add KeyNavigatorBehavior to VP
        //viewer.setViewingPlatform( vp );                      // set VP for the Viewer

        return vp;
    }

    PlatformGeometry labelPlatformGeometry(String szText) {
        PlatformGeometry pg = new PlatformGeometry();
        pg.addChild(createLabel(szText, 0f, 0.5f, 0f));    // label the PlatformGeometry ~
        return pg;                                           // to help identify the viewer
    }

    // creates a simple Raster text label (similar to Text2D)
    private Shape3D createLabel(String szText, float x, float y, float z) {
        BufferedImage bufferedImage = new BufferedImage(25, 14, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();
        g.setColor(Color.white);
        g.drawString(szText, 2, 12);

        ImageComponent2D img2D = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bufferedImage);
        Raster renderRaster = new Raster(new Point3f(x, y, z), Raster.RASTER_COLOR,
                0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), img2D, null);
        return new Shape3D(renderRaster);                  // create the Raster for the image
    }

    /* a function to create and position a simple Cone to represent the Viewer */
    ViewerAvatar createViewerAvatar(String szText, Color3f objColor) {
        ViewerAvatar viewerAvatar = new ViewerAvatar();
        // lay down the Cone, pointing sharp-end towards the Viewer's field of view
        TransformGroup tg = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setEuler(new Vector3d(Math.PI / 2.0, Math.PI, 0));
        tg.setTransform(t3d);

        Appearance app = new Appearance();

        tg.addChild(new Cone(0.5f, 1.5f, Primitive.GENERATE_NORMALS, app));
        viewerAvatar.addChild(tg);                         // add Cone to parent BranchGroup

        return viewerAvatar;
    }

    public SimpleUniverse getSu() {
        return su;
    }

    public Canvas3D getCanvas3D() {
        return canvas3D;
    }
}
