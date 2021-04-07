import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public class GroundAndBackground {
    public static TransformGroup rotatingSpheres(int x, int z){
        TransformGroup scene = new TransformGroup();
        Sphere s = new Sphere(0.2f, Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS | Primitive.ENABLE_APPEARANCE_MODIFY, 50);
        s.setAppearance(setApp("gridBlue"));

        Transform3D t3d = new Transform3D();
        t3d.rotZ(Math.PI/2);
        t3d.setTranslation(new Vector3f(x,-2.5f,z));

        TransformGroup rotate = new TransformGroup();
        rotate.addChild(s);
        scene.addChild(sphereRotation(10000, rotate));
        scene.setTransform(t3d);
        scene.addChild(rotate);
        return scene;
    }
    public static RotationInterpolator sphereRotation(int r_num, TransformGroup my_TG) {

        my_TG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D yAxis = new Transform3D();
        yAxis.rotX(Math.PI / 2.0);                // define animation along orbit
        yAxis.setTranslation(new Vector3f(3,0,0));
        Alpha rotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE |
                Alpha.DECREASING_ENABLE, 0, 0, 5000, 2500, 200, 5000, 2500, 200);
        RotationInterpolator rot_beh = new RotationInterpolator(
                rotationAlpha, my_TG, yAxis, 3 * (float) Math.PI / 2, (float) Math.PI / 2);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        rot_beh.setSchedulingBounds(bounds);
        return rot_beh;
    }

    public static TransformGroup FinishLine(){
        //Create a transformgroup of the flag location
        TransformGroup finishLine = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(0,0,-30.2f));

        //Create the flag
        finishLine.setTransform(t3d);
        createPillars(finishLine);
        checkerFlag(finishLine);

        //Return to road
        return finishLine;
    }
    private static void createPillars(TransformGroup scene){
        //Create one pillar for the flag, this is copied with a different location following
        TransformGroup transform = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(1,0,0));
        transform.setTransform(t3d);
        Cylinder c1 = new Cylinder(0.1f, 3f, Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS | Primitive.ENABLE_APPEARANCE_MODIFY, setApp("gridBlue"));
        transform.addChild(c1);
        scene.addChild(transform); //Attach to scene

        //Second pillar
        Cylinder c2 = new Cylinder(0.1f, 3f, Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS | Primitive.ENABLE_APPEARANCE_MODIFY, setApp("gridBlue"));
        t3d.setTranslation(new Vector3f(-1,0,0));
        transform = new TransformGroup(t3d);
        transform.addChild(c2);
        scene.addChild(transform); //Attach to scene
    }
    private static void checkerFlag(TransformGroup scene){
        TransformGroup flag = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(new Vector3f(0,1.23f,0));
        flag.setTransform(t3d);

        //Creates all the lines needed for the flag
        float[] uv0 = {0f, 0f};
        float[] uv1 = {1f, 0f};
        float[] uv2 = {1f, 1f};
        float[] uv3 = {0f, 1f};

        //Setting the coords and the texture of the flag
        QuadArray square = new QuadArray(4, QuadArray.COLOR_3 | QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2);
        square.setCoordinate(0, new Point3d(-1, -0.25, 0));
        square.setTextureCoordinate(0, 0, uv0);
        square.setCoordinate(1, new Point3d(1, -0.25, 0));
        square.setTextureCoordinate(0, 1, uv1);
        square.setCoordinate(2, new Point3d(1, 0.25, 0));
        square.setTextureCoordinate(0, 2, uv2);
        square.setCoordinate(3, new Point3d(-1, 0.25, 0));
        square.setTextureCoordinate(0, 3, uv3);

        //Set the app of the flag
        Shape3D s = new Shape3D(square);
        s.setAppearance(setApp("flag"));

        //Attatch the flag to the scene
        flag.addChild(s);
        scene.addChild(flag);
    }

    public static void Tree(TransformGroup objectTG, float x, float z) {

        TransformGroup transformGroup = new TransformGroup(); // we will rotate the loaded obj inside of this group
        Transform3D transform = new Transform3D(); // use this to apply the transformations to the transform group
        transform.setTranslation(new Vector3f(x, 0.8f, z));
        transformGroup.setTransform(transform); // apply the transformation to the transform group

        ObjectFile objectFile = new ObjectFile(ObjectFile.STRIPIFY | ObjectFile.TRIANGULATE | ObjectFile.RESIZE); // used to load in the obj file

        try {
            Scene s = objectFile.load(new File("Assets/tree.obj").toURI().toURL()); // load in the file

            BranchGroup objBG = s.getSceneGroup();
            Shape3D tree = (Shape3D) objBG.getChild(0); // get the shape3d object from the obj
            tree.setAppearance(setApp("gridBlue"));

            objBG.removeAllChildren();
            transformGroup.addChild(tree);
            objectTG.addChild(transformGroup);

        } catch (FileNotFoundException | MalformedURLException e) { // if there is an error
            System.err.println(e);
            System.exit(1);
        }

    }

    public static TransformGroup generateCylinder(int size, String file) {
        //Transform the object to the correct location
        Transform3D transform3D = new Transform3D();
        transform3D.setTranslation(new Vector3f(0, 1.5f, -15));
        TransformGroup scene_TG = new TransformGroup(transform3D);

        //Create the banner to hold the text image
        TransformGroup banner = new TransformGroup(transform3D);
        Cylinder c = new Cylinder(size, 0.5f, Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS | Primitive.ENABLE_APPEARANCE_MODIFY, setApp("Text"));
        banner.addChild(c);
        scene_TG.addChild(banner);

        //Add the rotation behavior
        scene_TG.addChild(Commons.rotateBehavior(10000, banner));

        //Connect the border to the text image
        baseCylinder(size, scene_TG);

        //Return to create scene
        return scene_TG;
    }

    private static void baseCylinder(int size, TransformGroup scene) {
        //Create the bases and translate to the correct location
        Transform3D transform3D = new Transform3D();
        transform3D.setTranslation(new Vector3f(0, 0.3f, 0));
        TransformGroup botBase = new TransformGroup(transform3D);
        transform3D.setTranslation(new Vector3f(0, -0.3f, 0));
        TransformGroup topBase = new TransformGroup(transform3D);

        //Create the object and rotate
        TransformGroup rotation = new TransformGroup();
        Cylinder c = new Cylinder(size * 1.05f, 0.1f, Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS | Primitive.ENABLE_APPEARANCE_MODIFY, setApp("gridBlue"));
        rotation.addChild(c);
        botBase.addChild(rotation);
        botBase.addChild(Commons.rotateBehavior(10000, rotation));

        //Create the object and rotate
        rotation = new TransformGroup();
        c = new Cylinder(size * 1.05f, 0.1f, Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS | Primitive.ENABLE_APPEARANCE_MODIFY, setApp("gridBlue"));
        rotation.addChild(c);
        topBase.addChild(rotation);
        topBase.addChild(Commons.rotateBehavior(10000, rotation));

        //Add to the outer scene
        scene.addChild(topBase);
        scene.addChild(botBase);
    }

    public static TransformGroup generateGround(int size, float h) {
        TransformGroup scene_TG = new TransformGroup();
        //Loop to tile the ground
        for (int i = 0; i < size; i++) {
            scene_TG.addChild(ground(new Vector3f(5f, h, 5f), -(i * 10), -10));
            scene_TG.addChild(ground(new Vector3f(5f, h, 5f), -(i * 10), 10));
            scene_TG.addChild(ground(new Vector3f(5f, h, 5f), -(i * 10), 0));
            scene_TG.addChild(sphere(8, 60, 10, -(i * 10)));
            scene_TG.addChild(sphere(8, 60, -10, -(i * 10)));
            Tree(scene_TG, -3, -(i * 10));
            Tree(scene_TG, 3, -(i * 10));
            scene_TG.addChild(rotatingSpheres(0,-(i * 10) - 2));
            scene_TG.addChild(rotatingSpheres(0,-(i * 10) + 2));
        }
        //Create a curb behind the car to look nice
        Transform3D t3d = new Transform3D();
        t3d.rotZ(Math.PI/2);
        t3d.setTranslation(new Vector3f(0,-0.1f,1f));
        TransformGroup curb = new TransformGroup(t3d);
        Cylinder c = new Cylinder(0.1f, 2f, Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS | Primitive.ENABLE_APPEARANCE_MODIFY, setApp("concrete"));
        curb.addChild(c);
        scene_TG.addChild(curb);

        return scene_TG;
    }

    private static Shape3D ground(Vector3f scale, int pos, int w) {
        //Creates all the lines needed for the Ground
        float[] uv0 = {0f, 0f};
        float[] uv1 = {1f, 0f};
        float[] uv2 = {1f, 1f};
        float[] uv3 = {0f, 1f};

        //Setting the coords and the texture of the flat surface
        QuadArray square = new QuadArray(4, QuadArray.COLOR_3 | QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2);
        square.setCoordinate(0, new Point3d(1 * scale.x + w, -1 * scale.y, 1 * scale.z + pos));
        square.setTextureCoordinate(0, 0, uv0);
        square.setCoordinate(1, new Point3d(1 * scale.x + w, -1 * scale.y, -1 * scale.z + pos));
        square.setTextureCoordinate(0, 1, uv1);
        square.setCoordinate(2, new Point3d(-1 * scale.x + w, -1 * scale.y, -1 * scale.z + pos));
        square.setTextureCoordinate(0, 2, uv2);
        square.setCoordinate(3, new Point3d(-1 * scale.x + w, -1 * scale.y, 1 * scale.z + pos));
        square.setTextureCoordinate(0, 3, uv3);

        Shape3D flatGround = new Shape3D(square);
        flatGround.setAppearance(setApp("grid"));
        return flatGround;
    }

    private static TransformGroup sphere(int n, int d, float x, float z) {
        //set the sphere to the correct location
        Transform3D move = new Transform3D();
        move.setTranslation(new Vector3f(x, -6, z));
        TransformGroup TG = new TransformGroup(move);

        //Create the sphere
        Sphere s = new Sphere(n, Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS | Primitive.ENABLE_APPEARANCE_MODIFY, d);

        //Apply to the sphere and attach to the base
        s.setAppearance(setApp("grid"));
        TG.addChild(s);

        //Return
        return TG;
    }

    public static Appearance setApp(String texture) {
        Appearance app = new Appearance();
        app.setTexture(texturedApp(texture));
        app.setMaterial(setMaterial());
        return app;
    }

    public static Texture texturedApp(String name) {
        //Load the image
        String filename = "Resources/textures/" + name + ".jpg";
        TextureLoader loader = new TextureLoader(filename, null);
        ImageComponent2D image = loader.getImage();

        //Check if the image was not found
        if (image == null) {
            System.out.println("Cannot load file: " + filename);
        }

        //Load the texture and return
        Texture2D texture = new Texture2D(Texture.BASE_LEVEL,
                Texture.RGBA, image.getWidth(), image.getHeight());
        texture.setImage(0, image);
        return texture;
    }

    private static Material setMaterial() {
        //material from lab 6
        int SH = 10;               // 10
        Material ma = new Material();
        ma.setAmbientColor(new Color3f(0.6f, 0.6f, 0.6f));
        ma.setEmissiveColor(new Color3f(0.0f, 0.0f, 0.0f));
        ma.setDiffuseColor(new Color3f(0.0f, 0.0f, 0.0f));
        ma.setSpecularColor(new Color3f(1f, 1f, 1f));
        ma.setShininess(SH);
        ma.setLightingEnable(true);
        return ma;
    }

}
