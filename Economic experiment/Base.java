import javax.swing.JFrame;

import diploma.MainController;
import diploma.MainWindow;

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