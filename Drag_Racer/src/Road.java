import org.jdesktop.j3d.examples.collision.Box;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Road {
    private static Point3d pt_zero = new Point3d(0d, 0d, 0d);
// aleksa adds road here

// Kevin adds lighting and fog
    private static void createLight(BranchGroup bg) { // create light with ambient lighting with point light
        AmbientLight ambientLight = new AmbientLight(true, new Color3f(0.2f, 0.2f, 0.2f)); // lightOn set to true, with Color3f(0.2f, 0.2f, 0.,2f)
        ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(0,0,0), 100));// set influencing bounds with bouding sphere at point3d (0,0,0) with radius 100
        bg.addChild(ambientLight); // add child to branch group with ambient light
        PointLight pointLight = new PointLight(true, Commons.White, new Point3f(2, 2, 2), new Point3f(1, 0, 0));
        pointLight.setInfluencingBounds(new BoundingSphere(new Point3d(0,0,0), 100));
        bg.addChild(pointLight); // add point light to branch group
    }

    private static ExponentialFog createFog(Color3f clr, BoundingSphere bounds) {
        ExponentialFog exponentialFog = new ExponentialFog(clr, 0.1f); // set up exponential fog with 0.1 density
        exponentialFog.setInfluencingBounds(bounds);
        return exponentialFog;
    }

    private static TransformGroup createColumn(double scale, Vector3d pos) {
        Transform3D transM = new Transform3D();
        transM.set(scale, pos);  // Create base TG with 'scale' and 'position'
        TransformGroup baseTG = new TransformGroup(transM);

        Shape3D shape = new Box(0.5, 5.0, 1.0);
        baseTG.addChild(shape); // Create a column as a box and add to 'baseTG'

        Appearance app = shape.getAppearance();
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(0.6f, 0.3f, 0.0f); // set column's color and make changeable
        app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        app.setColoringAttributes(ca);

        CrashingBoundaries cd = new CrashingBoundaries(shape);
        cd.setSchedulingBounds(new BoundingSphere(pt_zero, 10d)); // detect column's collision

        baseTG.addChild(cd); // add column with behavior of CollisionDector
        return baseTG;
    }

    private static TransformGroup createBox() {
        TransformGroup transfmTG[] = new TransformGroup[2];
        for (int i = 0; i < 2; i++) {         // two TGs: 0-self and 1-orbit
            transfmTG[i] = new TransformGroup();
            transfmTG[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        }

        Transform3D t = new Transform3D(); // define scale and position
        t.set(0.12f, new Vector3d(0.0, -0.6, 0.0));
        TransformGroup transCube = new TransformGroup(t);
        ColorCube colorCube = new ColorCube();
        Appearance app = new Appearance(); // create new appearance
        app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE); // set transparency attributes to write
        colorCube.setAppearance(app); // set colorCube appearance
        CrashingBoundaries tcdc = new CrashingBoundaries(colorCube); // create a Transparent collisionn detection for columns
        tcdc.setSchedulingBounds(new BoundingSphere(pt_zero, 10d)); // set scheduling bounds with bounding sphere using pt_zero and radius of 10d
        // add colorCube and tcdc to transCube
        transCube.addChild(colorCube);
        transCube.addChild(tcdc);
        transfmTG[0].addChild(transCube);            // add a unit cube to 3rd TG

        Transform3D yAxis1 = new Transform3D();
        yAxis1.rotX(Math.PI / 2.0);                // define animation along orbit
        Alpha alphaOrbit = new Alpha(-1, Alpha.INCREASING_ENABLE |
                Alpha.DECREASING_ENABLE, 0, 0, 5000, 2500, 200,	5000, 2500, 200);
        RotationInterpolator tickTock = new RotationInterpolator(alphaOrbit,
                transfmTG[1], yAxis1, -(float)Math.PI/ 2.0f, (float)Math.PI/ 2.0f);
        tickTock.setSchedulingBounds(new BoundingSphere(pt_zero, 10d));
        transfmTG[1].addChild(tickTock);     // add orbit animation to scene graph

        Transform3D yAxis2 = new Transform3D();
        Alpha alphaSelf = new Alpha(-1, Alpha.INCREASING_ENABLE,
                0, 0, 4000, 0, 0, 0, 0, 0);   // define self-rotating animation
        RotationInterpolator rotatorSelf = new RotationInterpolator(alphaSelf,
                transfmTG[0], yAxis2, 0.0f,	(float) Math.PI * 2.0f);
        rotatorSelf.setSchedulingBounds(new BoundingSphere(pt_zero, 10d));
        transfmTG[0].addChild(rotatorSelf);
        transfmTG[1].addChild(transfmTG[0]);      // add self-rotation to orbit

        return transfmTG[1];
    }


    private static void createTickTock(TransformGroup sceneTG) {
        Vector3d[] pos = {new Vector3d(0.5, -0.15, 0.7),
                new Vector3d(0.5, -0.15, -0.1)};
        for (int i =0; i < 2; i++)
            sceneTG.addChild(createColumn(0.1, pos[i]));
        sceneTG.addChild(createBox());
    }

    public static BranchGroup createScene() {
        BranchGroup scene = new BranchGroup(); // create 'scene' as content branch
        TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
        scene.addChild(sceneTG);	                         // add TG to the scene BranchGroup


        createTickTock(sceneTG);
        scene.compile(); // optimize scene BG

        return scene;
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Commons.setEye(new Point3d(3.7, 1, 0));
                new Commons.MyGUI(createScene(), "Drag Racing Game");
            }
        });
    }

}
