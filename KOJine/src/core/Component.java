package core;

import core.components.ColliderComponent;
import physics.TriggerData;

/**
 * The basis for a component that can be attached on a GameObject
 */
public class Component {
	
	/**
	 * Is the Component active
	 */
	private boolean active = true;
	
	/**
	 * Component's owner GameObject
	 */
	private GameObject owner;
	
	/**
	 * How's tagged the component
	 */
	public String tag = "";
	
	
	/**
	 * Gets the Component's owner
	 * @return This component's owner (a GameObject)
	 */
	public GameObject getOwner() 
	{
		return owner;
	}
	
	
	/**
	 * Change this GameObject's owner (probably shouldn't be used much)
	 * @param owner	The new owner of this Component
	 */
	public void setOwner(GameObject owner)
	{
		this.owner = owner;
	}
	
	/**
	 * Sets this component as active (will be updated) or not
	 * @param active New active value
	 */
	public void setActive(boolean active)
	{
		this.active = active;
	}
	
	/**
	 * Returns if this component is active or not
	 * @return If the component is active
	 */
	public boolean isActive()
	{
		return this.active;
	}
	
	/**
	 * Override this to change behaviour
	 * If returns true, only one component of this kind can be added to a GameObject
	 * @return true if the component must be unique, false else
	 */
	public boolean isUnique()
	{
		return false;
	}
	
	/**
	 * Called at the creation / initialization of this component
	 */
	public void start() {}
	
	
	/**
	 * Called every game loop
	 * @param delta : the time between two update call
	 */
	public void update(double delta) {}
	
	
	/**
	 * Called a fixed number of time per second.
	 * @param delta : time between two fixedUpdate call
	 */
	public void fixedUpdate(double delta) {}
	
	/**
	 * Called when a collider on this gameobject touches another trigger
	 * @param ownCollider
	 * @param otherCollider
	 * @param data additional data
	 */
	public void onTrigger(ColliderComponent ownCollider, ColliderComponent otherCollider,TriggerData data) {}
	
	/**
	 * Called when a collider on this gameobject just stopped colliding with another trigger
	 * @param ownCollider
	 * @param otherCollider
	 */
	public void onTriggerExit(ColliderComponent ownCollider, ColliderComponent otherCollider) {}
	
	/**
	 * Called when a collider on this gameobject just started colliding with another trigger
	 * @param ownCollider
	 * @param otherCollider
	 */
	public void onTriggerEnter(ColliderComponent ownCollider, ColliderComponent otherCollider) {}
}
