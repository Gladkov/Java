package diploma;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;

public class Logger
{
	PrintWriter _file;
	
	public Logger()
	{
		this._file = null;
	}
	
	public void startManagerLog()
	{
		this.startLog(Config.MANAGER_LOG_FILENAME);
	}
	
	public void startEmployeeLog()
	{
		this.startLog(Config.EMPLOYEE_LOG_FILENAME);
	}
	
	private void startLog(String filename)
	{
		try
		{
			FileWriter writer= new FileWriter(filename, true);
			_file = new PrintWriter(writer);
			_file.println("--------------------------------------------------------------------------------");
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "Failed to create log file");
		}		
	}
	
	public void logManagerRound(int round, Double proposal, Double percents, Double profit)
	{
		_file.println("Round: " + round + ". Manager propose: " + proposal + ". Client responds: " + percents + 
				". Profit: " + profit);
		_file.flush();
	}
	
	public void logEmployeeRound(int round, Double proposal, Double percents, Double profit)
	{
		_file.println("Round: " + round + ". Manager propose: " + proposal + ". Client responds: " + percents + 
				". Profit: " + profit);
		_file.flush();
	}
}