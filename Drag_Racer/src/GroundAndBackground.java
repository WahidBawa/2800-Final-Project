import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Vector3f;

import java.util.Iterator;

public class GroundAndBackground{
    public static TransformGroup generateCylinder(int size, String file){
        //Transform the object to the correct location
        Transform3D transform3D = new Transform3D();
        transform3D.setTranslation(new Vector3f(0,1.5f,-15));
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
    private static void baseCylinder(int size, TransformGroup scene){
        //Create the bases and translate to the correct location
        Transform3D transform3D = new Transform3D();
        transform3D.setTranslation(new Vector3f(0,0.2f,0));
        TransformGroup botBase = new TransformGroup(transform3D);
        transform3D.setTranslation(new Vector3f(0,-0.2f,0));
        TransformGroup topBase = new TransformGroup(transform3D);

        //Create the object and rotate
        TransformGroup rotation = new TransformGroup();
        Cylinder c = new Cylinder(size*1.05f, 0.1f, Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS | Primitive.ENABLE_APPEARANCE_MODIFY, setApp("neon"));
        rotation.addChild(c);
        botBase.addChild(rotation);
        botBase.addChild(Commons.rotateBehavior(10000, rotation));

        //Create the object and rotate
        rotation = new TransformGroup();
        c = new Cylinder(size*1.05f, 0.1f, Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS | Primitive.ENABLE_APPEARANCE_MODIFY, setApp("neon"));
        rotation.addChild(c);
        topBase.addChild(rotation);
        topBase.addChild(Commons.rotateBehavior(10000, rotation));

        //Add to the outer scene
        scene.addChild(topBase);
        scene.addChild(botBase);
    }

    public static TransformGroup generateGround(int size, float h){
        TransformGroup scene_TG = new TransformGroup();
        //Loop to tile the ground
        for(int i = 0; i < size; i++){
            scene_TG.addChild(ground(new Vector3f(5f, h, 5f), -(i*10), -10));
            scene_TG.addChild(ground(new Vector3f(5f, h, 5f), -(i*10), 10));
            scene_TG.addChild(ground(new Vector3f(5f, h, 5f), -(i*10), 0));
            scene_TG.addChild(sphere(8, 60, 10, -(i*10)));
            scene_TG.addChild(sphere(8, 60, -10, -(i*10)));
        }
        return scene_TG;
    }
    private static Shape3D ground (Vector3f scale, int pos, int w){
        //Creates all the lines needed for the Ground
        float uv0[] = {0f, 0f};
        float uv1[] = {1f, 0f};
        float uv2[] = {1f, 1f};
        float uv3[] = {0f, 1f};

        //Setting the coords and the texture of the flat surface
        QuadArray square = new QuadArray(4, QuadArray.COLOR_3 | QuadArray.COORDINATES| QuadArray.TEXTURE_COORDINATE_2);
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
    private static TransformGroup sphere(int n, int d, float x, float z){
        //set the sphere to the correct location
        Transform3D move = new Transform3D();
        move.setTranslation(new Vector3f(x,-6,z));
        TransformGroup TG = new TransformGroup(move);

        //Create the sphere
        Sphere s = new Sphere(n, Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS | Primitive.ENABLE_APPEARANCE_MODIFY, d);

        //Apply to the sphere and attach to the base
        s.setAppearance(setApp("grid"));
        TG.addChild(s);

        //Return
        return TG;
    }

    private static Appearance setApp(String texture) {
        Appearance app = new Appearance();
        app.setTexture(texturedApp(texture));
        app.setMaterial(setMaterial());
        return app;
    }

    private static Texture texturedApp (String name){
        //Load the image
        String filename = "Resources/textures/" + name + ".jpg";
        TextureLoader loader = new TextureLoader(filename, null);
        ImageComponent2D image = loader.getImage();

        //Check if the image was not found
        if(image == null){
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
