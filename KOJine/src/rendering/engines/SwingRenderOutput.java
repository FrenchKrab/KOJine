package rendering.engines;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;

import dataTypes.Vector2;
import rendering.ShapeRenderRequest;
import rendering.TextRenderRequest;
import rendering.TextureFill;
import rendering.ColorFill;
import rendering.FillOptions;
import rendering.FontOptions;
import rendering.RenderOutput;
import rendering.RenderRequest;

public class SwingRenderOutput implements RenderOutput {

	public SwingRenderOutput(JFrame frame)
	{
		this.frame = frame;
		frame.createBufferStrategy(2);
	}
	
	private JFrame frame;
	

	
	@Override
	public void renderRegion(ArrayList<RenderRequest> requests, AffineTransform regionTransform)
	{ 
		BufferStrategy bs = frame.getBufferStrategy();;
		Graphics2D g2 = null;
		do {
			try{
				
				Dimension dim = frame.getSize();

				g2 = (Graphics2D) bs.getDrawGraphics();
				g2.clearRect(0, 0, dim.width, dim.height);
				
				for(RenderRequest request : requests)
				{
					switch(request.getRequestType())
					{
					case SHAPE:
						ShapeRenderRequest shapeRequest = (ShapeRenderRequest)request;
						DrawShape(shapeRequest, g2);
						break;
					case TEXT:
						TextRenderRequest textRequest = (TextRenderRequest)request;
						DrawText(textRequest, g2);
					default:
						break;
					}
				}
			} finally {
				g2.dispose();
			}
			bs.show();
		} while (bs.contentsLost());
				
		
	}
	

	private void DrawShape(ShapeRenderRequest request, Graphics2D graphics) {
		if(request.getShape()==null)
			return;
		
		//Fill the shape
		graphics.setPaint(null);	//Reset the Graphics2D's filling options
		FillOptions fill = request.getFillOptions();
		if(fill != null)
		{
			if(fill instanceof ColorFill)
			{
				ColorFill colorFill = (ColorFill)fill; 
				graphics.setColor(colorFill.color);
			}
			else if(fill instanceof TextureFill)
			{
				TextureFill textureFill = (TextureFill)fill;
				TexturePaint texPaint = new TexturePaint(textureFill.texture, new Rectangle((int)textureFill.offset.x,(int)textureFill.offset.y,textureFill.texture.getWidth(), textureFill.texture.getHeight()));
				graphics.setPaint(texPaint);
			}

			graphics.fill(request.getShape());
		}

	}

	private void DrawText(TextRenderRequest request, Graphics2D graphics)
	{
		FontOptions fontOptions = request.getFontOptions();
		if(fontOptions == null)
			return;
		Font font = new Font(fontOptions.font, KOJineFontStyleToSwing(fontOptions.style), fontOptions.size);
		FontRenderContext fontRenderContext = graphics.getFontRenderContext();
		GlyphVector glyphVector = font.createGlyphVector(fontRenderContext, request.getText());
		
		AffineTransform transform = request.getTransform();
		graphics.drawGlyphVector(glyphVector, (float)transform.getTranslateX(), (float)transform.getTranslateY());
	}
	
	private int KOJineFontStyleToSwing(int style)
	{
		int swingStyle = 0;
		if((style & FontOptions.STYLE_BOLD) != 0)
			swingStyle = swingStyle | Font.BOLD;
		if((style & FontOptions.STYLE_ITALIC) != 0)
			swingStyle = swingStyle | Font.ITALIC;
		return swingStyle;
	}
	
	@Override
	public Vector2 getWindowSize() {
		Dimension dim = frame.getSize();
		return new Vector2(dim.width, dim.height);
	}




	
	
}
