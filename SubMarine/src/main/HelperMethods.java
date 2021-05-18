package main;

import java.awt.Color;

public class HelperMethods {
	
	public static Color lerpColor(Color a, Color b, float lerp)
	{
		if(lerp>1)
			lerp=1;
		if(lerp<0)
			lerp=0;
		
		return new Color((int)lerp(a.getRed(), b.getRed(), lerp),
				(int)lerp(a.getGreen(), b.getGreen(), lerp),
				(int)lerp(a.getBlue(), b.getBlue(), lerp),
				(int)lerp(a.getAlpha(), b.getAlpha(), lerp)
				);
	}
	
	
	public static float lerp(float a, float b, float lerp)
	{
		return a+(b-a)*lerp;
	}
}
