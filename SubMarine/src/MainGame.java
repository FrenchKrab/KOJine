import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import core.GameObject;
import core.GameWorld;
import core.components.BoxCollider;
import core.components.Camera2D;
import core.components.PolygonRenderer;
import core.components.Spatial2D;
import core.components.TextRenderer;
import dataTypes.Vector2;
import gameComponents.AttachComponent;
import gameComponents.GameObjectsMaker;
import gameComponents.SubmarineCamera;
import gameComponents.SubmarineController;
import gameComponents.WinManager;
import input.feeders.SwingInputFeeder;
import main.GameConstants;
import rendering.engines.SwingRenderOutput;
import rendering.ColorFill;
import rendering.FontOptions;
import rendering.TextureFill;

public class MainGame {

	public static void main(String[] args) {
		//Initialiser la GUI avec Swing
		MainGUI gui = new MainGUI();
		SwingInputFeeder inputFeeder = new SwingInputFeeder();
		gui.addKeyListener(inputFeeder);
		
		int worldValue;
		
		//Créer un monde 1 et en lancer la boucle principale
		GameWorld world = new GameWorld(new SwingRenderOutput(gui), inputFeeder);
		populateWorld1(world);
		worldValue = world.mainLoop();
		
		//Si la partie est gagnée, passer au monde suivant, sinon détruire la GUI et s'arrêter là
		if(worldValue == GameConstants.WORLDRETURN_LOSE)
		{
			gui.dispose();
			return;
		}
		
		//Créer un monde 2 et en lancer la boucle principale
		GameWorld world2 = new GameWorld(new SwingRenderOutput(gui), inputFeeder);
		populateWorld2(world2);
		
		worldValue = world2.mainLoop();
		if(worldValue == GameConstants.WORLDRETURN_LOSE)
		{
			gui.dispose();
			return;
		}
		
		//Créer un monde 3 et en lancer la boucle principale
		GameWorld world3 = new GameWorld(new SwingRenderOutput(gui), inputFeeder);
		populateWorld3(world3);
		
		worldValue = world3.mainLoop();
		if(worldValue == GameConstants.WORLDRETURN_LOSE)
		{
			gui.dispose();
			return;
		}

		gui.dispose();
		return;
	}

	public static void populateWorld1(GameWorld world)
	{
		//Décommenter ce bloc pour voir à quoi ressemble les textures en jeu
		/*GameObject aa = new GameObject(world);
		aa.addComponent(new Spatial2D(-2000,-2000,0));
		PolygonRenderer polyRenderer1 = new PolygonRenderer();
		BufferedImage perry = world.getResources().loadImage("resources/water1.png");
		polyRenderer1.fill = new TextureFill(perry, new Rectangle(0,0,perry.getHeight(), perry.getWidth()));
		//polyRenderer1.fill = new ColorFill(new Color(127,127,127));
		polyRenderer1.shape = new Polygon(new int[] {0,4000,4000,0}, new int[] {0,0,4000,4000}, 4);
		aa.addComponent(polyRenderer1);
		*/
		
		GameObject uiTest = new GameObject(world);
		uiTest.name = GameConstants.WINMANAGER_NAME;
		uiTest.addComponent(new Spatial2D(5,28*2,0));
		WinManager winManagerComp = new WinManager(30f);
		winManagerComp.incrementBoatCount();
		uiTest.addComponent(winManagerComp);
		
		//Submarine
		GameObject go1 = new GameObject(world);
		go1.addComponent(new Spatial2D(-200,200,0));
		
		PolygonRenderer renderer1 = new PolygonRenderer();
		renderer1.fill = new ColorFill(new Color(255,0,0));
		renderer1.shape = new Polygon(new int[] {-10,0,10,10,-10}, new int[] {-10,-20,-10,10,10}, 5);
		renderer1.tag = "body";
		go1.addComponent(renderer1);
	
		go1.addComponent(new SubmarineController());
		
		//CAMERA
		GameObject camera = new GameObject(world);
		camera.addComponent(new Camera2D());
		camera.addComponent(new AttachComponent(go1, true, new Vector2(0,-30), false, 0));
		camera.addComponent(new SubmarineCamera());

		GameObject tradingBoat = GameObjectsMaker.createTradingBoat(world, 10, 50, (float) (-Math.PI/2));
		Spatial2D tbSpatial = tradingBoat.getComponent(Spatial2D.class);
		GameObject ennemyBoat = GameObjectsMaker.createEnnemyBoat(world, 15,
				new Vector2[] {new Vector2(-50,-50), new Vector2(50,-50), new Vector2(50,50), new Vector2(-50,50)},
				tbSpatial, 20);
		
	}
	
	public static void populateWorld2(GameWorld world)
	{
		GameObject uiTest = new GameObject(world);
		uiTest.name = GameConstants.WINMANAGER_NAME;
		uiTest.addComponent(new Spatial2D(5,28*2,0));
		WinManager winManagerComp = new WinManager(75f);
		uiTest.addComponent(winManagerComp);
		
		//Submarine
		GameObject go1 = new GameObject(world);
		go1.addComponent(new Spatial2D(-200,200,0));
		
		PolygonRenderer renderer1 = new PolygonRenderer();
		renderer1.fill = new ColorFill(new Color(255,0,0));
		renderer1.shape = new Polygon(new int[] {-10,0,10,10,-10}, new int[] {-10,-20,-10,10,10}, 5);
		renderer1.tag = "body";
		go1.addComponent(renderer1);
	
		go1.addComponent(new SubmarineController());
		
		//CAMERA
		GameObject camera = new GameObject(world);
		camera.addComponent(new Camera2D());
		camera.addComponent(new AttachComponent(go1, true, new Vector2(0,-30), false, 0));
		camera.addComponent(new SubmarineCamera());
		
		for (int i = 0; i<5; i++)
		{
			winManagerComp.incrementBoatCount();
			
			GameObject tradingBoat = GameObjectsMaker.createTradingBoat(world, 10, 30, (float) (-Math.PI/2));
			Spatial2D tbSpatial = tradingBoat.getComponent(Spatial2D.class);
			tbSpatial.setPosition(i*100,0);
			
			GameObject ennemyBoat = GameObjectsMaker.createEnnemyBoat(world, 15,
					new Vector2[] {new Vector2(-50,-50), new Vector2(50,-50)},
					tbSpatial, 20);
			
			GameObject ennemyBoat2 = GameObjectsMaker.createEnnemyBoat(world, 15,
					new Vector2[] {new Vector2(-50,50), new Vector2(50,50)},
					tbSpatial, 20);
		}
	}
	
	public static void populateWorld3(GameWorld world)
	{
		GameObject uiTest = new GameObject(world);
		uiTest.name = GameConstants.WINMANAGER_NAME;
		uiTest.addComponent(new Spatial2D(5,28*2,0));
		WinManager winManagerComp = new WinManager(75f);
		uiTest.addComponent(winManagerComp);
		
		//Submarine
		GameObject go1 = new GameObject(world);
		go1.addComponent(new Spatial2D(0,300,0));
		
		PolygonRenderer renderer1 = new PolygonRenderer();
		renderer1.fill = new ColorFill(new Color(255,0,0));
		renderer1.shape = new Polygon(new int[] {-10,0,10,10,-10}, new int[] {-10,-20,-10,10,10}, 5);
		renderer1.tag = "body";
		go1.addComponent(renderer1);
	
		go1.addComponent(new SubmarineController());
		
		//CAMERA
		GameObject camera = new GameObject(world);
		camera.addComponent(new Camera2D());
		camera.addComponent(new AttachComponent(go1, true, new Vector2(0,-30), false, 0));
		camera.addComponent(new SubmarineCamera());
		
		for (int i = 0; i<5; i++)
		{
			winManagerComp.incrementBoatCount();
			winManagerComp.incrementBoatCount();
			winManagerComp.incrementBoatCount();
			
			GameObject tradingBoat = GameObjectsMaker.createTradingBoat(world, 10, 30, (float) (-Math.PI/2));
			Spatial2D tbSpatial = tradingBoat.getComponent(Spatial2D.class);
			tbSpatial.setPosition(i*100,0);
			
			GameObject tradingBoat2 = GameObjectsMaker.createTradingBoat(world, 10, 30, (float) (-Math.PI/2));
			Spatial2D tbSpatial2 = tradingBoat2.getComponent(Spatial2D.class);
			tbSpatial2.setPosition(i*100,-100);
			
			GameObject tradingBoat3 = GameObjectsMaker.createTradingBoat(world, 10, 30, (float) (-Math.PI/2));
			Spatial2D tbSpatial3 = tradingBoat3.getComponent(Spatial2D.class);
			tbSpatial3.setPosition(i*100,-200);
			
			GameObject ennemyBoat = GameObjectsMaker.createEnnemyBoat(world, 15,
					new Vector2[] {new Vector2(-50,-50), new Vector2(50,-50)},
					tbSpatial3, 20);
			
			GameObject ennemyBoat2 = GameObjectsMaker.createEnnemyBoat(world, 15,
					new Vector2[] {new Vector2(-50,50), new Vector2(50,50)},
					tbSpatial, 20);
			
			GameObject ennemyBoat3 = GameObjectsMaker.createEnnemyBoat(world, 15,
					new Vector2[] {new Vector2(-50,-100), new Vector2(50,-100)},
					tbSpatial3, 20);
			
			GameObject ennemyBoat4 = GameObjectsMaker.createEnnemyBoat(world, 15,
					new Vector2[] {new Vector2(-50,100), new Vector2(50,100)},
					tbSpatial, 20);
		}
	}
}
