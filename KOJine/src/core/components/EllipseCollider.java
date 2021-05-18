package core.components;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/**
 * A component that handles collision detection for ellipse shaped collider (circle/ellipse)
 */
public class EllipseCollider extends ColliderComponent{

	public EllipseCollider ()
	{
		this.ellipse = new Ellipse2D.Double(0, 0, 1, 1);
	}
	
	public EllipseCollider(double x, double y, double w, double h)
	{
		this.ellipse = new Ellipse2D.Double(x-w, y-h, x+w, y+h);
	}
	
	public EllipseCollider(Ellipse2D ellipse)
	{
		this.ellipse = ellipse;
	}
	
	/**
	 * The ellipse collision shape (local space)
	 */
	public Ellipse2D ellipse; 
	
	private Spatial2D spatial;
	
	
	@Override
	public void start() {
		super.start();
		spatial = this.getOwner().getOrCreateComponent(Spatial2D.class);
	}
	
	
	@Override
	public Area getArea() {
		//If component has the rectangle data 
		if(ellipse != null)
		{
			//If gameobject has a spatial  
			if(spatial != null)
			{
				Area a = new Area(ellipse);
				a.transform(spatial.getTransform());
				return a;
			}
			else
			{
				//Get or create the needed spatial
				spatial = this.getOwner().getOrCreateComponent(Spatial2D.class);
			}
		}
		//If we're there, we couldn't get an area. Return null
		return null;
	}

}
