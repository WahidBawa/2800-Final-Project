import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.vecmath.Vector3f;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

public class Tree {

    public Tree(TransformGroup objectTG) {

        TransformGroup transformGroup = new TransformGroup(); // we will rotate the loaded obj inside of this group
        Transform3D transform = new Transform3D(); // use this to apply the transformations to the transform group
        transform.setTranslation(new Vector3f(0,1.5f,-15));
        transformGroup.setTransform(transform); // apply the transformation to the transform group

        ObjectFile objectFile = new ObjectFile(ObjectFile.STRIPIFY | ObjectFile.TRIANGULATE | ObjectFile.RESIZE); // used to load in the obj file

        try {
            Scene s = objectFile.load(new File("Assets/tree.obj").toURI().toURL()); // load in the file

            BranchGroup objBG = s.getSceneGroup();
            Shape3D tree = (Shape3D) objBG.getChild(0); // get the shape3d object from the obj

            objBG.removeAllChildren();
            transformGroup.addChild(tree);
            objectTG.addChild(transformGroup);

        } catch (FileNotFoundException | MalformedURLException e) { // if there is an error
            System.err.println(e);
            System.exit(1);
        }

    }
}

