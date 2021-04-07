import org.jogamp.java3d.*;

import java.util.Iterator;

/* This behavior of collision detection highlights the
    object when it is in a state of collision. */
public class FinishLineBoundry extends Behavior {
    public static boolean isInCollisionRecently;
    public static boolean inCollision;
    private final Shape3D shape;
    private final Texture shapeTexture;
    private final Appearance shapeAppearance;
    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;

    public FinishLineBoundry(Shape3D s) {
        shape = s; // save the original color of 'shape"
        shapeAppearance = shape.getAppearance();
        shapeTexture = shapeAppearance.getTexture();
        inCollision = false;
        isInCollisionRecently = false;
    }

    public void initialize() { // USE_GEOMETRY USE_BOUNDS
        wEnter = new WakeupOnCollisionEntry(shape, WakeupOnCollisionEntry.USE_GEOMETRY);
        wExit = new WakeupOnCollisionExit(shape, WakeupOnCollisionExit.USE_GEOMETRY);
        wakeupOn(wEnter); // initialize the behavior
    }

    public void processStimulus(Iterator<WakeupCriterion> criteria) {
        inCollision = !inCollision; // collision has taken place

        if (inCollision) { // change color to highlight 'shape'
            try { //Strange error where it thinks appearance wasnt set
                Commons.client.stopCounting();
            } catch (CapabilityNotSetException e) {
            }
            wakeupOn(wExit); // keep the color until no collision
        } else { // change color back to its original
            try {//Strange error where it thinks appearance wasnt set

            } catch (CapabilityNotSetException e) {
            }
            wakeupOn(wEnter); // wait for collision happens
        }
    }
}