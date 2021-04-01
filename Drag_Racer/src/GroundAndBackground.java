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
        Transform3D transform3D = new Transform3D();
        transform3D.setTranslation(new Vector3f(0,1.5f,-10));
        TransformGroup scene_TG = new TransformGroup(transform3D);

        TransformGroup banner = new TransformGroup(transform3D);
        Cylinder c = new Cylinder(size, 0.5f, Primitive.GENERATE_TEXTURE_COORDS, setApp("Text"));
        banner.addChild(c);
        scene_TG.addChild(banner);

        scene_TG.addChild(Commons.rotateBehavior(10000, banner));

        baseCylinder(size, scene_TG);

        return scene_TG;
    }
    private static void baseCylinder(int size, TransformGroup scene){


        Transform3D transform3D = new Transform3D();
        transform3D.setTranslation(new Vector3f(0,0.2f,0));
        TransformGroup botBase = new TransformGroup(transform3D);
        transform3D.setTranslation(new Vector3f(0,-0.2f,0));
        TransformGroup topBase = new TransformGroup(transform3D);

        TransformGroup rotation = new TransformGroup();
        Cylinder c = new Cylinder(size*1.05f, 0.1f, Primitive.GENERATE_TEXTURE_COORDS, setApp("neon"));
        rotation.addChild(c);
        botBase.addChild(rotation);
        botBase.addChild(Commons.rotateBehavior(10000, rotation));

        rotation = new TransformGroup();
        c = new Cylinder(size*1.05f, 0.1f, Primitive.GENERATE_TEXTURE_COORDS, setApp("neon"));
        rotation.addChild(c);
        topBase.addChild(rotation);
        topBase.addChild(Commons.rotateBehavior(10000, rotation));

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
            scene_TG.addChild(sphere(8, 30, 10, -(i*10)));
            scene_TG.addChild(sphere(8, 30, -10, -(i*10)));
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

        return new Shape3D(square, setApp("grid"));
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

    private static Material setMaterial(){
        Material newMaterial = new Material();
        //Gray color
        newMaterial.setAmbientColor(new Color3f(0.6f, 0.6f, 0.6f));
        //Black color
        newMaterial.setEmissiveColor(new Color3f(0, 0,0));
        //Gray
        newMaterial.setDiffuseColor(new Color3f(0.6f, 0.6f, 0.6f));
        //Black
        newMaterial.setSpecularColor(new Color3f(1f, 1f, 1f));
        //Create the shininess of the object
        newMaterial.setShininess(128);
        newMaterial.setLightingEnable(true);
        return newMaterial;
    }

}
