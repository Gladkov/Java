package diploma;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientHandler implements Runnable
{
	private enum Action
	{
		UNKNOWN,
		CONNECT,
		WAIT_FOR_PROPOSAL,
		SEND_RESPONSE,
	}
	
	Socket			_client;
	MainController	_controller;
	Thread			_thread;
	Action			_action;	
	String			_address;
	int				_port;
	Double			_percents;
	
	public ClientHandler(MainController controller)
	{
		_client = null;
		_controller = controller;
		_action = Action.UNKNOWN;
	}
	
	public void connect(String address, int port)
	{
		_address = address;
		_port = port;
		
		_action = Action.CONNECT;
		
		_thread = new Thread(this, "client_thread");
		_thread.start();
	}
	
	public void waitForProposal()
	{
		_action = Action.WAIT_FOR_PROPOSAL;
		
		_thread = new Thread(this, "client_thread");
		_thread.start();
	}
	
	public void sendResponse(Double percents)
	{
		_percents = percents;
		
		_action = Action.SEND_RESPONSE;		
		
		_thread = new Thread(this, "client_thread");
		_thread.start();
	}
	
	@Override
	public void run()
	{
		if (_action == Action.CONNECT)
		{
			try
			{
				_client = new Socket(_address, _port);		
				_controller.clientConnectedToServer();
			}
			catch (Exception e)
			{
				_controller.clientError("Error: " + e.getMessage());
			}
		}
		else if (_action == Action.WAIT_FOR_PROPOSAL && _client != null)
		{
			try
			{
				BufferedReader reader = new BufferedReader(new InputStreamReader(_client.getInputStream()));
				char chars[] = new char[1024];
								
				if (reader.read(chars, 0, 1000) != -1)
				{
					try
					{
						int length = 0;
						
						while (chars[length] != '\0') ++length;
						
						String strProposal = new String(chars, 0, length);						
						Double proposal = Double.parseDouble(new String(strProposal));
						_controller.clientReceivedProposal(proposal);
					}
					catch (Exception e)
					{
						_controller.clientError("Invalid proposal");
					}
				}
			}
			catch (IOException e)
			{
				_controller.clientError("Error: " + e.getMessage());
			}
		}
		else if (_action == Action.SEND_RESPONSE && _client != null)
		{
			try
			{
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(_client.getOutputStream()));			
				writer.write(new String("" + _percents));
				writer.flush();
				_controller.clientResponseSent();
			}
			catch (IOException e)
			{
				_controller.serverError("Error: " + e.getMessage());
			}
		}
	}	
}