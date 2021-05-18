package rendering;

/**
 * Holds data for drawing a font
 */
public class FontOptions 
{
	public FontOptions()
	{}
	
	public FontOptions(String font, int style, int size)
	{
		this.font = font;
		this.style = style;
		this.size = size;
	}
	
	/**
	 * Default font used
	 */
	public static final String FONT_DEFAULT = "Verdana";
	
	/**
	 * Constant representing bold style
	 */
	public static final int STYLE_BOLD = 0b1;
	
	/**
	 * Constant representing italic style
	 */
	public static final int STYLE_ITALIC = 0b10;

	
	/**
	 * The font to use's name
	 */
	public String font = FONT_DEFAULT;
	
	/**
	 * The style of the font (bold, italic, ...)
	 */
	public int style = 0;
	
	/**
	 * Size of the font in pixels
	 */
	public int size = 16;
	
	public FontOptions clone()
	{
		return new FontOptions(font, style, size);
	}
}
