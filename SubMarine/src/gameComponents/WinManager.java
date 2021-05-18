package gameComponents;

import core.Component;
import core.components.TextRenderer;
import input.InputKeyType;
import main.GameConstants;
import rendering.FontOptions;

/**
 * Gère les conditions de victoire et défaite et affiche le texte correspondant à l'écran
 * @author Kraby
 *
 */
public class WinManager extends Component {

	public WinManager()
	{
		
	}
	public WinManager(float time)
	{
		this.timeLeft = time;
	}
	
	
	private float timeLeft = 60;
	private int tradingBoatLeft = 0;
	
	private int win = GameConstants.WORLDRETURN_WIN;
	private boolean gameEnd = false;
	
	private TextRenderer uiText;
	
	@Override
	public void start() {
		// TODO Auto-generated method stub
		super.start();
		uiText = new TextRenderer("Test", new FontOptions("Verdana", 0, 28), true);
		getOwner().addComponent(uiText);
	}
	
	
	@Override
	public void update(double delta) {
		// TODO Auto-generated method stub
		super.update(delta);
		
		if(!gameEnd)
		{
			timeLeft -= delta;
			
			uiText.text = "Time left: " + (int)timeLeft;
			if(timeLeft<0)
			{
				win = GameConstants.WORLDRETURN_LOSE;
				gameEnd = true;
			}
		}
		else
		{
			if(win == GameConstants.WORLDRETURN_WIN)
				uiText.text = "LEVEL CLEARED - Press ENTER to continue";
			else
				uiText.text = "GAME OVER - Press ENTER to exit";
			
			if(getOwner().getWorld().getInputManager().isKeyJustPressed(InputKeyType.KEY_ENTER))
				this.getOwner().getWorld().endWorld(win);
		}
	}
	
	
	public void incrementBoatCount()
	{
		tradingBoatLeft++;
	}
	
	public void decrementBoatCount()
	{

		tradingBoatLeft--;
		checkForVictory();
	}
	
	public int getBoatLeft()
	{
		return tradingBoatLeft;
	}

	public void setLost()
	{
		win = GameConstants.WORLDRETURN_LOSE;
		gameEnd = true;
	}

	private void checkForVictory()
	{
		if(tradingBoatLeft == 0)
		{
			win = GameConstants.WORLDRETURN_WIN;
			gameEnd = true;
		}
	}
}
