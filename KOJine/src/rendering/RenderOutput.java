package rendering;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import dataTypes.Vector2;

/**
 * An interface to represent an object that can be given the task to
 * render what's on screen from a {@link RenderRequest} list.
 */
public interface RenderOutput {
	
	public void renderRegion(ArrayList<RenderRequest> requests, AffineTransform regionTransform);
	
	public Vector2 getWindowSize();
}
