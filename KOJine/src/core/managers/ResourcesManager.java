package core.managers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Manager that allows loading ressources
 * Almost unused, was planned for a texture based games, but textures
 * aren't implemented in this engine
 */
public class ResourcesManager {
	public ResourcesManager()
	{
		
	}
	
	
	protected HashMap<String, Object> resources = new HashMap<>();

	public Object getResource(String name)
	{
		return resources.get(name);
	}
	
	public void addResource(String name, Object resource)
	{
		resources.put(name, resource);
	}
	
	public BufferedImage loadImage(String name)
	{
		BufferedImage image = (BufferedImage) resources.getOrDefault(name, null);
		if(image == null)
		{
			final String dir = System.getProperty("user.dir");
	        System.out.println("current dir = " + dir);
			try {
				//See https://stackoverflow.com/questions/3861989/preferred-way-of-loading-resources-in-java
				image = ImageIO.read(new File(name));
			} catch (IOException e) {
				System.out.println("ERROR loading '" + name + "' :");
				e.printStackTrace();
			}
			this.addResource(name, image);
		}

		return image;
	}
}
