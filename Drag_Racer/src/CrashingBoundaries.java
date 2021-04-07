import org.jogamp.java3d.*;

import java.util.Iterator;

/* This behavior of collision detection highlights the
    object when it is in a state of collision. */
public class CrashingBoundaries extends Behavior {
    private final Shape3D shape;
    private final Texture shapeTexture;
    private final Appearance shapeAppearance;
    public static boolean isInCollisionRecently;
    public static boolean inCollision;
    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;

    public CrashingBoundaries(Shape3D s) {
        shape = s; // save the original color of 'shape"
        shapeAppearance = shape.getAppearance();
        shapeTexture = shapeAppearance.getTexture();
        inCollision = false;
        isInCollisionRecently= false;
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
            try { //Strange error where it thinks appearance wasnt set
                shapeAppearance.setTexture(grid);
//                Car.BehaviorArrowKey.setPosition3D(Car.BehaviorArrowKey.navigatorTG, Car.BehaviorArrowKey.viewposiPrevious);
//                isInCollisionRecently= !isInCollisionRecently;
            } catch (CapabilityNotSetException e) {
            }
            wakeupOn(wExit); // keep the color until no collision
        } else { // change color back to its original
            try {//Strange error where it thinks appearance wasnt set
                shapeAppearance.setTexture(shapeTexture);

            } catch (CapabilityNotSetException e) {
            }
            wakeupOn(wEnter); // wait for collision happens
        }
    }
}
