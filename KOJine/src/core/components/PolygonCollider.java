package core.components;

import java.awt.Polygon;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class PolygonCollider extends ColliderComponent {
	
	public Polygon polygon;

	private Spatial2D spatial;
	
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		spatial = this.getOwner().getOrCreateComponent(Spatial2D.class);
	}
	
	@Override
	public Area getArea() 
	{
		//If component has the rectangle data 
		if(polygon != null)
		{
			//If gameobject has a spatial  
			if(spatial != null)
			{
				Area a = new Area(polygon);
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
