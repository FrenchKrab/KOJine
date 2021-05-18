package input;

import core.managers.InputManager;

/**
 * Abstract class that represent an input feeder, that'll feed new input data to
 * the different input states (keyboard, gamepad, etc).
 */
public abstract class InputFeeder {
	
	private InputManager.InputManagerData data;
	
	/**
	 * Should only be called once by the InputManager to give the InputManagerData
	 * @param data
	 */
	public void initializeInputData(InputManager.InputManagerData data)
	{
		this.data = data;
	}
	
	protected KeyboardState getEditableKeyboardState()
	{
		return data.getEditableKeyboardState();
	}
	
	protected void trySettingKey(InputKeyType key, boolean value)
	{
		KeyboardState keyboard = this.getEditableKeyboardState();
		if(keyboard != null)
		{
			keyboard.setKey(key, value);
		}
	}
	
	//TODO: implement gamepads ?
	/*
	protected GamepadState getEditableKeyboardState()
	{
		return data.keyboard.getEditableNextState();
	}*/
}
