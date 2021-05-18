package rendering;

import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import dataTypes.Vector2;

/**
 * Fill a shape with a texture (not fully working)
 */
public class TextureFill extends FillOptions{
	public TextureFill()
	{
		super();
		this.texture = null;
		this.region = null;
		this.offset = new Vector2(0,0);

	}
	public TextureFill(BufferedImage texture, Rectangle2D region)
	{
		super();
		this.texture = texture;
		this.region = region;
		this.offset = new Vector2(0,0);
	}
	public TextureFill(BufferedImage texture, Rectangle2D region, Vector2 offset)
	{
		super();
		this.texture = texture;
		this.region = region;
		this.offset = offset;
	}
	
	
	public BufferedImage texture;
	public Rectangle2D region;
	public int filter;
	public Vector2 offset;
	
	@Override
	public FillOptions clone() {
		return new TextureFill(this.texture, (Rectangle2D) region.clone(), this.offset.clone());
	}
	
	/**
	 * TODO: fully implement (rotation doesn't work)
	 */
	@Override
	public void applyTransform(AffineTransform transform) {
		AffineTransform newTransform = new AffineTransform();
		double[] m = new double[6]; 
		transform.getMatrix(m);
		
		double up = new Vector2(m[2], m[3]).length();
		double right = new Vector2(m[0], m[1]).length();
		newTransform.setToScale(right, up);
		offset.x += m[4];
		offset.y += m[5];
		//System.out.println("["+m[0]+","+m[1]+","+m[2]+","+m[3]+","+m[4]+","+m[5]+")");
		
		BufferedImage transformedTexture = new BufferedImage((int)(this.texture.getWidth()*right), (int)(this.texture.getHeight()*up), this.texture.getType());
		AffineTransformOp op = new AffineTransformOp(newTransform, AffineTransformOp.TYPE_BILINEAR);
		op.filter(this.texture, transformedTexture);
		this.texture = transformedTexture;
	}
	
	
	@Override
	public FillOptions createTransformed(AffineTransform transform) {
		FillOptions newFill = this.clone();
		newFill.applyTransform(transform);
		return newFill;
	}

}
