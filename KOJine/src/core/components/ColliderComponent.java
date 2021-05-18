package core.components;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;

import core.Component;
import core.managers.PhysicsManager;
import physics.TriggerData;

/**
 * Abstract class for any component that needs to implement collision behaviour
 */
public abstract class ColliderComponent extends Component {
	
	
	/**
	 * Is this collider a trigger (=only calls onTrigger methods)(CURRENTLY ONLY TRIGGERS ARE SUPPORTED)
	 */
	public boolean isTrigger = false;
	
	
	public ArrayList<Double> parentPartitions = new ArrayList<Double>();
	
	/**
	 * Contains triggers currently being added (currently in contact)
	 * Used to detect when a trigger enter or leaves this collider. (onTriggerEnter and onTriggerExit)
	 */
	private HashSet<ColliderComponent> nextTriggers = new HashSet<ColliderComponent>();
	
	/**
	 * Contains triggers from last fixed update (in contact last frame)
	 * Used to detect when a trigger enter or leaves this collider. (onTriggerEnter and onTriggerExit)
	 */
	private HashSet<ColliderComponent> lastFrameTriggers = new HashSet<ColliderComponent>();
	
	
	/**
	 * Get this component's collision area
	 * @return The collision area
	 */
	public abstract Area getArea();

	
	@Override
	public void fixedUpdate(double delta) {
		super.fixedUpdate(delta);
		
		//Check changes and call onTrigger(Enter/Exit) accordingly
		callTriggerFunctions();
		
		//Updates triggers lists for next checks
		lastFrameTriggers = nextTriggers;
		nextTriggers = new HashSet<ColliderComponent>();
		
		//updatePartitionData();
	}
		
	@Override
	public void onTrigger(ColliderComponent ownCollider, ColliderComponent otherCollider, TriggerData data) {
		super.onTrigger(ownCollider, otherCollider, data);
		
		//Indicate we're currently in contact with the other collider
		nextTriggers.add(otherCollider);
	}
	
	
	/**
	 * Call all onTriggerEnter and onTriggerExit functions as needed.
	 * This is done thanks to the nextTriggers and lastFrameTriggers ArrayLists.
	 */
	private void callTriggerFunctions()
	{
		//Collider that were triggered last fixed update, but arent this fixed update
		//(they just stopped having collision)
		for(ColliderComponent c : lastFrameTriggers)
		{
			if(!nextTriggers.contains(c))
			{
				this.getOwner().onTriggerExit(this, c);
			}
		}
		
		//Collider that weren't triggered last fixed update, but aren this fixed update
		//(they just started having collision)
		for(ColliderComponent c : nextTriggers)
		{
			if(!lastFrameTriggers.contains(c))
			{
				this.getOwner().onTriggerEnter(this, c);
			}
		}
	}

	/**
	 * Update all partition data related to this collider:
	 */
	public void updatePartitionData()
	{
		//Remove all partition data related to this collider
		getOwner().getWorld().getPhysicsManager().RemoveColliderPartitionData(this);
		//
		ArrayList<Double> partitions = getOwner().getWorld().getPhysicsManager().UpdateColliderPartitionning(this);
		parentPartitions = partitions;
	}
}
