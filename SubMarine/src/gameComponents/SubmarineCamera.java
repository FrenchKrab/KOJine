package gameComponents;

import core.Component;
import core.GameObject;
import core.components.Spatial2D;
import core.managers.InputManager;
import dataTypes.Vector2;
import input.InputKeyType;

/**
 * Permet de controller la caméra
 */
public class SubmarineCamera extends Component {
	
	private float zoom = 1;
	private Spatial2D spatial;
	private AttachComponent attach;
	
	@Override
	public void start() {
		super.start();
		spatial = this.getOwner().getOrCreateComponent(Spatial2D.class);
		attach = this.getOwner().getComponent(AttachComponent.class);
	}
	
	
	@Override
	public void update(double delta) {
		InputManager input = getOwner().getWorld().getInputManager();
		
		if(input.isKeyJustPressed(InputKeyType.KEY_L))
		{
			attach.followRotation = !attach.followRotation;
		}
		if(input.isKeyPressed(InputKeyType.KEY_P))
		{
			spatial.scale(1+delta,1+delta);
		}
		if(input.isKeyPressed(InputKeyType.KEY_O))
		{
			spatial.scale(1-delta,1-delta);
		}
	}

}
