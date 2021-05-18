package core.managers;

import java.awt.event.KeyEvent;

import input.GamepadState;
import input.InputFeeder;
import input.InputKeyType;
import input.KeyboardState;


/**
 * Process all input data and allows to access it easily
 */
public class InputManager {
	public InputManager(InputFeeder feeder)
	{
		this.feeder = feeder;
		feeder.initializeInputData(this.inputData);
	}
	
	/**
	 * UNUSED: Store gamepad data (current state, delta, and next state)
	 */
	private class GamepadData
	{
		public GamepadData()
		{
			for(int i = 0; i<currentState.length; i++)
			{
				nextState[i] = new GamepadState();
				currentState[i] = new GamepadState();
				delta[i] = new GamepadState();
			}
		}
		
		private static final int CONTROLLER_COUNT = 4;
		
		private GamepadState[] nextState = new GamepadState[CONTROLLER_COUNT];
		
		private GamepadState[] currentState = new GamepadState[CONTROLLER_COUNT];
		private	GamepadState[] delta = new GamepadState[CONTROLLER_COUNT];
		
		/**
		 * Gives an GamepadState corresponding to the next state
		 * that you simply edit to set its value
		 * @param controllerId the controller's id
		 */
		public GamepadState getNextState(int controllerId)
		{
			if(controllerId > 0 && controllerId < nextState.length)
			{
				return nextState[controllerId];
			}
			else
			{
				System.out.println("ERROR : TRYING TO ACCESS A CONTROLLER ID THAT DOESNT EXISTS");
				return null;
			}
		}
		
		/**
		 * Update the current state and delta, call this every update
		 */
		public void update()
		{
			for(int i = 0; i<currentState.length; i++)
			{
				this.delta[i] = GamepadState.calculateDelta(currentState[i], nextState[i]);
				this.currentState[i] = new GamepadState(this.nextState[i]);	//clone
			}
		}
	}
	
	/**
	 * Store keyboard data (current state, delta, and next state)
	 */
	private class KeyboardData
	{
		
		/**
		 * The state currently being edited
		 */
		private KeyboardState nextState = new KeyboardState();
		
		/**
		 * The "previous" state, currently used to get input values
		 */
		private KeyboardState currentState = new KeyboardState();
		private KeyboardState delta = new KeyboardState();
		
		
		/**
		 * Gives an GamepadState corresponding to the next state
		 * that you simply edit to set its value
		 */
		public KeyboardState getNextState()
		{
			return this.nextState;
		}
		
		/**
		 * Gives the keyboard's current state. Warning: do NOT edit the key's values
		 * @return The keyboard's state
		 */
		public KeyboardState getCurrentState()
		{
			return this.currentState;
		}
		
		/**
		 * Gives a keyboard state that represent changes in the last frame.
		 * If a key value is true, it means it has changed.
		 * @return
		 */
		public KeyboardState getDelta()
		{
			return this.delta;
		}
		
		/**
		 * Update deltas and states. Should be called on GameWorld update
		 */
		public void update()
		{
			this.delta = KeyboardState.calculateDelta(currentState, nextState);
			this.currentState = new KeyboardState(this.nextState);
		}
	}
	
	/**
	 * Holds all input state and data
	 */
	public class InputManagerData
	{
		private KeyboardData keyboard = new KeyboardData();
		private GamepadData gamepads = new GamepadData();
		
		/**
		 * Calculate deltas, call this on world's update()
		 */
		public void update()
		{
			keyboard.update();
			gamepads.update();
		}
		
		/**
		 * Gets a state you can edit to indicate new inputs press
		 * @return
		 */
		public KeyboardState getEditableKeyboardState()
		{
			return keyboard.getNextState();
		}
		
		/**
		 * Get the current state of the keyboard
		 */
		public KeyboardState getCurrentKeyboardState()
		{
			return keyboard.getCurrentState();
		}
		
		/**
		 * Get the difference between this keyboard state and the previous one
		 */
		public KeyboardState getDeltaKeyboardState()
		{
			return keyboard.getDelta();
		}
	}
	
	
	/**
	 * Reponsible of feeding inputData with received inputs
	 */
	private InputFeeder feeder;
	
	/**
	 * This InputManager's data
	 */
	private InputManagerData inputData = new InputManagerData();
	
	
	/**
	 * Shouldn't be called by the user, used to update the input data every frame
	 */
	public void update()
	{
		inputData.update();
	}
	
	/**
	 * Is this key pressed
	 * @param key
	 * @return true if key pressed, false else
	 */
	public boolean isKeyPressed(InputKeyType key)
	{
		return inputData.getCurrentKeyboardState().getKey(key);
	}
	
	/**
	 * Has this key been pressed at this exact frame ?
	 * @param key
	 * @return true if key just pressed, false else
	 */
	public boolean isKeyJustPressed(InputKeyType key)
	{
		return isKeyPressed(key) && inputData.getDeltaKeyboardState().getKey(key);
	}
	
	/**
	 * Has this key been released at this exact frame ?
	 * @param key
	 * @return true if key just released, false else
	 */
	public boolean isKeyJustReleased(InputKeyType key)
	{
		return !isKeyPressed(key) && inputData.getDeltaKeyboardState().getKey(key);
	}
}
