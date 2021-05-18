import core.Component;
import core.GameWorld;
import core.Time;
import core.components.ColliderComponent;
import core.components.Spatial2D;
import core.managers.InputManager;
import dataTypes.Vector2;
import input.InputKeyType;
import physics.TriggerData;


/**
 *	This is a test class, used for debug purposes
 *	no longer used.
 */
public class TestComponent extends Component {

	private Spatial2D spatial;

	@Override
	public void start() {
		super.start();
		spatial = getOwner().getOrCreateComponent(Spatial2D.class);

	}

	@Override
	public void update(double delta) {
		super.update(delta);
		
		//System.out.println((int)(1/delta)+ " FPS");
		
		GameWorld world = getOwner().getWorld();
		InputManager input = world.getInputManager();
		Time t = world.getTime();
		
		float speed = 200;
		float rotationSpeed = 4f;
		float zoomSpeed = 1f;
		
		Vector2 move = new Vector2(0,0);
		float rotationOffset = 0;
		float zoomOffset = 1;
		if(input.isKeyPressed(InputKeyType.KEY_RIGHT))
		{
			move.x += speed;
		}
		if(input.isKeyPressed(InputKeyType.KEY_LEFT))
		{
			move.x -= speed;
		}
		if(input.isKeyPressed(InputKeyType.KEY_UP))
		{
			move.y -= speed;
		}
		if(input.isKeyPressed(InputKeyType.KEY_DOWN))
		{
			move.y += speed;
		}
		
		if(input.isKeyPressed(InputKeyType.KEY_Z))
		{
			rotationOffset += rotationSpeed;
		}
		if(input.isKeyPressed(InputKeyType.KEY_A))
		{
			rotationOffset -= rotationSpeed;
		}
		
		if(input.isKeyPressed(InputKeyType.KEY_Q))
		{
			zoomOffset += zoomSpeed * delta;
		}
		if(input.isKeyPressed(InputKeyType.KEY_S))
		{
			zoomOffset -= zoomSpeed * delta;
		}
		
		rotationOffset *= delta;
		//zoomOffset *= delta;
		
		move = move.multiply(delta);
		
		//spatial.setPosition(100 * Math.cos(t.time()), 100 * Math.sin(t.time()));
		spatial.translate(move);
		spatial.rotate(rotationOffset);
		spatial.scale(zoomOffset, zoomOffset);
		
		//spatial.setPosition(200 * Math.cos(t.time()), 200 * Math.sin(t.time()));
		//spatial.translateGlobal(-delta*20, 0);
		//System.out.println(spatial.getPosition().x + " , " + spatial.getPosition().y);
		//System.out.println(spatial.getRotationAngle());
	}
	
	@Override
	public void onTrigger(ColliderComponent ownCollider, ColliderComponent otherCollider, TriggerData data)
	{
		super.onTrigger(ownCollider, otherCollider, data);
		
		//System.out.println("TOUCH");
	}
	
	@Override
	public void onTriggerEnter(ColliderComponent ownCollider, ColliderComponent otherCollider)
	{
		super.onTriggerEnter(ownCollider, otherCollider);
		
		System.out.println("--IN");
	}
	
	@Override
	public void onTriggerExit(ColliderComponent ownCollider, ColliderComponent otherCollider)
	{
		super.onTriggerExit(ownCollider, otherCollider);
		
		System.out.println("--OUT");
	}
}
