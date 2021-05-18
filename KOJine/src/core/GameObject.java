package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import core.components.ColliderComponent;
import physics.TriggerData;

/**
 * A GameObject, the class that represent almost any in-game object
 */
public class GameObject {
	
	public GameObject ()
	{
		
	}
	
	public GameObject(GameWorld world)
	{
		this.setWorld(world);
	}
	
	/**
	 * The world this GameObject belongs to
	 */
	private GameWorld world;
	
	public String name = "";
	
	/**
	 * Is the GameObject active or not (==will its components be updated ?)
	 */
	public boolean active = true;
	
	/**
	 * UNUSED ! Planned to be used for a hierarchy based engine.
	 * Too hard to implement given limited time and knowledge
	 */
	private GameObject parent;
	
	/**
	 * UNUSED ! Planned to be used for a hierarchy based engine.
	 * Too hard to implement given limited time and knowledge
	 */
	private ArrayList<GameObject> children = new ArrayList<>();
	
	/**
	 * Component stored in a hashmap for efficiency. key=component class, value=arraylist of component class
	 */
	private HashMap<Class<? extends Component>, ArrayList<Component>> componentsHashMap = new HashMap<>();
	
	/**
	 * Contains all the components of this gameobject
	 */
	private ArrayList<Component> allComponents = new ArrayList<Component>();
	
	/**
	 * Contains all the components of this gameobject on which the start() method hasn't been called yet
	 */
	private ArrayList<Component> componentsToStart = new ArrayList<Component>();
	
	//---Cache---
	/**
	 * Holds all collider components this gameobject has
	 */
	private ArrayList<ColliderComponent> colliderComponents = new ArrayList<ColliderComponent>();
	
	
	void start()
	{
		//We make a clone because componentsToStart might be modified (if components try to add components
		// , which will need to be started), that would result in making the for loop crash
		ArrayList<Component> componentToStartTemp = (ArrayList<Component>) this.componentsToStart.clone();
		for(Component cToStart : componentToStartTemp)
		{
			cToStart.start();
		}
		componentsToStart.removeAll(componentToStartTemp);	//Remove all components already initalized
	}
	
	
	void update(double delta) 
	{
		this.start();	//Call start in components that need it
		for(Component c : this.allComponents)
		{
			if(!componentsToStart.contains(c))	//If started
				c.update(delta);
		}
	}
	
	
	void fixedUpdate(double delta)
	{
		for(Component c : this.allComponents)
		{
			c.fixedUpdate(delta);
		}
	}
	
	
	public void onTrigger(ColliderComponent ownCollider, ColliderComponent otherCollider,TriggerData data)
	{
		for(Component c : this.allComponents)
		{
			c.onTrigger(ownCollider, otherCollider, data);
		}
	}
	
	public void onTriggerEnter(ColliderComponent ownCollider, ColliderComponent otherCollider)
	{
		for(Component c : this.allComponents)
		{
			c.onTriggerEnter(ownCollider, otherCollider);
		}
	}
	
	public void onTriggerExit(ColliderComponent ownCollider, ColliderComponent otherCollider)
	{
		for(Component c : this.allComponents)
		{
			c.onTriggerExit(ownCollider, otherCollider);
		}
	}
	
	/**
	 * Set a new GameObject as parent of this one. Set as null to remove parenting.
	 * NOTE : currently not working/useless
	 * @param parent The new parent GameObject 
	 */
	public void setParent(GameObject parent)
	{
		this.parent = parent;
		if(parent != null)
		{
			this.world = parent.world;
		}
	}
	
	/**
	 * Get this GameObject's parent. 
	 * NOTE : currently not working/useless
	 * @return This GameObject's parent (null if it has none)
	 */
	public GameObject getParent()
	{
		return this.parent;
	}
	
	/**
	 * Get the world this GameObject belongs to
	 * @return the world if it exists, null else.
	 */
	public GameWorld getWorld()
	{
		return this.world;
	}
	
	/**
	 * Set the world this GameObject belongs to
	 * @param world New world the GameObject will belong to
	 */
	public void setWorld(GameWorld world)
	{
		if(this.world != null)
		{
			destroy();	//Change not tested, might cause problem ?
		}
		this.world = world;
		
		if(world != null)
		{
			world.addGameObject(this);
		}

	}
	
	/**
	 * Destroy this GameObject (will disappear next frame)
	 */
	public void destroy()
	{
		if(this.world != null)
		{
			this.world.removeGameObject(this);
		}
	}
	
	/**
	 * Attach a component to this GameObject.
	 * @param newComponent The Component to attach
	 */
	public void addComponent(Component newComponent)
	{
		Class<? extends Component> newComponentClass = newComponent.getClass();
		
		//If component (is null) or (is unique and already present)
		if(newComponent==null || (newComponent.isUnique() && componentsHashMap.containsKey(newComponentClass)))
			return;
		
		//Add the new component to "components" member
		allComponents.add(newComponent);
		
		//Add the new component to the components hashmap

		ArrayList<Component> hashList;
		
		if(!componentsHashMap.containsKey(newComponentClass))
		{
			hashList = new ArrayList<Component>();
			componentsHashMap.put(newComponentClass, hashList);
		}
		else
		{
			hashList = componentsHashMap.get(newComponentClass);
		}
		
		hashList.add(newComponent);
		
		//Set the component as a component to start
		this.componentsToStart.add(newComponent);
		
		//If the component is a collider, add it to the cached collider list
		if(newComponent instanceof ColliderComponent)
		{
			colliderComponents.add((ColliderComponent)newComponent);
		}
		
		//Set this gameobject as its owner
		newComponent.setOwner(this);
	}
	

	/**
	 * Returns the first attached Component of type t.
	 * @param t	The type of component wanted
	 * @return First component of type t found. Returns null if no corresponding component found.
	 */
	public <T extends Component> T getComponent(Class<T> t)
	{
		//System.out.println(t.toGenericString() + " check if found ..");
		if(componentsHashMap.containsKey(t))
		{
			//System.out.println(t.toGenericString() + " k found an entry");
			ArrayList<Component> c = componentsHashMap.get(t);
			//System.out.println("SIZE : " + c.size());
			if(c.size()>=1)
			{
				//System.out.println(t.toGenericString() + " size > = 1 is null :" + (c.get(0)==null) + " | casted null :" + ((T)c.get(0)==null));
				return (T) c.get(0);
			}
		}
		return null;
	}
	
	
	/**
	 * Get an array with all components of type t on this object.
	 * @param t	The type of wanted components
	 * @return	
	 */
	public <T extends Component> Component[] getComponents(Class<T> t)
	{
		if(componentsHashMap.containsKey(t))
		{
			ArrayList<Component> components = componentsHashMap.get(t);
			Component[] componentsArray = new Component[components.size()];
			components.toArray(componentsArray);
			return componentsArray;
		}
		return null;
	}
	
	/**
	 * Retrieve a component that contains the specified tag
	 * @param t Component type
	 * @param tag The desired tag
	 * @return First occurence of a component of type t that contains the specified tag
	 */
	public <T extends Component> T getComponentWithTag(Class<T> t, String tag)
	{
		if(componentsHashMap.containsKey(t))
		{
			ArrayList<Component> comps = componentsHashMap.get(t);
			for(Component c : comps)
			{
				if(c.tag.equalsIgnoreCase(tag))
				{
					return (T)c;
				}
			}
		}
		return null;
	}

	/**
	 * Retrieve all tagged components
	 * @param t Type of the components to retrieve
	 * @param tag The desired tag
	 * @return All components of type t that contains the specified tag
	 */
	public <T extends Component> Component[] getComponentsWithTag(Class<T> t, String tag)
	{
		if(componentsHashMap.containsKey(t))
		{
			ArrayList<Component> components = componentsHashMap.get(t);
			ArrayList<Component> correctlyTaggedComponents = new ArrayList<Component>();
			for(Component c : components)
			{
				if(c.tag.equalsIgnoreCase(tag))
				{
					correctlyTaggedComponents.add(c);
				}
			}
			Component[] componentsArray = new Component[correctlyTaggedComponents.size()];
			correctlyTaggedComponents.toArray(componentsArray);
			return componentsArray;
		}
		return null;
	}
	
	/**
	 * Retrieve the first occurence of a component type, and if it doesn't exist, create it.
	 * @param t Type of the wanted component
	 * @return Desired component
	 */
	public <T extends Component> T getOrCreateComponent(Class<T> t)
	{
		T component = this.getComponent(t);
		if(component != null)
		{
			return component;
		}
		else
		{
			try {
				component = t.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.addComponent(component);
			return component;
		}
	}
	
	/**
	 * Get all components attached to this GameObject
	 * @return
	 */
	public Component[] getAllComponents()
	{
		Component[] componentsArray = new Component[allComponents.size()];
		allComponents.toArray(componentsArray);
		return componentsArray;
	}
	
	/**
	 * Get all colliders attached to this GameObject
	 * (main use: physics engine cache)
	 * @return
	 */
	public ArrayList<ColliderComponent> getColliders()
	{
		return colliderComponents;
	}

	
}
