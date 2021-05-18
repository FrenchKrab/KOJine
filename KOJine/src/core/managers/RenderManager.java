package core.managers;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;

import dataTypes.Vector2;
import rendering.ShapeRenderRequest;
import rendering.RenderOutput;
import rendering.RenderRequest;
import rendering.RenderRequest.DrawType;
import sun.java2d.loops.DrawRect;

/**
 * Manager responsible of handling all render related requests
 */
public class RenderManager {
	
	public RenderManager(RenderOutput renderOutput)
	{
		setRenderOutput(renderOutput);
	}
	
	/**
	 * The plugged render output, responsible for display.
	 */
	private RenderOutput renderOutput;
	
	/**
	 * All received RenderRequest on this frame
	 */
	private ArrayList<RenderRequest> drawRequests = new ArrayList<RenderRequest>();
	
	/**
	 * All received camera draw requests on this frame
	 */
	private ArrayList<AffineTransform> cameraRenderRequest = new ArrayList<AffineTransform>();
	
	/**
	 * Define the new output strategy
	 * @param renderOutput
	 */
	public void setRenderOutput(RenderOutput renderOutput)
	{
		this.renderOutput = renderOutput;
	}
	
	/**
	 * Call this after every screen draw, cleans all draw requests received
	 */
	public void clearRequests()
	{
		drawRequests.clear();
		cameraRenderRequest.clear();
	}
	
	/**
	 * Add a draw request to the drawRequests list
	 * @param request The request to add
	 */
	public void addRequest(RenderRequest request)
	{
		drawRequests.add(request);
	}
	
	
	/**
	 * Renders a region of the world on screen
	 * @param regionTransform
	 */
	private void renderRegion(AffineTransform regionTransform)
	{
		ArrayList<RenderRequest> transformedRequests = new ArrayList<>();
		AffineTransform inverseCamera = regionTransform;
		try {
			inverseCamera = regionTransform.createInverse();
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		
		Vector2 windowSize = renderOutput.getWindowSize();
		AffineTransform offset = new AffineTransform();
		offset.setToScale(1, 1);
		offset.translate(windowSize.x/2, windowSize.y/2);
		
		Rectangle boundingBoxScreenSpace = new Rectangle(0, 0, (int)windowSize.x, (int)windowSize.y);
		Rectangle boundingBox = null;
		try {
			boundingBox = regionTransform.createTransformedShape(
					offset.createInverse().createTransformedShape(new Rectangle(0,0,(int)windowSize.x,(int)windowSize.y)))
					.getBounds();
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Do basic frustrum culling when possible
		for(RenderRequest req : drawRequests)
		{
			boolean inBoundingBox = false;
			//Try calculating if needs render with frustrum culling
			if(req.getRequestType() == DrawType.SHAPE)
			{
				ShapeRenderRequest shapeReq = (ShapeRenderRequest)req;
				if(req.isScreenSpace())	//If the req is in screen space
				{
					if(shapeReq.getShape().getBounds().intersects(boundingBoxScreenSpace))
						inBoundingBox = true;
				}
				else //If the req is in world space
				{
					if(shapeReq.getShape().getBounds().intersects(boundingBox))
						inBoundingBox = true;

				}
			}
			//If can't calculate frustrum culling, consider it needs to be displayed
			else
			{
				inBoundingBox = true;
			}
			
			//If the request is located in the camera viewport
			if(inBoundingBox)
			{
				RenderRequest transformedReq = req.clone();
				
				//If isn't in screen space, apply camera transform
				if(!req.isScreenSpace())
				{
					transformedReq.applyCameraTranform(inverseCamera);
					transformedReq.applyCameraTranform(offset);
				}
				//add to list of transformed requests to display
				transformedRequests.add(transformedReq);
			}
		}
		
		renderOutput.renderRegion(transformedRequests, regionTransform);
	}

	/**
	 * Add a camera to render from.
	 * @param camera
	 */
	public void addCameraRenderRequest(AffineTransform camera)
	{
		cameraRenderRequest.add(camera);
	}
	
	/**
	 * Render all cameras.
	 */
	public void renderAll()
	{
		
		for(AffineTransform draw : cameraRenderRequest)
		{
			renderRegion(draw);
		}
	}
	
}
