package core;


//Great resource for time management and desyncing physics and render updates
//https://gafferongames.com/post/fix_your_timestep/

/**
 * Time manager, responsible for all time processing
 * (calculating deltas and calling updates)
 */
public class Time {
	
	public Time(double fixedDeltaTime)
	{
		this.setFixedDeltaTime(fixedDeltaTime);
	}
	
	//times in nanosecond
	//1 ns = 1/1 000 000 ms = 1/1 000 000 000 s
	
	/**
	 * The current time (nanoseconds)
	 */
	private long currentTime = 0;
	
	/**
	 * Delta between current time and last frame (nanoseconds)
	 */
	private long deltaTime = 0;
	
	/**
	 * Accumulator, used to know when to call fixed update (nanoseconds)
	 */
	private long accumulator = 0;
	
	/**
	 * Current fixed time (last time fixedupdate was called) (nanoseconds)
	 */
	private long fixedTime = 0; 
	
	//cached data
	
	/**
	 * Current time in seconds
	 */
	private double currentTimeSeconds = 0;
	
	/**
	 * Current fixed time in seconds
	 */
	private double fixedTimeSeconds = 0;
	
	/**
	 * Current delta time in seconds
	 */
	private double deltaTimeSeconds;
	
	
	//settings
	
	/**
	 * Avoid big freezes by limiting the delta (the game will slow down instead)
	 */
	private long maxDelta = secondsToNano(1);
	
	/**
	 * The fixed delta time
	 */
	private long fixedDeltaTime = secondsToNano(0.05);
	
	//cached settings
	/**
	 * The fixed delta time in seconds
	 */
	private double fixedDeltaTimeSeconds;


	
	/**
	 * Call this everytime your mainloop loops
	 * @param a The class that'll get called for updates
	 */
	public void loopTurn(GameWorld a)
	{
		long newTime = System.nanoTime();
		long frameTime = newTime - currentTime;
		if(frameTime>maxDelta)
		{
			frameTime = maxDelta;
		}
		currentTime = newTime;
		currentTimeSeconds = nanoToSeconds(currentTime);
		
		deltaTime = frameTime;	//TODO: check if doesnt causes problems (it isnt the "true" delta time)
		deltaTimeSeconds = nanoToSeconds(deltaTime);
		
		accumulator += frameTime;
		if(accumulator>=fixedDeltaTime)
		{
			a.fixedUpdate(fixedDeltaTimeSeconds);
			updateFixedTime(fixedTime+fixedDeltaTime);
			accumulator -= fixedDeltaTime;
		}

		
		a.update(deltaTimeSeconds);
	}
	
	/**
	 * Update fixed delta time data
	 * @param newTime Current time at the moment this function was called
	 */
	private void updateFixedTime(long newTime)
	{
		fixedTime=newTime;
		fixedTimeSeconds=nanoToSeconds(fixedTime);
	}
	
	
	//Tools
	
	/**
	 * Convert seconds to nanoseconds
	 * @param seconds
	 * @return nanoseconds
	 */
	public long secondsToNano(double seconds)
	{
		return (long) (seconds * 1000000000);
	}
	
	/**
	 * Convert nano to seconds
	 * @param nano : nanoseconds
	 * @return seconds
	 */
	public double nanoToSeconds(long nano)
	{
		return nano/(double)1000000000;
	}
	
	
	/**
	 * Set the fixed delta time's delta
	 * @param delta New delta (in seconds)
	 */
	public void setFixedDeltaTime(double delta)
	{
		fixedDeltaTimeSeconds = delta;
		fixedDeltaTime = secondsToNano(delta);
	}
	
	
	/**
	 * Gives the current time in seconds
	 * @return time in seconds
	 */
	public double time()
	{
		return currentTimeSeconds;
	}
	
	/**
	 * Gives the current "fixed" time (used for fixedupdates) in seconds
	 * @return
	 */
	public double fixedTime()
	{
		return fixedTimeSeconds;
	}
	
	/**
	 * Returns the last delta time in seconds
	 * @return the delta time in seconds
	 */
	public double deltaTime()
	{
		return deltaTimeSeconds;
	}
	
	/**
	 * Returns the current fixed delta time in seconds
	 * @return The fixed delta time in seconds
	 */
	public double fixedDeltaTime()
	{
		return fixedDeltaTimeSeconds;
	}
	
	
}
