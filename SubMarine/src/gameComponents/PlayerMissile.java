package gameComponents;

import core.Component;
import core.components.BoxCollider;
import core.components.Spatial2D;
import dataTypes.Vector2;
import main.DamageDealer;
import main.GameConstants;

/**
 * Missile joueur (se déplace en ligne droite)
 */
public class PlayerMissile extends Component implements DamageDealer
{

	public static final float LIFESPAN = 1;
	
	public Vector2 velocity = Vector2.zero();
	
	private float livedTime = 0;
	
	private Hitbox hitbox;
	private Spatial2D spatial;
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		
		spatial = this.getOwner().getOrCreateComponent(Spatial2D.class);
		
		BoxCollider hitboxCollider = new BoxCollider(-15, -15, 30, 30);
		getOwner().addComponent(hitboxCollider);
		
		hitbox = new Hitbox(GameConstants.TEAM_PLAYER, true, false, 10);	//10 damages
		hitbox.colliders.add(hitboxCollider);
		hitbox.damageDealtCallback.add(this);
		getOwner().addComponent(hitbox);
	}
	
	
	@Override
	public void update(double delta) {
		// TODO Auto-generated method stub
		super.update(delta);
		
		spatial.translateGlobal(velocity.multiply(delta));
		spatial.setRotation(Math.atan2(velocity.x, -velocity.y));
		
		livedTime += delta;
		if(livedTime > LIFESPAN)
			getOwner().destroy();
	}
	
	
	
	@Override
	public void fixedUpdate(double delta) {
		// TODO Auto-generated method stub
		super.fixedUpdate(delta);
	}


	@Override
	public void dealtDamages(int damages) {
		getOwner().destroy();
	}
}
