package rendering;

import java.awt.geom.AffineTransform;

/**
 * Abstract class that contains options for filling a drawed element
 */
public abstract class FillOptions {

	/**
	 * Create a copy of this and transform the filling according to the transform
	 * @param transform
	 * @return
	 */
	public abstract FillOptions createTransformed(AffineTransform transform);
	
	/**
	 * Transform the filling according to the transform
	 * @param transform
	 */
	public abstract void applyTransform(AffineTransform transform);
	
	/**
	 * Create a copy
	 */
	public abstract FillOptions clone();
	
}
