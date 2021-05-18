package core.components;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.image.AffineTransformOp;

import core.Component;
import rendering.ColorFill;
import rendering.FillOptions;
import rendering.ShapeRenderRequest;
import rendering.StrokeOptions;

/**
 * A component that renders a polygon on screen
 */
public class PolygonRenderer extends Component {
	
	/**
	 * The shape to render
	 */
	public Shape shape = null;
	
	/**
	 * FillOptions of this shape: how will the shape be filled
	 */
	public FillOptions fill = null;
	
	/**
	 * Are coordinates of the spatial relative to the world or to screenspace (=~ world object or GUI ?)
	 */
	public boolean screenSpace = false;
	
	/**
	 * Spatial belonging to this component's GameObject
	 */
	private Spatial2D spatial;
	

	@Override
	public void start() {
		super.start();
		spatial = getOwner().getOrCreateComponent(Spatial2D.class);
	}
	
	@Override
	public void update(double delta)
	{
		sendRenderRequest();
	}
	
	/**
	 * Sends a render request to the render manager
	 */
	private void sendRenderRequest()
	{
		Shape transformedShape = this.shape;

		if(spatial != null)
		{
			transformedShape = spatial.getTransform().createTransformedShape(transformedShape);
		}

		ShapeRenderRequest request = new ShapeRenderRequest(transformedShape, new StrokeOptions(), this.fill, screenSpace);
		this.getOwner().getWorld().getRenderManager().addRequest(request);
	}

}
