package gameComponents;

import java.awt.Color;

import core.Component;
import core.GameObject;
import core.components.BoxCollider;
import core.components.Spatial2D;
import dataTypes.Vector2;
import main.DamageReceivable;
import main.GameConstants;
import main.Living;

/**
 * Navire marchand
 */
public class TradingBoat extends Component implements DamageReceivable, Living {

	public TradingBoat()
	{
		this.movingDirection = new Vector2(1, 0);
	}
	
	public TradingBoat(int life, float movingAngle)
	{
		this.maxLife = life;
		this.life = life;
		movingAngle += Math.PI/2;
		this.movingDirection = new Vector2(Math.cos(movingAngle), Math.sin(movingAngle));
		this.movingDirection.normalize();
	}
	
	private static final float ANIMATION_HIT_DURATION = 0.2f;
	private static final Color COLOR_DAMAGED = new Color(255,78,195);
	private static final Color COLOR_LIFEBAR_FULL = new Color(0,255,0);
	private static final Color COLOR_LIFEBAR_EMPTY = new Color(255,0,0);
	
	private static final float SPEED = 30;
	private static final int DEFAULT_LIFE = 50;
	
	private Vector2 movingDirection;
	private int maxLife = DEFAULT_LIFE;
	private int life = DEFAULT_LIFE;
	
	private float timeSinceLastDamage = ANIMATION_HIT_DURATION;
	
	private Spatial2D spatial;
	
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		
		spatial = getOwner().getOrCreateComponent(Spatial2D.class);
		
		BoxCollider hurtboxCollider = new BoxCollider(-15, -15, 30, 30);
		getOwner().addComponent(hurtboxCollider);
		
		
		Hitbox hurtbox = new Hitbox(GameConstants.TEAM_ENNEMY, false, true, 0);
		hurtbox.colliders.add(hurtboxCollider);
		hurtbox.damageReceivedCallback.add(this);
		getOwner().addComponent(hurtbox);
		
		GameObject lifebarGo = new GameObject(this.getOwner().getWorld());
		lifebarGo.addComponent(new Lifebar(this, COLOR_LIFEBAR_EMPTY, COLOR_LIFEBAR_FULL, new Vector2(50,5), new Vector2(-20,-50)));
	}

	
	@Override
	public void update(double delta) {
		// TODO Auto-generated method stub
		super.update(delta);
		
		spatial.translateGlobal(movingDirection.multiply(delta*SPEED));
		double desiredAngle = movingDirection.angle();
		spatial.setRotation(desiredAngle+Math.PI/2);
	}
	
	@Override
	public int getLife() {
		return life;
	}

	@Override
	public int getMaxLife() {
		return maxLife;
	}

	@Override
	public void setLife(int newLife) {
		life = newLife;
	}

	@Override
	public void receivedDamages(int damages) {
		life -= damages;
		if(life<=0 && life + damages > 0) //Si le bateau vient de mourir
		{
			GameObject win = getOwner().getWorld().findGameObjectWithName(GameConstants.WINMANAGER_NAME);
			if(win != null)
				win.getComponent(WinManager.class).decrementBoatCount();
			getOwner().destroy();
		}
	}
}
