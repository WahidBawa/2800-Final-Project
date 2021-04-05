import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3f;

public class StreetLights {
    public static TransformGroup createStreetLight() {
        TransformGroup baseMain = new TransformGroup();

        //the lamps are just cylinders with an opaque sphere with a smaller white sphere inside

        //CREATE CYLINDER
        Appearance app = new Appearance();
        ColoringAttributes colorCylinder = new ColoringAttributes(Commons.Grey, ColoringAttributes.FASTEST);
        app.setColoringAttributes(colorCylinder); //after setting color we set the appearance
        TransformGroup cylinderShape = new TransformGroup();
        cylinderShape.addChild(new Cylinder(0.04f, 1f, app));

        Transform3D translator = new Transform3D();
        translator.setTranslation(new Vector3f(0, 1.0f, 0)); //this vector will place the cylinder above y=0
        Transform3D cylinderMatrix = new Transform3D(); //this is the matrix used to move the cylinder
        cylinderMatrix.mul(translator); //translating the cylinder
        cylinderShape.setTransform(cylinderMatrix);

        //CREATE SPHERES
        TransformGroup spheres = new TransformGroup();

        //There are two light sources, one above and one below the diameters of the spheres, light interact from within the spheres
        PointLight pointLight = new PointLight(true, Commons.White, new Point3f(0, 1, 0), new Point3f(2f, 0, 0));
        pointLight.setInfluencingBounds(new BoundingSphere(new Point3d(0,0,0), 10f));
        spheres.addChild(pointLight); //add the light to center source

        PointLight pointLight2 = new PointLight(true, Commons.White, new Point3f(0, -1, 0), new Point3f(2f, 0, 0));
        pointLight2.setInfluencingBounds(new BoundingSphere(new Point3d(0,0,0), 10f));
        spheres.addChild(pointLight2); //add the light to center source

        spheres.addChild(Road.materializeSphere(0.2f, Commons.Grey, true));
        spheres.addChild(Road.materializeSphere(0.08f, Commons.Yellow, false));
        Transform3D translatorSphere = new Transform3D();
        translatorSphere.setTranslation(new Vector3f(0, 1.5f, 0)); //this vector will place the cylinder above y=0
        Transform3D sphereMatrix = new Transform3D(); //this is the matrix used to move the cylinder
        sphereMatrix.mul(translatorSphere); //translating the cylinder
        spheres.setTransform(sphereMatrix);

        baseMain.addChild(spheres);
        baseMain.addChild(cylinderShape);
        return baseMain;
    }

    public static TransformGroup createLamps(){
        TransformGroup baseMain= new TransformGroup();

        //create n lamps and place them in order down the columns
        //right side
        for (int i=-1; i<=16; i++) {
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
        for (int i=-1; i<=16; i++) {
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
}
