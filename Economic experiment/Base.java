import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import diploma.*;

public class Base
{
	public static void main(String argc [])
	{
    	JFrame.setDefaultLookAndFeelDecorated(true);
		
		MainWindow window = new MainWindow();
		MainController controller = new MainController();
		
		controller.setView(window);
		window.setController(controller);
		
		controller.runNewTest();
	}
}