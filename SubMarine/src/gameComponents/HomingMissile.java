package gameComponents;

import core.Component;
import core.components.BoxCollider;
import core.components.ColliderComponent;
import core.components.Spatial2D;
import dataTypes.Vector2;
import main.DamageDealer;
import main.GameConstants;

/**
 * Missile téléguidé vers une cible
 */
public class HomingMissile extends Component implements DamageDealer{
	
	public Spatial2D target;
	public float maxSpeed = 200;
	public float acceleration = 200;
	public float lifespan = 5;
	
	private double rotation = 0;
	private double speed = 0;
	private float timeOld = 0;
	
	private Spatial2D spatial;
	private ColliderComponent hitboxCollider;
	private Hitbox hitbox;
	
	@Override
	public void start() {
		super.start();
		spatial = getOwner().getOrCreateComponent(Spatial2D.class);
		
		Vector2 targetDirection = target.getPosition().substract(spatial.getPosition()).getNormalized();
		double targetRotation = Math.atan2(targetDirection.x, -targetDirection.y);
		rotation = targetRotation;
		speed = maxSpeed;
		
		hitboxCollider = new BoxCollider(-5, -5, 10, 10);
		getOwner().addComponent(hitboxCollider);
		
		hitbox = new Hitbox(GameConstants.TEAM_ENNEMY, true, false, 10);	//10 damages
		hitbox.colliders.add(hitboxCollider);
		hitbox.damageDealtCallback.add(this);
		getOwner().addComponent(hitbox);
	}
	
	
	@Override
	public void update(double delta) {
		super.update(delta);
		
		if(target != null)
		{
			Vector2 targetDirection = target.getPosition().substract(spatial.getPosition()).getNormalized();
			double targetRotation = Math.atan2(targetDirection.x, -targetDirection.y);
			if(targetRotation > rotation)
			{
				while(targetRotation - rotation > Math.PI)
				{
					targetRotation -= Math.PI*2;
				}
			}
			else
			{
				while(targetRotation - rotation < -Math.PI)
				{
					targetRotation += Math.PI*2;
				}
			}
			
			rotation += (targetRotation - rotation) * delta ;
			
			spatial.setRotation(rotation);
			
			//System.out.println(target.getPosition().substract(spatial.getPosition()).getNormalized().toString());
		}
		
		Vector2 dir = new Vector2(Math.sin(rotation), -Math.cos(rotation));
		spatial.translateGlobal(dir.multiply(speed*delta));
		
		timeOld += delta;
		if(timeOld>lifespan)
		{
			getOwner().destroy();
		}
		
	}


	@Override
	public void dealtDamages(int damages) {
		getOwner().destroy();
	}
}
