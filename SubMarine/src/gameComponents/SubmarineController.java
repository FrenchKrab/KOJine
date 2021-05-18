package gameComponents;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;

import core.Component;
import core.GameObject;
import core.components.BoxCollider;
import core.components.ColliderComponent;
import core.components.PolygonRenderer;
import core.components.Spatial2D;
import core.components.TextRenderer;
import core.managers.InputManager;
import dataTypes.Vector2;
import input.InputKeyType;
import main.DamageReceivable;
import main.DetectionZoneCallback;
import main.GameConstants;
import main.HelperMethods;
import main.Living;
import rendering.ColorFill;
import rendering.FontOptions;
import rendering.TextRenderRequest;

/**
 * Comportement du sous marin joueur
 */
public class SubmarineController extends Component implements DamageReceivable, Living
{	
	private final float acceleration = 2500;
	private final float missileSpeed = 350;
	private final float MAX_SPEED = 200;
	private final float DETECTABLE_MAX_RADIUS = 200;
	private final float DETECTABLE_MIN_RADIUS = 1;
	private final float MAX_DEPTH = 300;
	private final float SUBMERSION_SPEED = MAX_DEPTH/1f;
	private final float ANIMATION_HIT_DURATION = 0.2f;
	private final float I_FRAMES_ONHIT = 0.2f;
	private final int LIFE_MAX = 100;
	
	private final Color COLOR_AT_OUTSIDE = new Color(150,255,0);
	private final Color COLOR_AT_MINDEPTH = new Color(136,194,54);
	private final Color COLOR_AT_MAXDEPTH = new Color(97,126,55);
	private final Color COLOR_DAMAGED = new Color(255,48,0);
	private final Color COLOR_LIFEBAR_FULL = new Color(0,255,0);
	private final Color COLOR_LIFEBAR_EMPTY = new Color(255,0,0);
	
	private final Vector2 UI_LIFEBAR_POS = new Vector2(40,-20);
	private final Vector2 UI_LIFEBAR_SIZE_FULL = new Vector2(100,5);
	
	
	private Vector2 velocity = Vector2.zero();
	private Vector2 movementInput = Vector2.zero();
	
	private int life = LIFE_MAX;
	private float depth = 0;

	private Color depthColor = COLOR_AT_OUTSIDE;
	private Color currentColor = COLOR_AT_OUTSIDE;
	private float timeSinceLastDamage = ANIMATION_HIT_DURATION;
	
	private ColliderComponent hurtboxCollider;
	private Hitbox hurtbox;
	private PolygonRenderer bodyRenderer;
	private PolygonRenderer lifeBar;
	private Spatial2D spatial;
	
	private DetectionZone detectableZone;
	

	@Override
	public void start() {
		super.start();
		spatial = getOwner().getOrCreateComponent(Spatial2D.class);
		
		//Pas besoin de callback, on veut juste pouvoir être detecté par les navires ennemis
		detectableZone = new DetectionZone(GameConstants.TEAM_PLAYER, true, false, DETECTABLE_MAX_RADIUS);
		getOwner().addComponent(detectableZone);
		
		hurtboxCollider = new BoxCollider(-1,-1,2,2);
		getOwner().addComponent(hurtboxCollider);
		
		hurtbox = new Hitbox(GameConstants.TEAM_PLAYER, false, true, 0);
		hurtbox.damageReceivedCallback.add(this);
		hurtbox.colliders.add(hurtboxCollider);
		getOwner().addComponent(hurtbox);
		
		bodyRenderer = getOwner().getComponentWithTag(PolygonRenderer.class, "body");
		updateDepthColor();
		
		GameObject lifebarGo = new GameObject(this.getOwner().getWorld());
		lifebarGo.addComponent(new Lifebar(this, COLOR_LIFEBAR_EMPTY, COLOR_LIFEBAR_FULL, new Vector2(100,5), new Vector2(-50,-50)));
		
		
	}
	
	
	
	@Override
	public void update(double delta) {	
		InputManager input = this.getOwner().getWorld().getInputManager();
		//Get movement input
		movementInput = Vector2.zero();
		if(input.isKeyPressed(InputKeyType.KEY_UP))
		{
			movementInput.y-=1;
		}
		if(input.isKeyPressed(InputKeyType.KEY_DOWN))
		{
			movementInput.y+=1;
		}
		if(input.isKeyPressed(InputKeyType.KEY_RIGHT))
		{
			movementInput.x+=1;
		}
		if(input.isKeyPressed(InputKeyType.KEY_LEFT))
		{
			movementInput.x-=1;
		}
		
		if(input.isKeyPressed(InputKeyType.KEY_W))
		{
			depth = (float)Math.min(depth + SUBMERSION_SPEED * delta, MAX_DEPTH);
			updateDepthColor();
			detectableZone.setZoneRadius(HelperMethods.lerp(DETECTABLE_MIN_RADIUS, DETECTABLE_MAX_RADIUS, 1-depth/MAX_DEPTH));
		}
		if(input.isKeyPressed(InputKeyType.KEY_X))
		{
			depth = (float)Math.max(depth - SUBMERSION_SPEED * delta, 0);
			updateDepthColor();
			detectableZone.setZoneRadius(HelperMethods.lerp(DETECTABLE_MIN_RADIUS, DETECTABLE_MAX_RADIUS, 1-depth/MAX_DEPTH));
		}
		
		if((input.isKeyJustPressed(InputKeyType.KEY_C) || input.isKeyJustPressed(InputKeyType.KEY_V)) && depth == 0)
		{
			GameObject newMissile = GameObjectsMaker.createPlayerMissile(this.getOwner().getWorld());
			newMissile.getComponent(Spatial2D.class).setPosition(spatial.getPosition());
			PlayerMissile missileComponent = newMissile.getComponent(PlayerMissile.class);
			missileComponent.velocity = this.velocity.getNormalized().multiply(missileSpeed);
		}
		
		Vector2 movement = movementInput.multiply(acceleration * delta);
		
		velocity = velocity.add(movement);
		velocity.clampLength(MAX_SPEED);

		spatial.translateGlobal(velocity.multiply(delta));
		spatial.setRotation(Math.atan2(velocity.x, -velocity.y));
		
		updateColor();

		timeSinceLastDamage+=delta;
		
		//60 objets : 53 FPS
		//System.out.println(getOwner().getWorld().gameObjects.size() + " || " + (int)(1/delta) + " FPS");
	}
	
	
	@Override
	public void fixedUpdate(double delta) {
		super.fixedUpdate(delta);
	}

	@Override
	public void onTriggerEnter(ColliderComponent ownCollider, ColliderComponent otherCollider) {
		super.onTriggerEnter(ownCollider, otherCollider);
		//System.out.println("OK");
	}

	private void updateColor()
	{
		Color color = depthColor;
		if(timeSinceLastDamage < ANIMATION_HIT_DURATION)
		{
			color = COLOR_DAMAGED;
		}
		((ColorFill)bodyRenderer.fill).color = color;
	}
	
	private void updateDepthColor()
	{
		if(depth==0)
			depthColor = COLOR_AT_OUTSIDE;
		else
			depthColor = HelperMethods.lerpColor(COLOR_AT_MINDEPTH, COLOR_AT_MAXDEPTH, depth/MAX_DEPTH);

	}

	@Override
	public void receivedDamages(int damages) {
		if(timeSinceLastDamage > I_FRAMES_ONHIT)
		{
			life -= damages;
			timeSinceLastDamage = 0;
		}
		
		if(life <= 0)
		{
			GameObject win = getOwner().getWorld().findGameObjectWithName(GameConstants.WINMANAGER_NAME);
			if(win != null)
				win.getComponent(WinManager.class).setLost();
			getOwner().destroy();
		}
	}
	



	@Override
	public int getLife() {
		return this.life;
	}



	@Override
	public int getMaxLife() {
		return LIFE_MAX;
	}



	@Override
	public void setLife(int newLife) {
		this.life = newLife;
	}
}
