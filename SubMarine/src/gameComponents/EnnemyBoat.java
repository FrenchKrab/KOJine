package gameComponents;

import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import core.Component;
import core.GameObject;
import core.components.BoxCollider;
import core.components.ColliderComponent;
import core.components.EllipseCollider;
import core.components.PolygonRenderer;
import core.components.Spatial2D;
import dataTypes.Vector2;
import main.DamageReceivable;
import main.DetectionZoneCallback;
import main.GameConstants;
import main.Living;
import rendering.ColorFill;

/**
 * G�re le comportement d'un bateau d'attaque ennemi
 */
public class EnnemyBoat extends Component implements DetectionZoneCallback, DamageReceivable, Living {
	
	public EnnemyBoat()
	{
	}
	
	public EnnemyBoat(Spatial2D guardedBoat, int life)
	{
		this.guardedBoat = guardedBoat;
		this.maxLife = life;
		this.life = life;
	}

	
	private enum BehaviourState {FOLLOW_TRADING, ATTACK_PLAYER };
	
	/**
	 * Rayon de detection des bateau par d�faut
	 */
	private static final float DEFAULT_DETECTION_RADIUS = 50;
	
	/**
	 * Nombre de missiles envoy�s par seconde par d�faut
	 */
	private static final float DEFAULT_MISSILE_PER_SECOND = 0.5f;
	
	private static final int DEFAULT_LIFE = 30;
	
	/**
	 * Rayon dans lequel on consid�rera que le bateau a valid� un point de passage
	 */
	private static final float WAYPOINT_CLOSE_ENOUGH = 10; 
	private static final float SPEED = 60;
	
	private static final float ANIMATION_HIT_DURATION = 0.2f;
	
	private static final Color COLOR_DAMAGED = new Color(255,78,195);
	private static final Color COLOR_LIFEBAR_FULL = new Color(0,255,0);
	private static final Color COLOR_LIFEBAR_EMPTY = new Color(255,0,0);
	private static final Color COLOR_AI_NEUTRAL = new Color(255,153,103);
	private static final Color COLOR_AI_ATTACK = new Color(255,84,0);
	
	/**
	 * Etat actuel de l'IA du bateau
	 */
	private BehaviourState aiState = BehaviourState.FOLLOW_TRADING;
	
	/**
	 * Vie maximum que le bateau peut poss�der
	 */
	private int maxLife = DEFAULT_LIFE;
	
	/**
	 * Vie actuelle du bateau
	 */
	private int life = DEFAULT_LIFE;
	
	/**
	 * Spatial2D du sous-marin joueur
	 */
	private Spatial2D player = null;
	
	/**
	 * Vector2 vers lequel ce bateau va se diriger
	 */
	private Vector2 target = null;
	
	/**
	 * Spatial2D que ce bateau gardera (probablement un bateau de commerce)
	 */
	public Spatial2D guardedBoat = null;
	
	/**
	 * Liste des points de passages relatif au bateau � d�fendre que ce bateau parcourera
	 */
	public Vector2[] guardingWaypoints = null;
	
	/**
	 * Index du point de passage vers lequel on se dirige
	 */
	public int targetWaypoint = 0;
	
	/**
	 * Temps de charge, qui devra atteindre "missilePerSeconds" avant que le bateau tire un missile
	 */
	private float fireCooldown = 0;
	
	/**
	 * Nombre de missiles tir�s par seconde par ce bateau
	 */
	private float missilePerSeconds = DEFAULT_MISSILE_PER_SECOND;

	/**
	 * Temps �coul� depuis la derni�re prise de d�gats
	 */
	private float timeSinceLastDamage = ANIMATION_HIT_DURATION;
	
	private DetectionZone fieldOfView;
	private PolygonRenderer boatRenderer;
	private Spatial2D spatial;
	
	@Override
	public void start() {
		super.start();

		
		this.boatRenderer = getOwner().getComponent(PolygonRenderer.class);
		this.spatial = getOwner().getOrCreateComponent(Spatial2D.class);
		this.fireCooldown = 1/missilePerSeconds;
		
		this.fieldOfView = new DetectionZone(GameConstants.TEAM_ENNEMY, false, true, DEFAULT_DETECTION_RADIUS);
		this.fieldOfView.callbacks.add(this);
		getOwner().addComponent(fieldOfView);
		
		BoxCollider hurtboxCollider = new BoxCollider(-15, -15, 30, 30);
		getOwner().addComponent(hurtboxCollider);
		
		Hitbox hurtbox = new Hitbox(GameConstants.TEAM_ENNEMY, true, true, 10);
		hurtbox.colliders.add(hurtboxCollider);
		hurtbox.damageReceivedCallback.add(this);
		getOwner().addComponent(hurtbox);
		
		GameObject lifebarGo = new GameObject(this.getOwner().getWorld());
		lifebarGo.addComponent(new Lifebar(this, COLOR_LIFEBAR_EMPTY, COLOR_LIFEBAR_FULL, new Vector2(maxLife,5), new Vector2(-maxLife/2,-50)));
	}
	
	@Override
	public void update(double delta) {
		// TODO Auto-generated method stub
		super.update(delta);
		
		if(aiState==BehaviourState.ATTACK_PLAYER)
		{
			if(player != null)
			{
				target = player.getPosition();
				//Fire a missile
				if(this.fireCooldown > 1f/missilePerSeconds)
				{
					GameObject newMissile = GameObjectsMaker.createHomingMissile(this.getOwner().getWorld(), player);
					newMissile.getComponent(Spatial2D.class).setPosition(spatial.getPosition());
					this.fireCooldown -= 1f/missilePerSeconds;
				}
			}
		}
		else if(aiState==BehaviourState.FOLLOW_TRADING)
		{
			//Si on a bien un bateau � d�fendre
			if(guardedBoat != null)
			{
				//Si on a aucun point de passage d�finit
				if(guardingWaypoints == null || guardingWaypoints.length == 0)
				{
					target = guardedBoat.getPosition();	//Par d�faut, le bateau ennemi se dirige vers le bateau a d�fendre
				}
				else	//Si on a des points de passage � parcourir
				{
					//Pour �tre sur de ne pas faire un outofbound index en cas de changement de l'arraylist
					targetWaypoint = targetWaypoint % guardingWaypoints.length;
					target = guardingWaypoints[targetWaypoint].add(guardedBoat.getPosition());
					//Plus �conomique que de r�cup�rer la vraie length/distance
					if(target == null)
						System.out.println("TARGET NULL");
					if(spatial == null)
						System.out.println("SPATIAL NULL");
					
					if(target.substract(spatial.getPosition()).lengthSquared() < WAYPOINT_CLOSE_ENOUGH*WAYPOINT_CLOSE_ENOUGH)
					{
						targetWaypoint++;
					}
				}
			}
		}

		Vector2 movement = target.substract(spatial.getPosition());
		double desiredAngle = movement.angle();
		spatial.setRotation(desiredAngle+Math.PI/2);
		spatial.translateGlobal(movement.getNormalized().multiply(delta*SPEED));
		
		
		updateColor();
		
		this.fireCooldown += delta;
		this.timeSinceLastDamage += delta;
	}
	
	/**
	 * Met � jour la couleur du bateau en fonction de son �tat (normal, d�gats, attaque, etc)
	 */
	private void updateColor()
	{
		Color newColor;
		if(timeSinceLastDamage < ANIMATION_HIT_DURATION)
			newColor = COLOR_DAMAGED;
		else if(aiState == BehaviourState.ATTACK_PLAYER)
			newColor = COLOR_AI_ATTACK;
		else
			newColor = COLOR_AI_NEUTRAL;
		((ColorFill)boatRenderer.fill).color = newColor;
	}
	
	/**
	 * Change le comportement de l'AI du bateau
	 */
	private void setAiState(BehaviourState aiState)
	{
		this.aiState = aiState;
		if(aiState == BehaviourState.ATTACK_PLAYER)
		{
			boatRenderer.fill = new ColorFill(COLOR_AI_ATTACK);
		}
		else if(aiState == BehaviourState.FOLLOW_TRADING)
		{
			boatRenderer.fill = new ColorFill(COLOR_AI_NEUTRAL);
		}
	}
	
	/**
	 * Callback from DetectionZone
	 */

	@Override
	public void ZoneEntered(DetectionZone ownZone, DetectionZone otherZone) 
	{
		if(ownZone == this.fieldOfView)
		{
			if(otherZone.team==GameConstants.TEAM_PLAYER)
			{
				this.player = otherZone.getOwner().getComponent(Spatial2D.class);
				setAiState(BehaviourState.ATTACK_PLAYER);
			}
		}

	}
	
	/**
	 * Callback from DetectionZone
	 */

	@Override
	public void ZoneExited(DetectionZone ownZone, DetectionZone otherZone) 
	{
		if(ownZone == this.fieldOfView)
		{
			if(otherZone.team==GameConstants.TEAM_PLAYER)
			{
				if(this.player == otherZone.getOwner().getComponent(Spatial2D.class))
				{
					setAiState(BehaviourState.FOLLOW_TRADING);
				}
			}
		}
	}

	
	@Override
	public void receivedDamages(int damages) {
		life -= damages;
		timeSinceLastDamage = 0;
		
		if(life <= 0)
		{
			getOwner().destroy();
		}
	}

	@Override
	public int getLife() {
		return life;
	}

	@Override
	public void setLife(int newLife) {
		this.life = newLife;
	}

	@Override
	public int getMaxLife() {
		return maxLife;
	}
	

}
