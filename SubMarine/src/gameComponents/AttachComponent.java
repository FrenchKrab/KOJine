package gameComponents;

import core.Component;
import core.GameObject;
import core.components.Spatial2D;
import dataTypes.Vector2;

/**
 * Component permettant de faire suivre à un GameObject un autre
 */
public class AttachComponent extends Component {
	
	public AttachComponent()
	{}
	
	/**
	 * 
	 * @param target Cible à suivre
	 * @param followPosition Doit-on suivre la position ?
	 * @param positionOffset Décalage de ce GameObject par rapport à la position de la cible
	 * @param followRotation Doit-on suivre la rotation ?
	 * @param rotationOffset Décalage de rotation de ce GameObject par rapport à la rotation de la cible
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
	 * Décalage de ce GameObject par rapport à la position de la cible
	 */
	public Vector2 positionOffset = new Vector2(0,0);
	
	/**
	 * Décalage de rotation de ce GameObject par rapport à la rotation de la cible
	 */
	public float rotationOffset = 0;
	
	/**
	 * L'offset de position est-il relatif à la rotation et l'échelle de la cible ?
	 */
	public boolean relativePositionOffset = false;
	
	/**
	 * Décalage de ce GameObject par rapport à la position de la cible
	 */
	public boolean followPosition = true;
	
	/**
	 * Décalage de rotation de ce GameObject par rapport à la rotation de la cible
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
	 * Définir une nouvelle cible à suivre
	 * @param target Nouvelle cible
	 */
	public void setTarget(GameObject target)
	{
		this.targetSpatial = target.getOrCreateComponent(Spatial2D.class);
	}
	
}
