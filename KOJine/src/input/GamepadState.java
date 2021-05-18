package input;



/**
 * A class containing the state of a controller
 * @author Kraby
 * Inspiration taken from 
 * https://github.com/StrikerX3/JXInput
 * 
 */
public class GamepadState {
	
	public GamepadState() {}
	
	public GamepadState(GamepadState i)
	{
		this.a = i.a;
		this.b = i.b;
		this.y = i.y;
		this.x = i.x;
		this.up = i.up;
		this.right = i.right;
		this.down = i.down;
		this.left = i.left;
		this.select = i.select;
		this.start = i.start;
		this.lShoulder = i.lShoulder;
		this.rShoulder = i.rShoulder;
		this.lThumb = i.lThumb;
		this.rThumb = i.rThumb;
		
		this.lStickX = i.lStickX;
		this.lStickY = i.lStickY;
		this.rStickX = i.rStickX;
		this.rStickY = i.rStickY;
		this.lTrigger = i.lTrigger;
		this.rTrigger = i.rTrigger;
	}
	
	public boolean a, b, y, x;
	public boolean up, right, down, left;
	public boolean select, start;
	public boolean lShoulder, rShoulder;
	public boolean lThumb, rThumb;
	
	public float lStickX, lStickY;	//Left joystick
	public float rStickX, rStickY;	//Right joystick
	public float lTrigger, rTrigger;	//Analog triggers
	
	public static GamepadState calculateDelta(GamepadState from, GamepadState to)
	{
		GamepadState delta = new GamepadState();
		
		delta.a = from.a != to.a;
		delta.b = from.b != to.b;
		delta.x = from.x != to.x;
		delta.y = from.y != to.y;
		delta.up = from.up != to.up;
		delta.right = from.right != to.right;
		delta.down = from.down != to.down;
		delta.left = from.left != to.left;
		delta.select = from.select != to.select;
		delta.start = from.start != to.start;
		delta.lShoulder = from.lShoulder != to.lShoulder;
		delta.rShoulder = from.rShoulder != to.rShoulder;
		delta.lThumb = from.lThumb != to.lThumb;
		delta.rThumb = from.rThumb != to.rThumb;
		
		delta.lStickX = to.lStickX - from.lStickX;
		delta.lStickY = to.lStickY - from.lStickY;
		delta.rStickX = to.rStickX - from.rStickX;
		delta.rStickY = to.rStickY - from.rStickY;
		delta.lTrigger = to.lTrigger - from.lTrigger;
		delta.rTrigger = to.rTrigger - from.rTrigger;
		
		return delta;
	}
	
}
