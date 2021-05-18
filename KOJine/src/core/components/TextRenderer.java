package core.components;

import java.awt.Shape;
import java.awt.geom.AffineTransform;

import core.Component;
import rendering.FontOptions;
import rendering.ShapeRenderRequest;
import rendering.TextRenderRequest;

/**
 * A component that renders a text on screen
 */
public class TextRenderer extends Component 
{
	public TextRenderer()
	{}
	
	public TextRenderer(String text, FontOptions font, boolean screenSpace)
	{
		this.text = text;
		this.font = font;
		this.screenSpace = screenSpace;
	}
	
	/**
	 * Text to display
	 */
	public String text="";
	
	/**
	 * Font options of this shape: how will the font look like
	 */
	public FontOptions font = null;
	
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
	protected void sendRenderRequest()
	{
		TextRenderRequest request = new TextRenderRequest(text, font, new AffineTransform(spatial.getTransform()), screenSpace);
		this.getOwner().getWorld().getRenderManager().addRequest(request);
	}
}
