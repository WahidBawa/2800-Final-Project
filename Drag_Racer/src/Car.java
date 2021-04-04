import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class Car {

    private static SoundUtilityJOAL soundJOAL;
    private static String snd_pt = "Car";
    private static TransformGroup carTF;

    public static class BehaviorArrowKey extends Behavior {
        private TransformGroup navigatorTG;
        private WakeupOnAWTEvent wEnter;
//        private final WakeupCriterion[] wakeupCriteria;
        private final WakeupCondition wakeupCondition;

        public BehaviorArrowKey(ViewingPlatform targetVP, TransformGroup chasedTG) {
            navigatorTG = chasedTG;
            targetVP.getViewPlatformTransform();

            wakeupCondition = new WakeupOr(new WakeupCriterion[]{new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED), new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED)});
        }

        public void initialize() {
            wEnter = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
            wakeupOn(wEnter);                              // decide when behavior becomes live
        }

        @Override
        public void processStimulus(Iterator<WakeupCriterion> criteria) {
            Transform3D navigatorTF = new Transform3D();   // get Transform3D from 'navigatorTG'
            navigatorTG.getTransform(navigatorTF);
            Vector3d vct = new Vector3d();
            navigatorTF.get(vct);

//            soundJOAL.setPos(snd_pt, (float)vct.x,  (float)vct.y, (float) vct.z); //get the xyz of the movement vector and set the sound location to that vector


            WakeupOnAWTEvent event;
            WakeupCriterion genericEvent;
            AWTEvent[] events;

            while (criteria.hasNext()) { // while there is another wakeup criteria
                genericEvent = criteria.next();

                if (genericEvent instanceof WakeupOnAWTEvent) { // if generic event is instance of wakeupEvent
                    event = (WakeupOnAWTEvent) genericEvent;
                    events = event.getAWTEvent();
                    ProcessKeyEvent(events);

                }
            }

            wakeupOn(wEnter);                              // decide when behavior becomes live
        }


        private void ProcessKeyEvent(AWTEvent[] events) {
            for (AWTEvent event : events) { // iterate through events
                KeyEvent keyEvent = (KeyEvent) event; // set key event
                if (keyEvent.getID() == KeyEvent.KEY_PRESSED) { // see if this is a key down event
                    if (keyEvent.getKeyCode() == KeyEvent.VK_UP) { // check if the key you've pressed is the target key
                        Transform3D transform1 = new Transform3D();
                        navigatorTG.getTransform(transform1);
                        Vector3f vector3f = new Vector3f();

                        transform1.get(vector3f);

                        transform1.set(new Vector3f(vector3f.x, vector3f.y, vector3f.z - 0.1f));

                        navigatorTG.setTransform(transform1);
                    }
                }

            }
        }
    }

    private static BranchGroup loadShape() {
        int flags = ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY;
        ObjectFile f = new ObjectFile(flags, (float) (60 * Math.PI / 180.0));
        Scene s = null;
        try {
            s = f.load("Assets/Car.obj");
        } catch (FileNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        } catch (ParsingErrorException e) {
            System.err.println(e);
            System.exit(1);
        } catch (IncorrectFormatException e) {
            System.err.println(e);
            System.exit(1);
        }
        return s.getSceneGroup();
    }

    public static Material setMaterialCar(Color3f clr) {
        //material from lab 6
        int SH = 2;               // 10
        Material ma = new Material();
        ma.setAmbientColor(clr);
        ma.setEmissiveColor(clr);
        ma.setDiffuseColor(clr);
        ma.setSpecularColor(clr);
        ma.setShininess(SH);
        ma.setLightingEnable(true);
        return ma;
    }

    private static Appearance setApp(Color3f clr) {
        Appearance app = new Appearance();
        app.setMaterial(setMaterialCar(Commons.Grey));
        ColoringAttributes colorAtt = new ColoringAttributes();
        colorAtt.setColor(clr);
        app.setColoringAttributes(colorAtt);
        return app;
    }

    public static BranchGroup carObject() {
        BranchGroup objectBG = new BranchGroup();
        TransformGroup objectTG = new TransformGroup();
        objectTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        //adding the cow shape here
        BranchGroup carBG = loadShape();
        Shape3D carShape= (Shape3D)carBG.getChild(0);
        carShape.setAppearance(setApp(Commons.Grey));
        TransformGroup objectCAR = new TransformGroup();

        objectCAR.addChild(carBG);

        Transform3D rotator = new Transform3D();
        Transform3D rotator2 = new Transform3D();
        Transform3D rotator3 = new Transform3D();
        Transform3D scaler = new Transform3D();
        scaler.setScale(0.6d);
        rotator.rotY(-1.570796);		//this is to rotate around y axis
        rotator2.rotX(-1.570796);		//this is to rotate around z axis
        Transform3D trfm= new Transform3D();
        trfm.mul(rotator);
        trfm.mul(scaler);
        trfm.mul(rotator2);

        objectCAR.setTransform(trfm);
        carTF = objectCAR;

        objectTG.addChild(objectCAR);

//        soundJOAL = new SoundUtilityJOAL();
//        if (!soundJOAL.load(snd_pt, 0f, 0f, 10f, true))     // fix 'snd_pt' at cow location
//            System.out.println("Could not load " + snd_pt);
//        else
//            soundJOAL.play(snd_pt);                         // start 'snd_pt'



        ViewingPlatform ourView = Commons.getSimpleU().getViewingPlatform();
//        KeyNavigatorBehavior myRotationbehavior = new KeyNavigatorBehavior(objectTG);

        BehaviorArrowKey myViewRotationbehavior = new BehaviorArrowKey(ourView, objectTG);

//        myRotationbehavior.setSchedulingBounds(new BoundingSphere());
//        myRotationbehavior.setSchedulingInterval(myRotationbehavior.getNumSchedulingIntervals()-1);
//        objectTG.addChild(myRotationbehavior);

        myViewRotationbehavior.setSchedulingBounds(new BoundingSphere());
        objectTG.addChild(myViewRotationbehavior);

        objectBG.addChild(objectTG);

        return objectBG;
    }
}
