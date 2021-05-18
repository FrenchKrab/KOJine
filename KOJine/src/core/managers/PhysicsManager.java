package core.managers;

import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import core.GameObject;
import core.components.ColliderComponent;
import physics.TriggerData;

/**
 * Manage all collisions
 * NOTE : probably won't do many things , collision response isn't needed, only colliders overlaping should
 * be enough
 */
public class PhysicsManager {
	
	/**
	 * The height/width of each cell of the partition grid
	 */
	private final static double PARTITION_SIZE = 32;
	
	/**
	 * Contains all colliders belonging to each partition.
	 * See CalculatePartitionKey for how keys are handled.
	 */
	private HashMap<Double, ArrayList<ColliderComponent>> collisionPartition = new HashMap<Double, ArrayList<ColliderComponent>>();
	
	/**
	 * Remove all partition data associated to a collider
	 * @param col The collider to remove data
	 */
	public void RemoveColliderPartitionData(ColliderComponent col)
	{
		for(Double d : col.parentPartitions)
		{
			collisionPartition.get(d).remove(col);
		}
	}
	
	
	/**
	 * Returns the keys of all containing partitions of this collider
	 * @param col
	 * @return ArrayList of keys
	 */
	public ArrayList<Double> UpdateColliderPartitionning(ColliderComponent col)
	{
		ArrayList<Double> containingPartitions = new ArrayList<Double>();
		
		Area area = col.getArea();
		if(area != null)
		{
			//Go through every partition cell we belong
			Rectangle bounds = area.getBounds();
			int xMin = (int)(bounds.x/PARTITION_SIZE);
			int xMax = (int)((bounds.x+bounds.width)/PARTITION_SIZE);
			int yMin = (int)(bounds.y/PARTITION_SIZE);
			int yMax =  (int)((bounds.y + bounds.height)/PARTITION_SIZE);
			for(int x = xMin ; x <= xMax ; x++)
			{
				for(int y = yMin ; y <= yMax ; y++)
				{
					double ID = CalculatePartitionKey(x,y);
					ArrayList<ColliderComponent> partition = collisionPartition.get(ID); 
					if(partition == null)
					{
						partition = new ArrayList<ColliderComponent>();
						collisionPartition.put(ID, partition);
					}
					
					partition.add(col);
					containingPartitions.add(ID);
				}
			}
		}
		//System.out.println(containingPartitions.size());
		return containingPartitions;
	}
	
	/**
	 * Gives a partition key (double) from a partition position (x,y)
	 * @param x partition's x index
	 * @param y partition's y index
	 * @return Partition's key
	 */
	private static Double CalculatePartitionKey(int x, int y)
	{
		//decimals store the y, whole number store the x. There shouldn't be too many keys collisions.
		return x+y*0.0001;	
	}
	
	
	
	/**
	 * (OBSOLETE) Detect collisions and triggers and call methods accordingly.
	 * NOTE: you can test performance without spatial partitionning by using this method instead
	 * of DoCollisionDetection (inverse their name for example)
	 * @param gameObjects The gameobject list
	 */
	public void DoCollisionDetectionWithoutSpatialPartitionning(ArrayList<GameObject> gameObjects)
	{
		int count = gameObjects.size();
		for(int i = 0; i<count; i++)
		{
			GameObject go1 = gameObjects.get(i);
			if(go1.active)
			{
				ArrayList<ColliderComponent> componentList1 = go1.getColliders();
				for(ColliderComponent collider1 : componentList1)
				{
					if(collider1.isActive())
					{
						for(int j = i+1; j<count; j++)
						{
							GameObject go2 = gameObjects.get(j);
							if(go2.active)
							{
								ArrayList<ColliderComponent> componentList2 = go2.getColliders();
								for(ColliderComponent collider2 : componentList2)
								{
									if(collider2.isActive())
									{
										TestTwoColliders(collider1, collider2);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Detect collisions and triggers and call methods accordingly.
	 * @param gameObjects The gameobject list
	 */
	public void DoCollisionDetection(ArrayList<GameObject> gameObjects)
	{
		//Compute all partition data for all colliders
		for(GameObject go : gameObjects)
		{
			ArrayList<ColliderComponent> componentList = go.getColliders();
			for(ColliderComponent c : componentList)
			{
				c.updatePartitionData();
			}		
		}
		
		//Detect collisions for all colliders
		int count = gameObjects.size();
		for(int i = 0; i<count; i++)
		{
			GameObject go1 = gameObjects.get(i);
			if(go1.active)
			{
				ArrayList<ColliderComponent> componentList1 = go1.getColliders();
				for(ColliderComponent col1 : componentList1)
				{
					if(col1.isActive())
					{
						//Because multiple partitions can contain the same collider, store
						//which colliders we already collided with (huge performance gain).
						HashSet<ColliderComponent> alreadyCollided = new HashSet<>();
						//For each partition of the collider col1
						for (Double partitionId : col1.parentPartitions)
						{
							ArrayList<ColliderComponent> partition = collisionPartition.get(partitionId);
							//For each collider col2 of the given partition
							for(ColliderComponent col2 : partition)
							{
								//If col2 isn't col1, and col2 is active, and col1 & col2 do not belong to the same GameObject
								if(col2!=col1 && col2.isActive() && go1 != col2.getOwner() && !alreadyCollided.contains(col2))
								{
									//Then there is a possible collision between them, test it
									if(TestTwoColliders(col1, col2))
										alreadyCollided.add(col2);
								}
							}
							partition.remove(col1);
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Test collisions between two colliders and notify them of it.
	 * @param col1
	 * @param col2
	 * @return True if collision occured and collision between these colliders don't need to be checked again, false else
	 */
	private boolean TestTwoColliders(ColliderComponent col1, ColliderComponent col2)
	{
		Area area1 = col1.getArea();
		Area area2 = col2.getArea();
		//If one of them don't have area, no need to check collision between them
		//later, return true
		if(area1 == null || area2 == null)
			return true;
		
		Area intersection = new Area(area1);
		intersection.intersect(area2);
		if(!intersection.isEmpty())
		{
			//TODO : Add support for things other than triggers
			//WARNING : this "if" is currently ALWAYS verified as true
			if(true || col1.isTrigger || col2.isTrigger)
			{
				TriggerData triggerData = new TriggerData();
				triggerData.collisionArea = new Area(intersection);
				col1.getOwner().onTrigger(col1, col2, triggerData);
				col2.getOwner().onTrigger(col2, col1, triggerData);
				return true;
			}
		}
		//No collisions occured, but maybe in another spatial partition, a collision will
		//occur, return false
		return false;
	}
	
}
