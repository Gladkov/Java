package diploma;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerHandler implements Runnable
{
	private enum Action
	{
		UNKNOWN,
		WAIT_FOR_CLIENT,
		SEND_PROPOSAL,
		WAIT_FOR_RESPONSE
	}
	
	MainController	_controller;
	ServerSocket 	_serverSocket;
	Socket			_client;
	Thread			_thread;
	Double 			_proposal;
	Action			_action;
	
	public ServerHandler(MainController controller, int port)
	{
		_controller = controller;
		_client = null;
		_serverSocket = null;
		_proposal = 0.0;
		_action = Action.WAIT_FOR_CLIENT;
		
		try
		{
			_serverSocket = new ServerSocket(port);
			_thread = new Thread(this, "server_thread");
			_thread.start();
		}
		catch (IOException e)
		{
			_controller.serverError("Error: " + e.getMessage());
		}
	}
	
	public void sendProposalToClient(Double proposal)
	{
		_proposal = proposal;		
		
		_action = Action.SEND_PROPOSAL;
		
		_thread = new Thread(this, "server_thread");
		_thread.start();		
	}
	
	public void waitForResponse()
	{
		_action = Action.WAIT_FOR_RESPONSE;
		
		_thread = new Thread(this, "server_thread");
		_thread.start();
	}
	
	@Override
	public void run()
	{
		if (_action == Action.WAIT_FOR_CLIENT)
		{
			try
			{
				_client = _serverSocket.accept();
				_controller.serverClientConnected();
			}
			catch (IOException e)
			{
				_controller.serverError("Error: " + e.getMessage());
				_client = null;
			}
		}
		else if (_action == Action.SEND_PROPOSAL && _client != null)
		{
			try
			{				
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(_client.getOutputStream()));
				writer.write(_proposal.toString());
				writer.flush();
				_controller.serverProposalSent();
			}
			catch (IOException e)
			{
				_controller.serverError("Error: " + e.getMessage());
			}
		}
		else if (_action == Action.WAIT_FOR_RESPONSE && _client != null)
		{
			try
			{
				char chars[] = new char[1024];
				BufferedReader reader = new BufferedReader(new InputStreamReader(_client.getInputStream()));
				
				if (reader.read(chars, 0, 1000) != -1)
				{
					Double percents = 0.0;
					
					try
					{
						int length = 0;
						
						while (chars[length] != '\0') ++length;
						
						percents = Config.parseEmployeeResponse(new String(chars, 0, length));
						if (percents < 0 || percents > 100)
							_controller.serverError("Error: response from client is out of range");	
						else
							_controller.serverClientResponded(percents);
					}
					catch (Exception e)
					{
						_controller.serverError("Error: invalid response from client");	
					}
				}
				else
				{
					_controller.serverError("Error: failed to get response from client");
				}
			}
			catch (IOException e)
			{
				_controller.serverError("Error: " + e.getMessage());
			}
		}
			
	}
	
}