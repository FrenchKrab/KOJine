package input;

public class KeyboardState {
	
	public KeyboardState() {}
	public KeyboardState(KeyboardState state)
	{
		this.keys = state.keys.clone();
	}
	
	public boolean[] keys = new boolean[InputKeyType.COUNT];
	
	public void setKey(InputKeyType key, boolean value)
	{
		keys[key.ordinal()] = value;
	}
	
	public boolean getKey(InputKeyType key)
	{
		return keys[key.ordinal()];
	}
	
	public static KeyboardState calculateDelta(KeyboardState from, KeyboardState to)
	{
		KeyboardState delta = new KeyboardState();
		for (int i = 0; i<from.keys.length; i++)
		{
			delta.keys[i] = from.keys[i] != to.keys[i];
		}
		return delta;
	}
	
	public void reset()
	{
		for(int i = 0; i < keys.length; i++)
		{
			keys[i] = false;
		}
	}
}
