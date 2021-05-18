package gameComponents;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import core.Component;
import core.components.PolygonRenderer;
import dataTypes.Vector2;
import main.HelperMethods;
import main.Living;
import rendering.ColorFill;

/**
 * Barre de vie suivant un gameobject 
 */
public class Lifebar extends Component
{
	public Lifebar()
	{
		
	}
	
	public <T extends Component & Living> Lifebar(T target, Color colorEmpty, Color colorFull, Vector2 size, Vector2 offset)
	{
		attach = new AttachComponent(target.getOwner(), true, offset, false, 0);
		this.target = target;
		this.size = size;
		this.colorEmpty = colorEmpty;
		this.colorFull = colorFull;
	}
		
	public Vector2 size = new Vector2(50,5);
	
	public Color colorFull = new Color(0,255,0);
	public Color colorEmpty = new Color(255,0,0);
	public boolean vertical = false;
	
	public AttachComponent attach;
	public Living target;
	
	private PolygonRenderer lifebar;
	
	@Override
	public void start() {
		super.start();
		
		getOwner().addComponent(attach);
		
		lifebar = new PolygonRenderer();
		getOwner().addComponent(lifebar);
	}
	
	@Override
	public void update(double delta) {
		super.update(delta);
		
		if(target != null)
		{
			float lifePercent = ((float)target.getLife()/target.getMaxLife());
			if(vertical)
				lifebar.shape = new Rectangle2D.Double(0, 0, size.x, size.y*lifePercent);
			else
				lifebar.shape = new Rectangle2D.Double(0, 0, size.x*lifePercent, size.y);
			lifebar.fill = new ColorFill(HelperMethods.lerpColor(colorEmpty, colorFull, lifePercent));
		}

	}
	
}
