package core.components;

import java.awt.geom.AffineTransform;

import core.Component;

/**
 * A component that acts as a camera
 */
public class Camera2D extends Component {

	public Camera2D ()
	{
		super();
	}
	
	/**
	 * Spatial belonging to this component's GameObject
	 */
	private Spatial2D spatial;
	

	@Override
	public void start() {
		super.start();
		spatial = getOwner().getOrCreateComponent(Spatial2D.class);
	}

	@Override
	public void update(double delta) {
		//Send a camera render request to the render manager
		this.getOwner().getWorld().getRenderManager().addCameraRenderRequest(spatial.getTransform());
	}
}
