package core;

import java.util.ArrayList;


import core.components.Camera2D;
import core.managers.InputManager;
import core.managers.PhysicsManager;
import core.managers.RenderManager;
import core.managers.ResourcesManager;
import core.managers.SoundManager;
import input.InputFeeder;
import rendering.RenderOutput;
import rendering.engines.SwingRenderOutput;


public class GameWorld {
	
	public GameWorld(RenderOutput renderOutput, InputFeeder feeder)
	{
		time = new Time(1f/15);
		this.renderManager = new RenderManager(renderOutput);
		this.inputManager = new InputManager(feeder);
	}
	
	/**
	 * Manage time on this world
	 */
	private Time time;
	
	/**
	 * Manage physics on this world
	 */
	private PhysicsManager physicsManager = new PhysicsManager();
	
	/**
	 * Manages inputs on this world
	 */
	private InputManager inputManager;
	
	/**
	 * Manages rendering on this world
	 */
	private RenderManager renderManager;
	
	/**
	 * UNUSED : should have managed sound on this world
	 */
	private SoundManager soundManager = new SoundManager();
	
	/**
	 * Manages resources loading on this world
	 */
	private ResourcesManager resources = new ResourcesManager();
	
	
	/**
	 * All gameobjects this world contains
	 */
	public ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
	
	/**
	 * GameObjects queued for adding
	 */
	private ArrayList<GameObject> gameObjectsToAdd = new ArrayList<GameObject>();
	
	/**
	 * GameObjects queued for removing/destroying
	 */
	private ArrayList<GameObject> gameObjectsToRemove = new ArrayList<GameObject>();
	
	/**
	 * Has the world been stopped ?
	 */
	private boolean worldEnded = false;
	
	/**
	 * The world's return value/return code
	 */
	private int worldReturnValue = 0;
	
	/**
	 * Start the mainloop
	 * @return the world's return value.
	 */
	public int mainLoop()
	{
		for(GameObject go : gameObjects)
		{
			go.start();
		}
		
		while(!worldEnded)
		{
			time.loopTurn(this);
		}
		
		return worldReturnValue;
	}
	
	/**
	 * Called as much times as possibles, constitutes the main loop,
	 * the delta can vary greatly and represents the time in seconds between
	 * this update call and the last.
	 * @param delta Time elapsed since the last update call (in seconds)
	 */
	void update(double delta)
	{
		//Clear all the drawing request
		renderManager.clearRequests();
		
		//Update received input data
		inputManager.update();
		
		//Add all gameobjects queued for adding
		gameObjects.addAll(gameObjectsToAdd);
		gameObjectsToAdd.clear();
		
		//Remove all gameobjects queueud for destroying
		gameObjects.removeAll(gameObjectsToRemove);
		gameObjectsToRemove.clear();
		
		//Main update loop for gameobjects and their components
		for(GameObject go : gameObjects)
		{
			go.update(delta);
		}
		
		//Render cameras
		renderManager.renderAll();
	}

	/**
	 * Actually isn't called at a fixed rate, but if used like it's called
	 * at a fixed rate, everything should work like it IS called at a fixed rate.
	 * @param delta Time elapsed since the last fixedUpdate call (in seconds) (should be constant if not changed by user)
	 */
	void fixedUpdate(double delta)
	{
		for(GameObject go : gameObjects)
		{
			go.fixedUpdate(delta);
		}
		
		//Calculate collisions
		physicsManager.DoCollisionDetection(gameObjects);
	}
	
	/**
	 * Adds a gameobject to this world (applied next frame)
	 * @param go
	 */
	void addGameObject(GameObject go)
	{
		gameObjectsToAdd.add(go);
		if(go.getWorld() != this)
		{
			go.setWorld(this);
		}
	}
	
	/**
	 * Remove a gameobjects for this world (applied next frame)
	 * @param go
	 */
	void removeGameObject(GameObject go)
	{
		gameObjectsToRemove.add(go);
	}
	
	
	/**
	 * End the world and make its return value 'returnCode'
	 * @param returnCode the world's return value
	 */
	public void endWorld(int returnCode)
	{
		worldEnded = true;
		worldReturnValue = returnCode;
	}
	
	/**
	 * Get this world's input manager
	 * @return
	 */
	public InputManager getInputManager()
	{
		return this.inputManager;
	}
	
	/**
	 * Get this world's render manager
	 * @return
	 */
	public RenderManager getRenderManager()
	{
		return this.renderManager;
	}
	
	/**
	 * Get this world's physics manager
	 * @return
	 */
	public PhysicsManager getPhysicsManager()
	{
		return this.physicsManager;
	}
	
	/**
	 * Get this world's time manager
	 * @return
	 */
	public Time getTime()
	{
		return this.time;
	}
	
	/**
	 * Get this world's resources manager
	 * @return
	 */
	public ResourcesManager getResources()
	{
		return this.resources;
	}
	
	/**
	 * Find a GameObject in this world with a given name
	 * @param name searched name
	 * @return the gameobject if found, null else
	 */
	public GameObject findGameObjectWithName(String name)
	{
		for(GameObject go : gameObjects)
		{
			if(go.name.equalsIgnoreCase(name))
				return go;
		}
		return null;
	}

}
