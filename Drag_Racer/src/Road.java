import org.jdesktop.j3d.examples.collision.Box;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.java3d.utils.geometry.Cylinder;
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
import java.io.File;

public class Road {
    private static Point3d pt_zero = new Point3d(0d, 0d, 0d);
// aleksa adds road here

/*

        KEVIN PLEASE GOD STOP ADDING RANDOM SHIT TO THE ROAD CLASS
        IM DROWNING IN RANDOM FUNCTIONS -Av


 */
    private static Background createBkground(Color3f clr, BoundingSphere bounds) {
        //creating the background using background attribute
        Background bg = new Background();
        bg.setImage(new TextureLoader("2800-Final-Project/Drag_Racer/Resources/textures/neon.jpg", null).getImage()); //filename here

        bg.setImageScaleMode(Background.SCALE_NONE_CENTER);
        bg.setApplicationBounds(bounds);
        bg.setColor(clr);
    return bg;
}

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

    public static Shape3D xyzAxis(Color3f yColor, float length) {

        //The xyz will be created by 3 seperate but connected lines using a line array
        int strip[]= {2,2,2}; //declaring 3 strips, each made up of 1 line
        Point3f coords[]= new Point3f[6]; //6 coordinates all together
        LineStripArray lsa = new LineStripArray(6,LineStripArray.COLOR_3 |GeometryArray.COORDINATES,strip);
        //Z
        coords[0]=new Point3f(0.0f,0.0f, length);
        coords[1]=new Point3f(0.0f,0.0f, 0.0f); //note each line must start at origin
        //Y
        coords[2]=new Point3f(0.0f,length, 0.0f);
        coords[3]=new Point3f(0.0f,0.0f, 0.0f);
        //X
        coords[4]=new Point3f(length,0.0f, 0.0f);
        coords[5]=new Point3f(0.0f,0.0f, 0.0f);

        lsa.setCoordinates(0, coords);
        for (int i=0; i<2; i++) {
            //color for z axis red
            lsa.setColor(i, Commons.Red);
            //Color for y axis is Colorf ycolor
            lsa.setColor(i+2, yColor);
            //color for x axis green
            lsa.setColor(i+4, Commons.Green);
        }

        return new Shape3D(lsa);
    }

    private static TransformGroup createColumn(double scale, Vector3d pos) {
        Transform3D transM = new Transform3D();
        transM.set(scale, pos);  // Create base TG with 'scale' and 'position'
        TransformGroup baseTG = new TransformGroup(transM);

        Shape3D shape = new Box(0.5, 2.0, 200.0);
        baseTG.addChild(shape); // Create a column as a box and add to 'baseTG'

        Appearance app = shape.getAppearance();
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(1f, 1f, 1f); // set column's color and make changeable
        app.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
        app.setColoringAttributes(ca);

        CrashingBoundaries cd = new CrashingBoundaries(shape);
        cd.setSchedulingBounds(new BoundingSphere(pt_zero, 10d)); // detect column's collision

        baseTG.addChild(cd); // add column with behavior of CollisionDector
        return baseTG;
    }

    public static Shape3D texturePlane(String name, float x, float z) {
        Point3f[] verts = { new Point3f(-x,0,0),
                new Point3f(x,0,0), new Point3f(x,0,z),
                new Point3f(-x,0,z)};
        QuadArray qa = new QuadArray(4, GeometryArray.COORDINATES |
                GeometryArray.COLOR_3 | GeometryArray.TEXTURE_COORDINATE_2);
        qa.setCoordinates(0, verts);
        float uv0[] = { 0f, 0f };
        float uv1[] = { 1f, 0f };
        float uv2[] = { 1f, 1f };
        float uv3[] = { 0f, 1f };
        qa.setTextureCoordinate(0, 0, uv0);
        qa.setTextureCoordinate(0, 1, uv1);
        qa.setTextureCoordinate(0, 2, uv2);
        qa.setTextureCoordinate(0, 3, uv3);
        Appearance app = new Appearance();
        app.setTexture(texturedApp(name));
        PolygonAttributes poly= new PolygonAttributes();
        poly.setCullFace(PolygonAttributes.CULL_NONE);
        app.setPolygonAttributes(poly);
        Shape3D shape3D = new Shape3D(qa, app);
        return shape3D;
    }

    public static Texture texturedApp(String name) {
        String filename = "2800-Final-Project/Drag_Racer/Resources/textures/"+name+".jpg";
        TextureLoader loader = new TextureLoader(filename, null); //loads the image
        ImageComponent2D image = loader.getImage();
        if (image == null) {
            System.out.println("Cannot load file: " + filename); //incase fileName cant be found
        }
        Texture2D texture = new Texture2D(Texture.BASE_LEVEL,Texture.RGBA, image.getWidth(), image.getHeight()); //Texture2d object
        texture.setImage(0, image);
        return texture;
    }

    //old function that just tests if the columns have collision
    //not neccesary
    private static TransformGroup createBox() {
        TransformGroup transfmTG[] = new TransformGroup[2];
        for (int i = 0; i < 2; i++) {         // two TGs: 0-self and 1-orbit
            transfmTG[i] = new TransformGroup();
            transfmTG[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        }

        Transform3D t = new Transform3D(); // define scale and position
        t.set(0.12f, new Vector3d(0.0, -1, 0.0));
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

    private static TransformGroup createRoad(){
        TransformGroup baseMain= new TransformGroup();

        //create 20 road pieces and place them in order down the columns
        for (int i=1; i<=20; i++) {
            TransformGroup RoadPiece = new TransformGroup();
            Transform3D translator5 = new Transform3D();
            translator5.setTranslation(new Vector3f(0.0f, -0.1f, (float) -i));
            Transform3D trfm5 = new Transform3D();              // TG for composition matrix
            trfm5.mul(translator5);                              // apply translation
            RoadPiece.setTransform(trfm5);
            RoadPiece.addChild(texturePlane("road", 1, 1));
            baseMain.addChild(RoadPiece);
        }

        return baseMain;
    }

    public static Material setMaterial() {
        //material from lab 6
        int SH = 128;               // 10
        Material ma = new Material();
        ma.setAmbientColor(new Color3f(0.6f, 0.6f, 0.6f));
        ma.setEmissiveColor(new Color3f(0.0f, 0.0f, 0.0f));
        ma.setDiffuseColor(new Color3f(0.6f, 0.6f, 0.6f));
        ma.setSpecularColor(new Color3f(1.0f, 1.0f, 1.0f));
        ma.setShininess(SH);
        ma.setLightingEnable(true);
        return ma;
    }

    public static Sphere materializeSphere(float radius, Color3f color, boolean IsTransparent) {
        Appearance app = new Appearance();
        Color3f theColor = color;
        ColoringAttributes ca = new ColoringAttributes(theColor, ColoringAttributes.NICEST);
        app.setColoringAttributes(ca);
        PolygonAttributes pa = new PolygonAttributes();
        app.setPolygonAttributes(pa);
        //app.setMaterial(setMaterial());  //uncomment this when we have the lighting set up
        if (IsTransparent) {
            TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.5f);
            app.setTransparencyAttributes(ta);
        }
        return new Sphere(radius, Sphere.GENERATE_NORMALS, 80, app);
    }

    private static TransformGroup createStreetLight(){
        TransformGroup baseMain= new TransformGroup();

        //the lamps are just cylinders with an opaque sphere with a smaller white sphere inside

        //CREATE CYLINDER
        Appearance app = new Appearance();
        ColoringAttributes colorCylinder= new ColoringAttributes(Commons.Grey, ColoringAttributes.FASTEST);
        app.setColoringAttributes(colorCylinder); //after setting color we set the appearance
        TransformGroup cylinderShape= new TransformGroup();
        cylinderShape.addChild(new Cylinder(0.04f, 1f, app));

        Transform3D translator= new Transform3D();
        translator.setTranslation(new Vector3f(0, 1.0f ,0)); //this vector will place the cylinder above y=0
        Transform3D cylinderMatrix= new Transform3D(); //this is the matrix used to move the cylinder
        cylinderMatrix.mul(translator); //translating the cylinder
        cylinderShape.setTransform(cylinderMatrix);

        //CREATE SPHERES
        TransformGroup spheres = new TransformGroup();
        spheres.addChild(materializeSphere(0.2f, Commons.Grey,true ));
        spheres.addChild(materializeSphere(0.08f, Commons.Yellow,false ));
        Transform3D translatorSphere= new Transform3D();
        translatorSphere.setTranslation(new Vector3f(0, 1.5f ,0)); //this vector will place the cylinder above y=0
        Transform3D sphereMatrix= new Transform3D(); //this is the matrix used to move the cylinder
        sphereMatrix.mul(translatorSphere); //translating the cylinder
        spheres.setTransform(sphereMatrix);

        baseMain.addChild(spheres);
        baseMain.addChild(cylinderShape);
        return baseMain;
    }

    private static TransformGroup createLamps(){
        TransformGroup baseMain= new TransformGroup();

        //create n lamps and place them in order down the columns
        //right side
        for (int i=1; i<=8; i++) {
            TransformGroup Lamp = new TransformGroup();
            Transform3D translator5 = new Transform3D();
            translator5.setTranslation(new Vector3f(1.2f, -0.4f, (float) 2*(-i) +3));
            Transform3D trfm5 = new Transform3D();              // TG for composition matrix
            trfm5.mul(translator5);    // apply translation
            Lamp.addChild(createStreetLight());
            Lamp.setTransform(trfm5);
            baseMain.addChild(Lamp);
        }
        //yes placing the street lights is weird in the z axis be aware of that

        //left side
        for (int i=1; i<=8; i++) {
            TransformGroup Lamp = new TransformGroup();
            Transform3D translator5 = new Transform3D();
            translator5.setTranslation(new Vector3f(-1.2f, -0.4f, (float) 2*(-i) +3));
            Transform3D trfm5 = new Transform3D();              // TG for composition matrix
            trfm5.mul(translator5);    // apply translation
            Lamp.addChild(createStreetLight());
            Lamp.setTransform(trfm5);
            baseMain.addChild(Lamp);
        }

        return baseMain;
    }
    /*
    This function creates the side walls for the project
     */
    private static void wallsBarriersRoadLamps(TransformGroup sceneTG) {
        Vector3d[] pos = {new Vector3d(1, 0, -10),
                new Vector3d(-1, 0, -10)};
        for (int i =0; i < 2; i++)
            sceneTG.addChild(createColumn(0.1, pos[i]));
        sceneTG.addChild(createBox());
        sceneTG.addChild(createRoad());
        sceneTG.addChild(createLamps());
    }

    public static BranchGroup createScene() {
        BranchGroup scene = new BranchGroup(); // create 'scene' as content branch

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.MAX_VALUE);
        scene.addChild(createBkground(Commons.Grey, bounds)); //add background

        scene.addChild(xyzAxis(Commons.Blue, 1));       //xyz axis added
        TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
        scene.addChild(sceneTG);	                         // add TG to the scene BranchGroup

        wallsBarriersRoadLamps(sceneTG);
        scene.compile(); // optimize scene BG

        return scene;
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Commons.setEye(new Point3d(0, 1, 7));
                new Commons.MyGUI(createScene(), "Drag Racing Game");
            }
        });
    }

}
