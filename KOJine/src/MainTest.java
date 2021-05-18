import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import core.Component;
import core.GameObject;
import core.GameWorld;
import core.components.BoxCollider;
import core.components.Camera2D;
import core.components.PolygonRenderer;
import core.components.Spatial2D;
import dataTypes.Vector2;
import input.feeders.SwingInputFeeder;
import rendering.ColorFill;
import rendering.TextureFill;
import rendering.engines.SwingRenderOutput;

/**
 *	This is a test class, used for debug purposes
 *	no longer used.
 */
public class MainTest {

	/*
	public static void main(String[] args) {
		
		TestGUI gui = new TestGUI();
		SwingInputFeeder inputFeeder = new SwingInputFeeder();
		gui.addKeyListener(inputFeeder);
		
		GameWorld world = new GameWorld(new SwingRenderOutput(gui), inputFeeder);
		
		//GO1
		GameObject go1 = new GameObject(world);
		Spatial2D spatial1 = new Spatial2D();
		go1.addComponent(spatial1);
		
		PolygonRenderer renderer1 = new PolygonRenderer();
		renderer1.fill = new ColorFill(new Color(255,0,0));
		renderer1.shape = new Polygon(new int[] {-10,10,10,-10}, new int[] {-10,-10,10,10}, 4);
		go1.addComponent(renderer1);
		
		go1.addComponent(new BoxCollider(-10,-10,20,20));
		
		//go1.addComponent(new TestComponent());
		
		//GO1
		GameObject go2 = new GameObject(world);
		Spatial2D spatial2 = new Spatial2D();
		spatial2.setPosition(new Vector2(50,10));
		go2.addComponent(spatial2);
		
		PolygonRenderer renderer2 = new PolygonRenderer();
		renderer2.fill = new ColorFill(new Color(0,0,255));
		renderer2.shape = new Polygon(new int[] {-10,10,10,-10}, new int[] {-10,-10,10,10}, 4);
		go2.addComponent(renderer2);
		
		go2.addComponent(new BoxCollider(-10,-10,20,20));
		
		//CAMERA
		GameObject camera = new GameObject(world);
		Spatial2D spatialCam = new Spatial2D();
		camera.addComponent(spatialCam);
		camera.addComponent(new Camera2D());
		camera.addComponent(new TestComponent());
		
		world.mainLoop();
	}
	*/
	
	
	
	public static void main(String[] args) {
		
		TestGUI gui = new TestGUI();
		SwingInputFeeder inputFeeder = new SwingInputFeeder();
		gui.addKeyListener(inputFeeder);
		
		GameWorld world = new GameWorld(new SwingRenderOutput(gui), inputFeeder);
		
		
		GameObject go1 = new GameObject(world);
		Spatial2D spatial1 = new Spatial2D();

		spatial1.setRotation(0);
		System.out.println("UP /"+ spatial1.up().toString());
		
		go1.addComponent(spatial1);
		PolygonRenderer polyRenderer1 = new PolygonRenderer();
		BufferedImage perry = world.getResources().loadImage("resources/perry.jpg");
		polyRenderer1.fill = new TextureFill(perry, new Rectangle(0,0,perry.getHeight(), perry.getWidth()));
		//polyRenderer1.fill = new ColorFill(new Color(127,127,127));
		polyRenderer1.shape = new Polygon(new int[] {-10,10,10,-10}, new int[] {-10,-10,10,10}, 4);
		go1.addComponent(polyRenderer1);
		go1.addComponent(new BoxCollider(-10,-10,20,20));
		go1.addComponent(new TestComponent());

		
		
		for(int i = 0; i < 10; i++)
		{
			GameObject go = new GameObject(world);
			
			Spatial2D spatial = new Spatial2D();
			spatial.setRotation(0);
			spatial.setPosition(20*i, 0);;
			go.addComponent(spatial);
			System.out.println("UP2 /"+ spatial.up().toString());
			
			PolygonRenderer polyRenderer = new PolygonRenderer();
			polyRenderer.fill = new ColorFill(new Color(i*20,0,255-i*20));
			polyRenderer.shape = new Polygon(new int[] {0,10,10,0}, new int[] {0,0,10,10}, 4);
			go.addComponent(polyRenderer);

			BoxCollider collider = new BoxCollider();
			collider.box = new Rectangle(0,0,10,10);
			collider.isTrigger = true;
			go.addComponent(collider);
		}
		
		GameObject camera = new GameObject(world);
		Camera2D camComponent = new Camera2D();
		camera.addComponent(camComponent);
		//camera.addComponent(new TestComponent());
		//camera.addComponent(new BoxCollider(-5,-5,10,10));
		PolygonRenderer polyRenderer = new PolygonRenderer();
		polyRenderer.fill = new ColorFill(new Color(20,0,255));
		polyRenderer.shape = new Polygon(new int[] {0,100,100,0}, new int[] {0,0,100,100}, 4);
		camera.addComponent(polyRenderer);

		
		world.mainLoop();
	}
	
	
	public static void OLDmain(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Yes");
		
		
		TestGUI gui = new TestGUI();
		SwingInputFeeder inputFeeder = new SwingInputFeeder();
		gui.addKeyListener(inputFeeder);
		
		GameWorld world = new GameWorld(new SwingRenderOutput(gui), inputFeeder);
		
		GameObject go = new GameObject();
		go.setWorld(world);
		
		go.addComponent(new Component());
		go.addComponent(new Component());
		go.addComponent(new TestComponent());
		Spatial2D spatial = new Spatial2D();
		go.addComponent(spatial);
		
		spatial.setPosition(5,10);
		spatial.rotate(1);
		System.out.println(spatial.getRotationAngle());

		float x = 0;
		float y = 0;
		while(true)
		{
			x+=0.0025;
			x=x%300;
			y+=0.000125;
			y=y%300;
			if(gui.bufferStrategy == null)
			{
				gui.createBufferStrategy(2);
				gui.bufferStrategy = gui.getBufferStrategy();
			}
			
			for(int i = 0; i < 2; i++)
			{
				Graphics2D bufferedGraphics = (Graphics2D) gui.bufferStrategy.getDrawGraphics();
				bufferedGraphics.clearRect(0, 0, 5000, 5000);
				bufferedGraphics.setColor(new Color(255,0,255));
				BufferedImage image = null;
				try {
					image = ImageIO.read(bufferedGraphics.getClass().getResource("/resources/perry.jpg"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AffineTransform texture = new AffineTransform();
				texture.rotate(Math.PI*0.25, image.getWidth()/2, image.getHeight()/2);
				
				AffineTransformOp op = new AffineTransformOp(texture, AffineTransformOp.TYPE_BILINEAR);
				image = op.filter(image, null);
				
				Stroke dashed = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0, new float[]{20}, x*10);
				bufferedGraphics.setStroke(dashed);
				bufferedGraphics.setPaint(new TexturePaint(image, new Rectangle(0,0,20,20)));
				bufferedGraphics.fillRect(20, 20, 200, 200);
				

				
				gui.bufferStrategy.show();
				bufferedGraphics.dispose();
			}
		}
		
	}

}
