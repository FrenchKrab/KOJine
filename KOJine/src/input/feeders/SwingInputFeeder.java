package input.feeders;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import input.InputFeeder;
import input.InputKeyType;
import input.KeyboardState;

public class SwingInputFeeder extends InputFeeder implements KeyListener {

	private static class SwingKeyConverter
	{
		private static HashMap<Integer, InputKeyType> convertTable;
		
		public static void initializeIfNeeded()
		{
			if(convertTable == null)
			{
				convertTable = new HashMap<Integer, InputKeyType>();
				convertTable.put(KeyEvent.VK_A, InputKeyType.KEY_A);
				convertTable.put(KeyEvent.VK_B, InputKeyType.KEY_B);
				convertTable.put(KeyEvent.VK_C, InputKeyType.KEY_C);
				convertTable.put(KeyEvent.VK_D, InputKeyType.KEY_D);
				convertTable.put(KeyEvent.VK_E, InputKeyType.KEY_E);
				convertTable.put(KeyEvent.VK_F, InputKeyType.KEY_F);
				convertTable.put(KeyEvent.VK_G, InputKeyType.KEY_G);
				convertTable.put(KeyEvent.VK_H, InputKeyType.KEY_H);
				convertTable.put(KeyEvent.VK_I, InputKeyType.KEY_I);
				convertTable.put(KeyEvent.VK_J, InputKeyType.KEY_J);
				convertTable.put(KeyEvent.VK_K, InputKeyType.KEY_K);
				convertTable.put(KeyEvent.VK_L, InputKeyType.KEY_L);
				convertTable.put(KeyEvent.VK_M, InputKeyType.KEY_M);
				convertTable.put(KeyEvent.VK_N, InputKeyType.KEY_N);
				convertTable.put(KeyEvent.VK_O, InputKeyType.KEY_O);
				convertTable.put(KeyEvent.VK_P, InputKeyType.KEY_P);
				convertTable.put(KeyEvent.VK_Q, InputKeyType.KEY_Q);
				convertTable.put(KeyEvent.VK_R, InputKeyType.KEY_R);
				convertTable.put(KeyEvent.VK_S, InputKeyType.KEY_S);
				convertTable.put(KeyEvent.VK_T, InputKeyType.KEY_T);
				convertTable.put(KeyEvent.VK_U, InputKeyType.KEY_U);
				convertTable.put(KeyEvent.VK_V, InputKeyType.KEY_V);
				convertTable.put(KeyEvent.VK_W, InputKeyType.KEY_W);
				convertTable.put(KeyEvent.VK_X, InputKeyType.KEY_X);
				convertTable.put(KeyEvent.VK_Y, InputKeyType.KEY_Y);
				convertTable.put(KeyEvent.VK_Z, InputKeyType.KEY_Z);
				convertTable.put(KeyEvent.VK_UP, InputKeyType.KEY_UP);
				convertTable.put(KeyEvent.VK_DOWN, InputKeyType.KEY_DOWN);
				convertTable.put(KeyEvent.VK_LEFT, InputKeyType.KEY_LEFT);
				convertTable.put(KeyEvent.VK_RIGHT, InputKeyType.KEY_RIGHT);
				convertTable.put(KeyEvent.VK_ENTER, InputKeyType.KEY_ENTER);
				convertTable.put(KeyEvent.VK_BACK_SPACE, InputKeyType.KEY_BACKSPACE);
				convertTable.put(KeyEvent.VK_ESCAPE, InputKeyType.KEY_ESCAPE);
				convertTable.put(KeyEvent.VK_CONTROL, InputKeyType.KEY_LEFTCTRL);
				convertTable.put(KeyEvent.VK_CONTROL, InputKeyType.KEY_RIGHTCTRL);
				convertTable.put(KeyEvent.VK_ALT, InputKeyType.KEY_ALT);
				convertTable.put(KeyEvent.VK_SPACE, InputKeyType.KEY_SPACE);
			}
		}
		
		public static InputKeyType convertKeyEvent(int keyEvent)
		{
			return convertTable.getOrDefault(keyEvent, InputKeyType.KEY_NONE);
		}
	}
	
	
	
	public SwingInputFeeder()
	{
		super();
		SwingKeyConverter.initializeIfNeeded();
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		this.trySettingKey(SwingKeyConverter.convertKeyEvent(e.getKeyCode()), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.trySettingKey(SwingKeyConverter.convertKeyEvent(e.getKeyCode()), false);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub	
	}
	
}
