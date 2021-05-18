package gameComponents;

import core.Component;
import core.GameObject;
import core.components.Spatial2D;
import dataTypes.Vector2;

/**
 * Component permettant de faire suivre � un GameObject un autre
 */
public class AttachComponent extends Component {
	
	public AttachComponent()
	{}
	
	/**
	 * 
	 * @param target Cible � suivre
	 * @param followPosition Doit-on suivre la position ?
	 * @param positionOffset D�calage de ce GameObject par rapport � la position de la cible
	 * @param followRotation Doit-on suivre la rotation ?
	 * @param rotationOffset D�calage de rotation de ce GameObject par rapport � la rotation de la cible
	 */
	public AttachComponent(GameObject target, boolean followPosition, Vector2 positionOffset, boolean followRotation, float rotationOffset)
	{
		this.setTarget(target);
		this.followPosition = followPosition;
		this.positionOffset = positionOffset;
		this.followRotation = followRotation;
		this.rotationOffset = rotationOffset;
	}
	
	/**
	 * D�calage de ce GameObject par rapport � la position de la cible
	 */
	public Vector2 positionOffset = new Vector2(0,0);
	
	/**
	 * D�calage de rotation de ce GameObject par rapport � la rotation de la cible
	 */
	public float rotationOffset = 0;
	
	/**
	 * L'offset de position est-il relatif � la rotation et l'�chelle de la cible ?
	 */
	public boolean relativePositionOffset = false;
	
	/**
	 * D�calage de ce GameObject par rapport � la position de la cible
	 */
	public boolean followPosition = true;
	
	/**
	 * D�calage de rotation de ce GameObject par rapport � la rotation de la cible
	 */
	public boolean followRotation = false;
	
	private Spatial2D spatial;
	private Spatial2D targetSpatial;
		
	
	@Override
	public void start() {
		super.start();
		spatial = this.getOwner().getOrCreateComponent(Spatial2D.class);
	}
	
	
	@Override
	public void update(double delta) {

		if(targetSpatial != null)
		{
			if(followRotation)
			{
				spatial.setRotation(targetSpatial.getRotationAngle() + rotationOffset);
			}
			if(followPosition)
			{
				spatial.setPosition(targetSpatial.getPosition());
				Vector2 offset = Vector2.zero();
				if(relativePositionOffset)
				{
					offset = targetSpatial.right().multiply(positionOffset.x);
					offset = offset.add(targetSpatial.up().multiply(positionOffset.y));
				}
				else
				{
					offset = positionOffset;
				}
				spatial.translate(offset);
			}
		}
	}
	
	
	/**
	 * D�finir une nouvelle cible � suivre
	 * @param target Nouvelle cible
	 */
	public void setTarget(GameObject target)
	{
		this.targetSpatial = target.getOrCreateComponent(Spatial2D.class);
	}
	
}
