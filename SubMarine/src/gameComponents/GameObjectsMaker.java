package gameComponents;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;

import core.Component;
import core.GameObject;
import core.GameWorld;
import core.components.BoxCollider;
import core.components.PolygonRenderer;
import core.components.Spatial2D;
import dataTypes.Vector2;
import main.Living;
import rendering.ColorFill;

/**
 * Classe d'aide permettant une création plus rapide des gameobjects commun
 */
public class GameObjectsMaker {
	
	public static final Color tradingBoatColor = new Color(20,200,200);
	public static final Color ennemyBoatColor = new Color(255,127,0);
	
	/**
	 * Crée un missile joueur
	 * @param world
	 * @return
	 */
	public static GameObject createPlayerMissile(GameWorld world)
	{
		GameObject missile = new GameObject(world);
		
		Spatial2D spatial = new Spatial2D();
		missile.addComponent(spatial);
		
		PolygonRenderer renderer = new PolygonRenderer();
		renderer.fill = new ColorFill(new Color(37,114,46));
		renderer.shape = new Polygon(new int[] {-3,0,3,3,0,-3}, new int[] {-5,-10,-5,5,0,5}, 6);
		missile.addComponent(renderer);
		
		missile.addComponent(new PlayerMissile());
		
		return missile;
	}
	
	/**
	 * Crée un navire marchand
	 * @param world
	 * @param size taille du navire
	 * @param life vie du navire
	 * @param movingAngle angle vers lequel le navire se dirige
	 * @return
	 */
	public static GameObject createTradingBoat(GameWorld world, int size, int life, float movingAngle)
	{
		GameObject boat = new GameObject(world);
		
		Spatial2D spatial = new Spatial2D();
		boat.addComponent(spatial);
		
		PolygonRenderer renderer = new PolygonRenderer();
		renderer.fill = new ColorFill(tradingBoatColor);
		renderer.shape = new Polygon(new int[] {-size,0,size,size,-size}, new int[] {-size/2,-size,-size/2,size,size}, 5);
		boat.addComponent(renderer);
		
		boat.addComponent(new BoxCollider(-size,-size,size*2,size*2));
		
		boat.addComponent(new TradingBoat(life, movingAngle));
		
		return boat;
	}
	
	/**
	 * Crée un navire d'attaque
	 * @param world
	 * @param size Taille du navire
	 * @param waypoints Points de passage (garde) du navire
	 * @param guardedBoat Navire guardé
	 * @param life Vie du navire
	 * @return
	 */
	public static GameObject createEnnemyBoat(GameWorld world, int size, Vector2[] waypoints, Spatial2D guardedBoat, int life)
	{
		GameObject boat = new GameObject(world);
		
		Spatial2D spatial = new Spatial2D();
		boat.addComponent(spatial);
		
		PolygonRenderer renderer = new PolygonRenderer();
		renderer.fill = new ColorFill(ennemyBoatColor);
		renderer.shape = new Polygon(new int[] {-size,0,size,size,-size}, new int[] {-size/2,-size,-size/2,size,size}, 5);
		boat.addComponent(renderer);
		
		//boat.addComponent(new BoxCollider(-size,-size,size*2,size*2));
		
		EnnemyBoat boatComp = new EnnemyBoat(guardedBoat, life);
		boatComp.guardingWaypoints = waypoints;
		boat.addComponent(boatComp);
		
		return boat;
	}
	
	/**
	 * Crée un missile téléguidé
	 * @param world
	 * @param target
	 * @return
	 */
	public static GameObject createHomingMissile(GameWorld world, Spatial2D target)
	{
		GameObject missile = new GameObject(world);
		
		Spatial2D spatial = new Spatial2D();
		missile.addComponent(spatial);
		
		PolygonRenderer renderer = new PolygonRenderer();
		renderer.fill = new ColorFill(new Color(255,0,0));
		renderer.shape = new Polygon(new int[] {-3,0,3,3,0,-3}, new int[] {-5,-10,-5,5,0,5}, 6);
		missile.addComponent(renderer);
		
		missile.addComponent(new BoxCollider(-10,-10,20,20));
		
		
		HomingMissile missileComponent = new HomingMissile();
		missileComponent.target = target;
		missile.addComponent(missileComponent);
		
		return missile;
	}

}
