import org.jogamp.java3d.*;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class Car {

    public static TransformGroup objectTG;
    private static SoundUtilityJOAL soundJOAL;
    private static final String snd_pt = "Car";
    private static TransformGroup carTF;

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
        objectTG = new TransformGroup();
        objectTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        //adding the cow shape here
        BranchGroup carBG = loadShape();
        Shape3D carShape = (Shape3D) carBG.getChild(0);
        carShape.setUserData(1);
        carShape.setAppearance(setApp(Commons.Grey));
        TransformGroup objectCAR = new TransformGroup();

        objectCAR.addChild(carBG);

        Transform3D rotator = new Transform3D();
        Transform3D rotator2 = new Transform3D();
        Transform3D rotator3 = new Transform3D();
        Transform3D scaler = new Transform3D();
        scaler.setScale(0.6d);
        rotator.rotY(-1.570796);        //this is to rotate around y axis
        rotator2.rotX(-1.570796);        //this is to rotate around z axis
        Transform3D trfm = new Transform3D();
        trfm.set(new Vector3f(0f, 0.06f, 0f));
        trfm.mul(rotator);
        trfm.mul(scaler);
        trfm.mul(rotator2);

        objectCAR.setTransform(trfm);
        carTF = objectCAR;

        objectTG.setUserData(1);

        objectTG.addChild(objectCAR);

        ViewingPlatform ourView = Commons.getSimpleU().getViewingPlatform();
        BehaviorArrowKey myViewRotationbehavior = new BehaviorArrowKey(ourView, objectTG);
        myViewRotationbehavior.setSchedulingBounds(new BoundingSphere());
        objectTG.addChild(myViewRotationbehavior);

        objectBG.addChild(objectTG);

        return objectBG;
    }

    public static class BehaviorArrowKey extends Behavior {
        Point3f viewposi = new Point3f(0.0f, 0.0f, 0.0f);
        private final TransformGroup navigatorTG;
        private WakeupOnAWTEvent wEnter;
        private float angle;
        private final float x;
        private final float y;
        private final float z;
        private final Matrix3f comMat = new Matrix3f();
        //        private final WakeupCondition wakeupCondition;
        private boolean canUpPlay = true;    //these variables prevent sounds from being played multiple times
        private boolean canDownPlay = true;
        private boolean canRPlay = true;
        private boolean canLPlay = true;

        public BehaviorArrowKey(ViewingPlatform targetVP, TransformGroup chasedTG) {
            navigatorTG = chasedTG;
            targetVP.getViewPlatformTransform();
            angle = 0;
            x = 0;
            y = 0;
            z = 0;
        }

        public void initialize() {

            wEnter = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
            wakeupOn(wEnter);                              // decide when behavior becomes live
            comMat.m00 = 1.0f;
            comMat.m01 = 0.0f;
            comMat.m02 = 0.0f;
            comMat.m10 = 0.0f;
            comMat.m11 = 1.0f;
            comMat.m12 = 0.0f;
            comMat.m20 = 0.0f;
            comMat.m21 = 0.0f;
            comMat.m22 = 1.0f;
        }

        @Override
        public void processStimulus(Iterator<WakeupCriterion> criteria) {
            Transform3D navigatorTF = new Transform3D();   // get Transform3D from 'navigatorTG'
            navigatorTG.getTransform(navigatorTF);
            Vector3d vct = new Vector3d();
            navigatorTF.get(vct);

            // soundJOAL.setPos(snd_pt, viewposi.x,  viewposi.y, viewposi.z); //get the xyz of the movement vector and set the sound location to that vector


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

        private Matrix3f coMatrix(int mode, float angle) {
            Matrix3f tempMat = new Matrix3f();
            switch (mode) {
                case 1:
                    tempMat.m00 = (float) Math.cos(angle);
                    tempMat.m01 = 0.0f;
                    tempMat.m02 = (float) Math.sin(angle);
                    tempMat.m10 = 0.0f;
                    tempMat.m11 = 1.0f;
                    tempMat.m12 = 0.0f;
                    tempMat.m20 = (float) -Math.sin(angle);
                    tempMat.m21 = 0.0f;
                    tempMat.m22 = (float) Math.cos(angle);
                    break;
                case 2:
                    tempMat.m00 = 1.0f;
                    tempMat.m01 = 0.0f;
                    tempMat.m02 = 0.0f;
                    tempMat.m10 = 0.0f;
                    tempMat.m11 = (float) Math.cos(angle);
                    tempMat.m12 = (float) Math.sin(angle);
                    tempMat.m20 = 0.0f;
                    tempMat.m21 = (float) -Math.sin(angle);
                    tempMat.m22 = (float) Math.cos(angle);
                    break;
                case 3:
                    tempMat.m00 = (float) Math.cos(angle);
                    tempMat.m01 = (float) -Math.sin(angle);
                    tempMat.m02 = 0.0f;
                    tempMat.m10 = (float) Math.sin(angle);
                    tempMat.m11 = (float) Math.cos(angle);
                    tempMat.m12 = 0.0f;
                    tempMat.m20 = 0.0f;
                    tempMat.m21 = 0.0f;
                    tempMat.m22 = 1.0f;
                    break;
                default:
            }
            return tempMat;
        }

        private Transform3D setRotation3D(TransformGroup Trans, float angle, Matrix3f rotMat, int mode) { // to set the position after rotation
            Transform3D rt3d = new Transform3D();
            Trans.getTransform(rt3d);
            rotMat.transpose();
            rotMat.invert();
            rotMat.mul(rotMat, coMatrix(mode, angle));
            rt3d.setRotation(rotMat);
            Trans.setTransform(rt3d);

            return rt3d;
        }

        private Transform3D setPosition3D(TransformGroup Trans, Point3f point) { // to set the position after movement
            Transform3D t3d = new Transform3D();
            navigatorTG.getTransform(t3d);
            t3d.setTranslation(new Vector3d(point));
            navigatorTG.setTransform(t3d);
            Commons.cam.moveCamera(viewposi.x, viewposi.y + 0.5f, viewposi.z + 3);
            return t3d;
        }

        private void ProcessKeyEvent(AWTEvent[] events) {
            for (AWTEvent event : events) { // iterate through events
                KeyEvent keyEvent = (KeyEvent) event; // set key event
                if (keyEvent.getID() == KeyEvent.KEY_PRESSED) { // see if this is a key down event
                    Transform3D transform1 = new Transform3D();
                    navigatorTG.getTransform(transform1);

                    if (keyEvent.getKeyCode() == KeyEvent.VK_UP) { // check if the key you've pressed is the target key

                        if (canUpPlay) {
                            Sounds.stopSounds(0);  //stop all sounds to avoid overlap
                            Sounds.playSound(1); //play sound
                            canUpPlay = false; //set false to avoid repeating
                        }
                        canDownPlay = true;
                        canRPlay = true;
                        canLPlay = true;

                        viewposi.x = viewposi.x - 3.0f * 0.02f * (float) Math.sin(angle);
                        viewposi.z = viewposi.z - 3.0f * 0.02f * (float) Math.cos(angle);
                        setPosition3D(navigatorTG, viewposi);


                    }

                    if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {

                        if (canLPlay) {
                            Sounds.stopSounds(1);  //stop all sounds to avoid overlap
                            Sounds.playSound(3); //play sound
                            canLPlay = false; //set false to avoid repeating
                        }
                        canDownPlay = true;
                        canUpPlay = true;
                        canRPlay = true;

                        angle += 0.1;
                        setRotation3D(navigatorTG, 0.1f, comMat, 1);

                    }

                    if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {


                        if (canRPlay) {
                            Sounds.stopSounds(1);  //stop all sounds to avoid overlap
                            Sounds.playSound(3); //play sound
                            canRPlay = false; //set false to avoid repeating
                        }
                        canDownPlay = true;
                        canUpPlay = true;
                        canLPlay = true;

                        angle -= 0.1;
                        setRotation3D(navigatorTG, -0.1f, comMat, 1);
                    }

                    if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) { // check if the key you've pressed is the target key


                        if (canDownPlay) {
                            Sounds.stopSounds(0);  //stop all sounds to avoid overlap
                            Sounds.playSound(0); //play sound
                            canDownPlay = false; //set false to avoid repeating
                        }
                        canRPlay = true;
                        canUpPlay = true;
                        canLPlay = true;

                        viewposi.x = viewposi.x + 1.0f * 0.02f * (float) Math.sin(angle) * 3f;
                        viewposi.z = viewposi.z + 1.0f * 0.02f * (float) Math.cos(angle) * 3f;
                        setPosition3D(navigatorTG, viewposi);
                    }


                }

            }
        }
    }
}
