import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.Primitive;

import java.util.Iterator;

/* This behavior of collision detection highlights the
    object when it is in a state of collision. */
public class CrashingBoundaries extends Behavior {
    public static boolean isInCollisionRecently;
    public static boolean inCollision;
    private final Primitive shape;
    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;

    public CrashingBoundaries(Primitive s) {
        shape = s; // save the original color of 'shape"
        inCollision = false;
        isInCollisionRecently = false;
    }

    public void initialize() { // USE_GEOMETRY USE_BOUNDS
        wEnter = new WakeupOnCollisionEntry(shape, WakeupOnCollisionEntry.USE_GEOMETRY);
        wExit = new WakeupOnCollisionExit(shape, WakeupOnCollisionExit.USE_GEOMETRY);
        wakeupOn(wEnter); // initialize the behavior
    }

    public void processStimulus(Iterator<WakeupCriterion> criteria) {
        Texture grid = GroundAndBackground.texturedApp("grid");
        inCollision = !inCollision; // collision has taken place

        if (inCollision) { // change color to highlight 'shape'
            //Car.BehaviorArrowKey.setPosition3D(Car.BehaviorArrowKey.navigatorTG, Car.BehaviorArrowKey.viewposiPrevious);
            //isInCollisionRecently= !isInCollisionRecently;
            System.out.println("Hit boundry");
            wakeupOn(wExit); // keep the color until no collision
        } else { // change color back to its original
            wakeupOn(wEnter); // wait for collision happens
        }
    }
}
