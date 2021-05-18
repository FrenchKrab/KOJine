import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 *	This is a test class, used for debug purposes
 *	no longer used.
 */
public class TestGUI extends JFrame {
	private final int BUFFER_COUNT = 2;
	public BufferStrategy bufferStrategy;
	
    final int FRAME_HEIGHT = 600;
    final int FRAME_WIDTH = 600;


    public TestGUI() {
        // build and display your GUI
        super("Game");

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem startMenuItem = new JMenuItem("Pause");
        menuBar.add(fileMenu);
        fileMenu.add(startMenuItem);


        //super.add(canvas);
        //setUndecorated (true);
        super.setVisible(true);
        super.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setJMenuBar(menuBar);
    }
	
    /*
	@Override
	public void paint(Graphics g)
	{
		if(bufferStrategy == null)
		{
			this.createBufferStrategy(BUFFER_COUNT);
			bufferStrategy = this.getBufferStrategy();
		}
		
		for(int i = 0; i < BUFFER_COUNT; i++)
		{
			Graphics2D bufferedGraphics = (Graphics2D) bufferStrategy.getDrawGraphics();
			bufferedGraphics.setColor(new Color(127,255,0));
			bufferedGraphics.fillRect(50, 10, 20, 30);
			bufferStrategy.show();
			bufferedGraphics.dispose();
		}
	}*/
	
}
