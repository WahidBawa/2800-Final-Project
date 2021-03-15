import org.jogamp.java3d.*;
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

    public static BranchGroup createScene() {
        int n = 6; // pass in n for cube(n < 3) or n = 6 for pavillion as a hexagon
        BranchGroup sceneBG = new BranchGroup();             // create 'objsBG' for content
        TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
        sceneBG.addChild(sceneTG);                           // add TG to the scene BranchGroup
//        sceneBG.addChild(CommonsKL.rotateBehavior(10000, sceneTG));
        // make sceneTG continuously rotating
        createLight(sceneBG);
//        sceneTG.addChild(createBase(0.6f));
        sceneBG.addChild(createFog(new Color3f(0.5f, 0.5f, 0.5f), new BoundingSphere(new Point3d(0f, 0f,0f), 100)));

        sceneBG.compile(); // optimize objsBG
        return sceneBG;
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Commons.setEye(new Point3d(2, 2, 6));
                new Commons.MyGUI(createScene(), "Drag Racing Game");
            }
        });
    }

}
