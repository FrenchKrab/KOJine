package core.components;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/**
 * A component that handles collision detection for box shaped collider (rectangles, squares, etc) (not tilted)
 */
public class BoxCollider extends ColliderComponent {

	public BoxCollider()
	{
		this.box = new Rectangle(-1,-1,2,2);
	}
	
	public BoxCollider(int x, int y, int w, int h)
	{
		this.box = new Rectangle(x,y,w,h);
	}
	
	/**
	 * The collision box (local space)
	 */
	public Rectangle2D box;
	
	/**
	 * Spatial belonging to this component's GameObject
	 */
	private Spatial2D spatial;
	
	
	@Override
	public void start() {
		super.start();
		spatial = this.getOwner().getOrCreateComponent(Spatial2D.class);

	}
	
	@Override
	public Area getArea() {
		//If component has the rectangle data 
		if(box != null)
		{
			//If gameobject has a spatial  
			if(spatial != null)
			{
				Area a = new Area(box);
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
