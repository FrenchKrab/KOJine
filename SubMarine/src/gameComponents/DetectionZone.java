package gameComponents;

import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import core.Component;
import core.components.ColliderComponent;
import core.components.EllipseCollider;
import main.DetectionZoneCallback;
import main.TeamBelonger;

/**
 * Gère la collision entre triggers avec une gestion "d'équipes"
 * Il est à noter que l'implementation d'un system de "layer" de collision
 * aurait pu éviter la création de cette classe, cependant, le temps manque.
 */
public class DetectionZone extends Component implements TeamBelonger{
	
	public DetectionZone()
	{
		team = 0;
		detectable = true;
		canDetect = true;
		setZoneRadius(100);
	}
	
	public DetectionZone(int team, boolean detectable, boolean canDetect, double radius)
	{
		this.team = team;
		this.detectable = detectable;
		this.canDetect = canDetect;
		setZoneRadius(radius);
	}
	
	/**
	 * Equipe à laquelle le propriétaire de cette zone appartient
	 */
	public int team = 0;
	
	/**
	 * Cette zone est-elle detectable ?
	 */
	public boolean detectable;
	
	/**
	 * Cette zone peut-elle detecter ?
	 */
	public boolean canDetect;

	/**
	 * Collider associé à cette zone
	 */
	private EllipseCollider detectionArea;
	
	public ArrayList<DetectionZoneCallback> callbacks = new ArrayList<DetectionZoneCallback>();
	
	@Override
	public void start() {
		super.start();
		getOwner().addComponent(this.detectionArea);
	}
	
	
	@Override
	public void onTriggerEnter(ColliderComponent ownCollider, ColliderComponent otherCollider)
	{
		super.onTriggerEnter(ownCollider, otherCollider);

		//Si cette zone n'est pas chargée de detecter, ces tests ne servent à rien
		if(!canDetect)
			return;
		
		//Si le collider dont il est question est bien celui de cette detection zone
		if(ownCollider == detectionArea)
		{
			//Si l'autre collider possede une zone, que ce n'est pas celle de ce collider, et que
			//c'est avec le collider de cette zone qu'on est en contact
			DetectionZone otherZone = otherCollider.getOwner().getComponent(DetectionZone.class);
			if(otherZone != null && otherZone != this && otherZone.detectable && otherZone.detectionArea == otherCollider && this.getTeam()!=otherZone.getTeam())
			{
				for(DetectionZoneCallback cb : callbacks)
				{
					cb.ZoneEntered(this, otherZone);
				}
			}
		}
	}
	
	@Override
	public void onTriggerExit(ColliderComponent ownCollider, ColliderComponent otherCollider)
	{
		super.onTriggerEnter(ownCollider, otherCollider);
		
		//Si cette zone n'est pas chargée de detecter, ces tests ne servent à rien
		if(!canDetect)
			return;
		
		//Si le collider dont il est question est bien celui de cette detection zone
		if(ownCollider == detectionArea)
		{
			//Si l'autre collider possede une zone, que ce n'est pas celle de ce collider, et que
			//c'est avec le collider de cette zone qu'on est en contact
			DetectionZone otherZone = otherCollider.getOwner().getComponent(DetectionZone.class);
			if(otherZone != null && otherZone != this && otherZone.detectable && otherZone.detectionArea == otherCollider && this.getTeam()!=otherZone.getTeam())
			{
				for(DetectionZoneCallback cb : callbacks)
				{
					cb.ZoneExited(this, otherZone);
				}
			}
		}
	}
	
	/**
	 * Redimensionne la zone
	 * @param radius Nouveau rayon de la zone
	 */
	public void setZoneRadius(double radius)
	{
		Ellipse2D ellipse = new Ellipse2D.Double(-radius, -radius, radius*2, radius*2);
		if(this.detectionArea == null)
		{
			detectionArea = new EllipseCollider(ellipse);
		}
		else
		{
			this.detectionArea.ellipse = ellipse;
		}
		
	}

	@Override
	public int getTeam() 
	{
		return this.team;
	}
}
