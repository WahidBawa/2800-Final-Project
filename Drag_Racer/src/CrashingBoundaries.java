
import org.jogamp.java3d.*;
import org.jogamp.vecmath.Color3f;

import java.util.Iterator;

/* This behavior of collision detection highlights the
    object when it is in a state of collision. */
public class CrashingBoundaries extends Behavior {
    private boolean inCollision;
    private final Shape3D shape;
    private final ColoringAttributes shapeColoring;
    private final Appearance shapeAppearance;
    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;

    public CrashingBoundaries(Shape3D s) {
        shape = s; // save the original color of 'shape"
        shapeAppearance = shape.getAppearance();
        shapeColoring = shapeAppearance.getColoringAttributes();
        inCollision = false;
    }

    public void initialize() { // USE_GEOMETRY USE_BOUNDS
        wEnter = new WakeupOnCollisionEntry(shape, WakeupOnCollisionEntry.USE_GEOMETRY);
        wExit = new WakeupOnCollisionExit(shape, WakeupOnCollisionExit.USE_GEOMETRY);
        wakeupOn(wEnter); // initialize the behavior
    }

    public void processStimulus(Iterator<WakeupCriterion> criteria) {
        Color3f hilightClr = Commons.Green;
        ColoringAttributes highlight = new ColoringAttributes(hilightClr, ColoringAttributes.SHADE_GOURAUD);
        inCollision = !inCollision; // collision has taken place

        if (inCollision) { // change color to highlight 'shape'
            shapeAppearance.setColoringAttributes(highlight);


            wakeupOn(wExit); // keep the color until no collision
        } else { // change color back to its original
            shapeAppearance.setColoringAttributes(shapeColoring);
            wakeupOn(wEnter); // wait for collision happens
        }
    }
}
