package rendering;

import java.awt.Shape;
import java.awt.geom.AffineTransform;

import rendering.RenderRequest.DrawType;

/**
 * RenderRequest to draw a text
 */
public class TextRenderRequest extends RenderRequest {
	

	public TextRenderRequest(String text, FontOptions font, AffineTransform transform, boolean screenSpace)
	{
		super(DrawType.TEXT, screenSpace);
		this.text = text;
		this.font = font;
		this.transform = transform;
	}
	
	
	private String text = "";
	private FontOptions font = null;
	private AffineTransform transform;
	
	public String getText()
	{
		return this.text;
	}
	
	public FontOptions getFontOptions()
	{
		return this.font;
	}
	

	public AffineTransform getTransform()
	{
		return this.transform;
	}
	
	@Override
	public RenderRequest clone() {
		return new TextRenderRequest(text, font.clone(), new AffineTransform(this.transform), isScreenSpace());
	}
	
	
	/**
	 * TODO: fully implement (doens't support rotation)
	 */
	@Override
	public void applyCameraTranform(AffineTransform camera) {
		//MAYBE WRONG
		AffineTransform cameraNew = new AffineTransform(camera);
		cameraNew.concatenate(transform);
		transform = cameraNew;
	}
}
