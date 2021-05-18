package rendering;

import java.awt.geom.AffineTransform;

/**
 * A class that transmits draw requests from components to the rendering manager
 */
public abstract class RenderRequest {
	
	protected RenderRequest(DrawType type, boolean screenSpace)
	{
		this.type = type;
		this.screenSpace = screenSpace;
	}
	
	public enum DrawType
	{
		SHAPE,
		TEXT
	};

	/**
	 * Are coordinates/etc relative to the screen ? (mostly used for UI)
	 * It means the cameraTransform won't be applied when displaying this request
	 */
	private boolean screenSpace = false;
	

	private DrawType type;
	
	/**
	 * The type of the request (shape, text, ..?)
	 * @return
	 */
	public DrawType getRequestType()
	{
		return this.type;
	}
	
	/**
	 * Must this request be drawn in screen space (or world space) ?
	 * @return
	 */
	public boolean isScreenSpace()
	{
		return this.screenSpace;
	}
	
	/**
	 * Creates a copy of the request
	 */
	public abstract RenderRequest clone();
	
	/**
	 * Apply the camera transform on the element to draw
	 * @param camera
	 */
	public abstract void applyCameraTranform(AffineTransform camera);
}
