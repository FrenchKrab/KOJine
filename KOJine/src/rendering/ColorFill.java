package rendering;

import java.awt.Color;
import java.awt.geom.AffineTransform;

public class ColorFill extends FillOptions {
	public ColorFill()
	{
		super();
		this.color = new Color(127);
	}
	public ColorFill(Color color)
	{
		super();
		this.color = color;
	}
	
	public Color color;

	@Override
	public FillOptions clone() {
		Color c = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
		return new ColorFill(c);
	}
	
	@Override
	public FillOptions createTransformed(AffineTransform transform) {
		return this;
	}
	
	@Override
	public void applyTransform(AffineTransform transform) {}
}
