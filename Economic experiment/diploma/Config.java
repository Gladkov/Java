package diploma;

/***************************************************************************
 * Manager formulas
 ***************************************************************************/
 class ManagerFormula1 implements Formula
{
	public String toString()
	{
		return "(120 - W) * e  ";
	}
	
	public Double evaluate(Double proposal, Double percents)
	{
		return ((120 - (20 + proposal * percents)) * percents);
	}
}
 
 
 
class ManagerFormula2 implements Formula
{
	public String toString()
	{
		return "(120 - W) * e + p * (b * e) - p * (20 + M) ";
	}
	
	public Double evaluate(Double proposal, Double percents)
	{
		return ((120 - (20 + proposal * percents)) * percents + 0.3 *proposal * percents - 0.3 * (20 + 3 * 20));
	}
}

class ManagerFormula3 implements Formula
{
	public String toString()
	{
		return "(120 - W) * e - PR - p * (20 + 1/2 * M) ";
	}
	
	public Double evaluate(Double proposal, Double percents)
	{
		return ((120 - 20 + proposal * percents) * percents - 0.3 * 0.5 * 20  - 0.3 * (20 + 0.5 * 3 * 20));
	}
}

/***************************************************************************
 * Employee formulas
 ***************************************************************************/
 class EmployeeFormula1 implements Formula
{
	public String toString()
	{
		return " W - C(e)";
	}
	
	public Double evaluate(Double proposal, Double percents)
	{
		return ((20 + proposal * percents) - (20 * percents * percents - percents));
	}
}
 
class EmployeeFormula2 implements Formula
{
	public String toString()
	{
		return " W - C(e) - p * (b * e)";
	}
	
	public Double evaluate(Double proposal, Double percents)
	{
		return ((20 + proposal * percents) - (20 * percents * percents - percents) - 0.3 * proposal * percents);
	}
}

class EmployeeFormula3 implements Formula
{
	public String toString()
	{
		return " W - C(e) + PR - p * (1/2 * M)";
	}
	
	public Double evaluate(Double proposal, Double percents)
	{
		return ((20 + proposal * percents) - (20 * percents * percents - percents) + 0.3 * 0.5 * 20 - 0.3 * (0.5 * 3 * 20));
	}
}

public class Config
{
	public static final Formula[] MANAGER_FORMULAS = 
		{
			new ManagerFormula1(),
			new ManagerFormula2(),
			new ManagerFormula3()
		};
	
	public static final Formula[] EMPLOYEE_FORMULAS = 
		{
			new EmployeeFormula1(),
			new EmployeeFormula2(),
			new EmployeeFormula3()
		};
	
	public static final int PORT = 4444;
	
	public static final int NUMBER_OF_ROUNDS = 10;
	
	public static final String MANAGER_LOG_FILENAME = "manager.txt";
	
	public static final String EMPLOYEE_LOG_FILENAME = "client.txt";
	
	public static final Double MANAGER_PROPOSAL_MIN = 0.0;	
	public static final Double MANAGER_PROPOSAL_MAX = 100.0;
	
	public static final Double EMPLOYEE_RESPONSE_MIN = 0.0;
	public static final Double EMPLOYEE_RESPONSE_MAX = 1.0;
	
	
	public static final Double parseManagerProposal(String s) throws Exception
	{
		Double result = Double.parseDouble(s);
		
		if (result < MANAGER_PROPOSAL_MIN || result > MANAGER_PROPOSAL_MAX)
			throw new Exception();
		
		return result;
	}
	
	public static final Double parseEmployeeResponse(String s) throws Exception
	{
		Double result = Double.parseDouble(s);
		
		if (result < EMPLOYEE_RESPONSE_MIN || result > EMPLOYEE_RESPONSE_MAX)
			throw new Exception();
		
		return result;
	}
}

