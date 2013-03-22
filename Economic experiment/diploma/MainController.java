package diploma;

import java.util.ArrayList;


public class MainController
{
	public enum State
	{
		UNKNOWN,
		WAITING_FOR_ROLE_SELECTION,
		WAITING_FOR_FORMULA_SELECTION,
		WAITING_FOR_EMPLOYEE,
		WAITING_FOR_MANAGER_ADDRESS,
		WAITING_FOR_PROPOSAL,
		PREPARING_INITIAL_PROPOSAL,
		WAITING_FOR_RESPONSE,
		PREPARING_NEXT_PROPOSAL,
		PREPARING_RESPONSE,
		THE_END
	}
	
	public enum Role
	{
		MANAGER		(0, "Owner"),
		EMPLOYEE	(1, "Manager");
		
		private String 	_strRole;
		@SuppressWarnings("unused")
		private int		_id;
		
		Role(int id, String description)
		{
			_id = id;
			_strRole = description;
		}
		
		public String getRoleDescription() { return _strRole; }
	}
	
	// ----------------------------------------------------------------------
	private State 			_state;
	private MainWindow		_view;
	private Role			_role;
	private Formula			_formula;	
	private String			_managerAddress;
	private ServerHandler	_serverHandler;
	private ClientHandler	_clientHandler;	
	private int				_currentRound;
	private Double			_managerProposal;	
	private Double 			_clientResponse;
	private Logger			_logger;
	public ArrayList<ManageInfo> managerInfo = new ArrayList<ManageInfo>();
	public ArrayList<EmployerInfo> employerInfo = new ArrayList<EmployerInfo>();
	// ----------------------------------------------------------------------	
	public MainController()
	{
		this._state = MainController.State.UNKNOWN;
		this._view = null;
		this._role = Role.MANAGER;
		this._formula = null;
		this._managerAddress = "";
		this._serverHandler = null;
		this._managerProposal = 0.0;
		this._clientResponse = 0.0;
		this._currentRound = 0;
		this._logger = new Logger();
	}
	
	public void setView(MainWindow view)
	{
		this._view = view;
	}
	
	public State getState()
	{
		return this._state;
	}
	
	public void runNewTest()
	{
		this.setState(State.WAITING_FOR_ROLE_SELECTION);		
	}
	
	public void setRole(Role role)
	{
		this._role = role;
	}
	
	public Role getRole()
	{
		return this._role;
	}
	
	public void setFormula(Formula f)
	{
		this._formula = f;
	}
	
	public Formula getFormula()
	{
		return this._formula;
	}
	
	public void setManagerAddress(String address)
	{
		this._managerAddress = address;
	}
	
	public void setManagerProposal(Double proposal)
	{
		this._managerProposal = proposal;
	}
	
	public void setClientResponse(Double response)
	{
		this._clientResponse = response;
	}
	
	public Double getManagerProposal()
	{
		return this._managerProposal;
	}
	
	public Double getClientResponse()
	{
		return this._clientResponse;
	}
	
	public int getRound()
	{
		return this._currentRound;
	}
	
	public Double getCurrentProfit()
	{
		return this._formula.evaluate(this._managerProposal, this._clientResponse);
	}
	
	private void setState(State state)
	{
		if (_state != state)
		{
			_state = state;
			
			if (_view != null)
				_view.refreshContents();
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	public void next()
	{
		if (this._view == null)
			return;
			
		switch (this._state)
		{
			case WAITING_FOR_ROLE_SELECTION:
					this.setState(State.WAITING_FOR_FORMULA_SELECTION);					
				break;
				
			case WAITING_FOR_FORMULA_SELECTION:
				if (this._formula != null)
				{
					if (this._role == Role.MANAGER)
					{
						this.setState(State.WAITING_FOR_EMPLOYEE);
						_logger.startManagerLog();
					}
					else
					{
						this.setState(State.WAITING_FOR_MANAGER_ADDRESS);
						_logger.startEmployeeLog();
					}
				}
				break;
				
			case WAITING_FOR_EMPLOYEE:
				this._view.setControlsState(false);
				_serverHandler = new ServerHandler(this, Config.PORT);
				break;
				
			case PREPARING_INITIAL_PROPOSAL:
			case PREPARING_NEXT_PROPOSAL:
				this._view.setControlsState(false);
				_serverHandler.sendProposalToClient(this._managerProposal);
				break;

			case WAITING_FOR_MANAGER_ADDRESS:
				this._view.setControlsState(false);
				_clientHandler = new ClientHandler(this);
				_clientHandler.connect(_managerAddress, Config.PORT);
				break;
				
			case PREPARING_RESPONSE:
				this._view.setControlsState(false);
				_clientHandler.sendResponse(_clientResponse);
				break;
		}
	}

	public void serverError(String message)
	{
		if (this._view != null)
		{
			this._view.showError(message);		
			this._view.setControlsState(true);
		}
	}
	
	public void clientError(String message)
	{
		if (this._view != null)
		{
			this._view.showError(message);		
			this._view.setControlsState(true);
		}
	}
	
	public void serverClientConnected()
	{
		this._currentRound = 1; // Start from first round
		this.setState(State.PREPARING_INITIAL_PROPOSAL);
		
		if (this._view != null)
		{
			this._view.setControlsState(true);
		}		
	}
	
	public void serverProposalSent()
	{
		this.setState(State.WAITING_FOR_RESPONSE);
		
		_serverHandler.waitForResponse();
	}
	
	public void serverClientResponded(Double percents)
	{
		this._clientResponse = percents;
				
		_logger.logManagerRound(this._currentRound, this._managerProposal, this._clientResponse, this.getCurrentProfit());
		ManageInfo mi = new ManageInfo(this._currentRound, this._managerProposal, this._clientResponse, this.getCurrentProfit());
		
		if(ManageInfo.contains(managerInfo, mi))
		{	
			
			this.setState(State.THE_END);
			
			if (this._view != null)
			{
				this._view.setControlsState(true);
				this._view.showSuccess("Success!" + "\n"+ "Your profit: " + this.getCurrentProfit());				
			}
			return;
		}
		managerInfo.add(mi);
		
		this._currentRound++;
		
		if (this._currentRound > Config.NUMBER_OF_ROUNDS)
			this.setState(State.THE_END);				
		else
			this.setState(State.PREPARING_NEXT_PROPOSAL);
			
		if (this._view != null)
		{
			this._view.setControlsState(true);
		}
	}

	public void clientConnectedToServer()
	{
		this._currentRound = 1; // Start from first round
		this.setState(State.WAITING_FOR_PROPOSAL);

		_clientHandler.waitForProposal();
	}
	
	public void clientReceivedProposal(Double proposal)
	{
		this._managerProposal = proposal;
		this.setState(State.PREPARING_RESPONSE);
		
		if (this._view != null)
		{
			this._view.setControlsState(true);
		}
	}
	
	public void clientResponseSent()
	{
		_logger.logEmployeeRound(this._currentRound, this._managerProposal, this._clientResponse, this.getCurrentProfit());
		EmployerInfo ei = new EmployerInfo(this._currentRound, this._managerProposal, this._clientResponse, this.getCurrentProfit());
		
		if(EmployerInfo.contains(employerInfo, ei))
		{
			this.setState(State.THE_END);
			
			if (this._view != null)
			{
				this._view.setControlsState(true);
				this._view.showSuccess("Success!" + "\n"+ "Your profit: " + this.getCurrentProfit());				
			}
			return;
		}
		
		employerInfo.add(ei);
		this._currentRound++;
				
		if (this._currentRound > Config.NUMBER_OF_ROUNDS)
		{
			this.setState(State.THE_END);
			
			if (this._view != null)
			{
				this._view.setControlsState(true);
			}
		}
		else
		{
			this.setState(State.WAITING_FOR_PROPOSAL);
			
			if (this._view != null)
			{
				this._view.setControlsState(true);
			}
			
			_clientHandler.waitForProposal();
		}
	}
}



