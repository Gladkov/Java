package diploma;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import diploma.MainController.Role;
import diploma.MainController.State;

public class MainWindow extends JFrame implements ActionListener, DocumentListener
{
	MainController						_controller;
	JPanel								_parentPanel;
	Map<MainController.State, JPanel> 	_panels;
	JPanel								_prevPanel;
	JPanel								_controlPanel;
	
	JButton 			_next;
	JRadioButton		_roleManager, _roleEmployee;
	JComboBox			_formulas;
	JTextField			_managerAddress;
	JTextField			_managerProposal;
	JLabel				_managerRound;
	JLabel				_managerPreviousProposal;
	JLabel				_managerClientResponse;
	JLabel				_managerProfit;
	JTextField			_managerNextProposal;
	JLabel				_clientRound;
	JLabel				_proposalFromManager;	
	JTextField			_clientResponse;	
	JLabel				_clientProfit;
		
	public MainWindow()
	{
		this._controller = null;
		this._prevPanel = null;
		
		this.setTitle("Experiment");
		this.setMinimumSize(new Dimension(300, 50));
		this.setLocationRelativeTo(null);
		this.setResizable(false);		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.initializeGui();
		
		this.setVisible(true);
	}
	
	private void initializeGui()
	{
		JPanel panel;		
		ButtonGroup buttonGroup;
		
		_panels = new HashMap<MainController.State, JPanel>();
				
		// Select role panel
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Choose your destiny"));
		panel.setLayout(new GridLayout(2, 1));		
		buttonGroup = new ButtonGroup();		
		_roleManager = new JRadioButton(Role.MANAGER.getRoleDescription());
		_roleManager.setSelected(true);
		panel.add(_roleManager);
		buttonGroup.add(_roleManager);				
		_roleEmployee = new JRadioButton(Role.EMPLOYEE.getRoleDescription());
		_roleEmployee.setSelected(false);
		panel.add(_roleEmployee);
		buttonGroup.add(_roleEmployee);		
		_panels.put(State.WAITING_FOR_ROLE_SELECTION, panel);
		
		// Select formula panel
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Choose your formula"));
		panel.setLayout(new FlowLayout());
		_formulas = new JComboBox();
		panel.add(_formulas);
		_panels.put(State.WAITING_FOR_FORMULA_SELECTION, panel);
		
		// Waiting for client 
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Employee connection waiting"));
		panel.setLayout(new FlowLayout());
		panel.add(new JLabel("Press next to start waiting for client"));
		_panels.put(State.WAITING_FOR_EMPLOYEE, panel);
		
		// Manager address
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Input manager address"));
		panel.setLayout(new GridLayout(2, 1));
		panel.add(new JLabel("IP address or hostname of the manager:"));
		_managerAddress = new JTextField("localhost");
		panel.add(_managerAddress);
		_panels.put(State.WAITING_FOR_MANAGER_ADDRESS, panel);
		
		// Waiting for proposal
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Waiting for proposal"));
		panel.setLayout(new FlowLayout());
		panel.add(new JLabel("Please wait for proposal from manager"));
		_panels.put(State.WAITING_FOR_PROPOSAL, panel);
		
		// Waiting for response
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Waiting for response"));
		panel.setLayout(new FlowLayout());
		panel.add(new JLabel("Please wait for response from client"));
		_panels.put(State.WAITING_FOR_RESPONSE, panel);
		
		// Preparing initial proposal
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Preparing initial proposal"));
		panel.setLayout(new GridLayout(2, 1));
		panel.add(new JLabel("Input your proposal to employee:"));
		_managerProposal = new JTextField();
		panel.add(_managerProposal);
		_panels.put(State.PREPARING_INITIAL_PROPOSAL, panel);
		
		// Preparing next proposal
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Preparing next proposal"));
		panel.setLayout(new GridLayout(5, 1));
		_managerRound = new JLabel();
		panel.add(_managerRound);
		_managerPreviousProposal = new JLabel();
		panel.add(_managerPreviousProposal);
		_managerClientResponse = new JLabel();
		panel.add(_managerClientResponse);
		_managerProfit = new JLabel();
		panel.add(_managerProfit);
		_managerNextProposal = new JTextField();
		panel.add(_managerNextProposal);
		_panels.put(State.PREPARING_NEXT_PROPOSAL, panel);
		
		// Preparing response
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Preparing response"));
		panel.setLayout(new GridLayout(4, 1));
		_clientRound = new JLabel();
		panel.add(_clientRound);
		_proposalFromManager = new JLabel();
		panel.add(_proposalFromManager);
		_clientResponse = new JTextField();
		_clientResponse.getDocument().addDocumentListener(this);
		panel.add(_clientResponse);
		_clientProfit = new JLabel();
		panel.add(_clientProfit);
		_panels.put(State.PREPARING_RESPONSE, panel);
		
		// The end
		panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("The end"));
		panel.setLayout(new FlowLayout());
		panel.add(new JLabel("The game is finished. Press Next to exit"));
		_panels.put(State.THE_END, panel);
		
		// Control panel
		_controlPanel = new JPanel();
		_controlPanel.setLayout(new FlowLayout());
		_next = new JButton("Next >");
		_next.setActionCommand("command_next");
		_next.addActionListener(this);
		_controlPanel.add(_next);
		
		// Main 
		_parentPanel = new JPanel();
		_parentPanel.setLayout(new BorderLayout());
		_parentPanel.add(_controlPanel, BorderLayout.SOUTH);
		this.add(_parentPanel);		
	}
	
	public void setController(MainController controller)
	{
		this._controller = controller;
	}

	public void refreshContents()
	{
		if (this._controller == null)
			return;
			
		if (this._controller.getState() == State.UNKNOWN)
		{
			JOptionPane.showMessageDialog(this, "Controller in unknown state");
			return;
		}

		if (this._controller.getState() == State.WAITING_FOR_FORMULA_SELECTION)
		{
			_formulas.removeAll();

			// Load proper formulas list
			Formula formulas[];
			if (this._controller.getRole() == Role.MANAGER)
				formulas = Config.MANAGER_FORMULAS;
			else
				formulas = Config.EMPLOYEE_FORMULAS;
			
			for(Formula f: formulas)
			{
				_formulas.addItem(f);
			}
			
			_formulas.updateUI();
		}
		else if (_controller.getState() == State.PREPARING_NEXT_PROPOSAL)
		{
			_managerRound.setText("Current round: " + _controller.getRound());
			_managerPreviousProposal.setText("Your previous proposal: " + _controller.getManagerProposal());
			_managerClientResponse.setText("Response from client: " + _controller.getClientResponse());
			_managerProfit.setText("Your profit: " + _controller.getCurrentProfit());
		}
		else if (_controller.getState() == State.PREPARING_RESPONSE)
		{
			_clientRound.setText("Current round: " + _controller.getRound());
			_proposalFromManager.setText("Proposal from manager: " + _controller.getManagerProposal());
			_clientProfit.setText("");
		}
		
		this.switchToPanelByState(this._controller.getState());		
	}
	
	public void showError(String message)
	{
		JOptionPane.showMessageDialog(this, message);
	}
	
	public void showSuccess(String message)
	{
		JOptionPane.showMessageDialog(this, message);
	}
	
	public void setControlsState(Boolean state)
	{
		_next.setEnabled(state);
	}
	
	private void switchToPanelByState(State state) 
	{
		if (_prevPanel != null)
			_parentPanel.remove(_prevPanel);
		
		JPanel panel = _panels.get(state);		
		_parentPanel.add(panel, BorderLayout.CENTER);
		_prevPanel = panel;
		
		_parentPanel.revalidate();
		_parentPanel.repaint();
	
		this.pack();
	}
	
	private Role getSelectedRole()
	{
		if (_roleManager != null && _roleManager.isSelected())
			return Role.MANAGER;
		return Role.EMPLOYEE;
	}
	
	private void calculateEmployeeProfit()
	{
		if (_controller.getRole() == Role.EMPLOYEE && _controller.getState() == State.PREPARING_RESPONSE)
		{
			if (_clientResponse.getText().length() > 0)
			{
				try
				{
					Double percents = Config.parseEmployeeResponse(_clientResponse.getText());
					
					_clientProfit.setText("Your profit will be: " + 
							_controller.getFormula().evaluate(_controller.getManagerProposal(), percents));
				}
				catch (Exception e)
				{
					this.showError("Input valid number in range " + Config.EMPLOYEE_RESPONSE_MIN + "-" + Config.EMPLOYEE_RESPONSE_MAX);
				}
			}
			else
			{
				_clientProfit.setText("");
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent ae)
	{
		if (_controller == null)
			return;
		
		if (ae.getActionCommand().equals("command_next"))
		{
			if (_controller.getState() == State.WAITING_FOR_ROLE_SELECTION)
				_controller.setRole(this.getSelectedRole());
			
			if (_controller.getState() == State.WAITING_FOR_FORMULA_SELECTION)
				_controller.setFormula((Formula)this._formulas.getSelectedItem());
			
			if (_controller.getState() == State.WAITING_FOR_MANAGER_ADDRESS)
				_controller.setManagerAddress(this._managerAddress.getText());
			
			if (_controller.getState() == State.PREPARING_INITIAL_PROPOSAL ||
				_controller.getState() == State.PREPARING_NEXT_PROPOSAL)
			{
				try
				{
					Double p;
					
					if (_controller.getState() == State.PREPARING_INITIAL_PROPOSAL)
						p = Config.parseManagerProposal(_managerProposal.getText());
					else
						p = Config.parseManagerProposal(_managerNextProposal.getText());
					
					_controller.setManagerProposal(p);
				}
				catch (Exception e)
				{
					this.showError("Input valid number in range " + Config.MANAGER_PROPOSAL_MIN + "-" + Config.MANAGER_PROPOSAL_MAX);
					return;
				}
			}
			
			if (_controller.getState() == State.PREPARING_RESPONSE)
			{
				try
				{
					Double r = Config.parseEmployeeResponse(_clientResponse.getText());
										
					_controller.setClientResponse(r);
				}
				catch (Exception e)
				{
					this.showError("Input valid number in range " + Config.EMPLOYEE_RESPONSE_MIN + "-" + Config.EMPLOYEE_RESPONSE_MAX);
					return;
				}				
			}
			
			if (_controller.getState() == State.THE_END)
				System.exit(0);
							
			_controller.next();
		}
	}

	@Override
	public void changedUpdate(DocumentEvent arg0)
	{
		this.calculateEmployeeProfit();
	}

	@Override
	public void insertUpdate(DocumentEvent arg0)
	{
		this.calculateEmployeeProfit();	
	}

	@Override
	public void removeUpdate(DocumentEvent arg0)
	{
		this.calculateEmployeeProfit();
	}
}