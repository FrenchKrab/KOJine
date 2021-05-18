package rendering;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;


/**
 * A render request that can render any type of geometrical shape
 */
public class ShapeRenderRequest extends RenderRequest {
	
	public ShapeRenderRequest (Shape shape,
							   StrokeOptions stroke,
							   FillOptions fill, boolean screenSpace)
	{
		super(DrawType.SHAPE, screenSpace);
		this.shape = shape;
		this.stroke = stroke;
		this.fill = fill;
	}

	
	private Shape shape;
	private StrokeOptions stroke;
	private FillOptions fill;
	
	
	
	public Shape getShape()
	{
		return this.shape;
	}
	
	public FillOptions getFillOptions()
	{
		return this.fill;
	}
	
	@Override
	public RenderRequest clone() {
		StrokeOptions strokeCloned = stroke.clone();
		FillOptions fillCloned = fill.clone();
		return new ShapeRenderRequest(this.shape, strokeCloned, fillCloned, this.isScreenSpace());
	}

	@Override
	public void applyCameraTranform(AffineTransform camera) {
		this.fill = fill.createTransformed(camera);
		this.shape = camera.createTransformedShape(this.shape);

	}
}
