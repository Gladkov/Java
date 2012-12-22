package download.manager;

import java.awt.BorderLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DownloadManager extends JFrame implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @param args
	 */
	
	private TextField addTextField;
	private DownloadTableModel downloadTableModel;
	private JTable table;
	private JButton clearButton, resumeButton, pauseButton, cancelButton;
	private Download selectDownload;
	private boolean clearing;
	
	public DownloadManager(){
		
		setTitle("Download Manager");
		setSize(640, 640);
		JMenuBar menuBar = new JMenuBar();
		JMenu _fileMenu = new JMenu("File");
		JMenuItem _fileExitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
		_fileMenu.setMnemonic(KeyEvent.VK_F);
		_fileExitMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			exitAction();
				
			}
		});
		_fileMenu.add(_fileExitMenuItem);
		menuBar.add(_fileMenu);
		setJMenuBar(menuBar);
		JPanel addPanel = new JPanel();
		addTextField = new TextField(30);
		addPanel.add(addTextField);
		JButton addButton = new JButton("Add Download");
		addButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addAction();
				
			}
		});
		
		addPanel.add(addButton);
		
		downloadTableModel = new DownloadTableModel();
		table = new JTable(downloadTableModel);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				tableSelectionChandged();
				
			}
		});
		
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ProgressRenderer render = new ProgressRenderer(0, 100);
		render.setStringPainted(true);
		table.setDefaultRenderer(JProgressBar.class, render);
		
		table.setRowHeight((int) render.getPreferredSize().getHeight());
		
		JPanel downloadPanel = new JPanel();
		downloadPanel.setBorder(BorderFactory.createTitledBorder("Downloads"));
		downloadPanel.setLayout(new BorderLayout());
		downloadPanel.add(new JScrollPane(table), BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel();
		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pauseAction();
				
			}
		});
		
		resumeButton = new JButton("Resume");
		resumeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				resumeAction();
				
			}
		});
		
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				clearAction();
				
			}
		});
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelAction();
				
			}
		});
		pauseButton.setEnabled(false);
		resumeButton.setEnabled(false);
		clearButton.setEnabled(false);
		cancelButton.setEnabled(false);
		buttonsPanel.add(pauseButton);
		buttonsPanel.add(resumeButton);
		buttonsPanel.add(clearButton);
		buttonsPanel.add(cancelButton);
		
		
		
		
		
		
		
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(addPanel, BorderLayout.NORTH);
		getContentPane().add(buttonsPanel, BorderLayout.CENTER);
		getContentPane().add(downloadPanel, BorderLayout.SOUTH);
		
	}
	
	
	
	private void exitAction(){
		
		System.exit(0);
	}
	
	private void addAction(){
		
		URL verifieUrl = verifyURL(addTextField.getText());
		if(verifieUrl != null)
		{
			downloadTableModel.addDownload(new Download(verifieUrl));
			addTextField.setText("");
			
			
		}else
		{
			JOptionPane.showMessageDialog(this, "Invalid Download URL", "Error", JOptionPane.ERROR_MESSAGE);
			
		}
		
		
	}
	
	private void pauseAction(){
		selectDownload.pause();
		updateButtons();
	}
	
	private void resumeAction(){
		
		selectDownload.resume();
		updateButtons();
	}
	private void clearAction(){
		
		clearing = true;
		downloadTableModel.clearDownload(table.getSelectedRow());
		clearing = false;
		selectDownload = null;
		updateButtons();
	}
	
	private void cancelAction(){
		selectDownload.cancel();
		updateButtons();
		
		
	}
	
	private URL verifyURL(String url){
		
		if(!url.toLowerCase().startsWith("http://"))
			return null;
		URL verifiedUrl =null;
		try{
			verifiedUrl = new URL(url);
			
			
			
		}catch(Exception e){
			
			return null;
		}
		if(verifiedUrl.getFile().length() < 2)
			return null;
		
		return verifiedUrl;
		
		
	
		
		
	}
	
	private void updateButtons()
	{
		if(selectDownload != null)
		{
			int status = selectDownload.getStatus();
			switch(status)
			{
			case Download.DOWNLODING:
				pauseButton.setEnabled(true);
				resumeButton.setEnabled(false);
				cancelButton.setEnabled(true);
				clearButton.setEnabled(false);
				break;
				//System.out.println("download " + status);
			case Download.PAUSED:
				pauseButton.setEnabled(false);
				resumeButton.setEnabled(true);
				cancelButton.setEnabled(true);
				clearButton.setEnabled(false);
				break;
				//System.out.println("pase = " + status);
			case Download.ERROR:
				pauseButton.setEnabled(false);
				resumeButton.setEnabled(true);
				cancelButton.setEnabled(false);
				clearButton.setEnabled(true);
				//System.out.println("error = " + status);
				break;
			default:
				pauseButton.setEnabled(false);
				resumeButton.setEnabled(false);
				cancelButton.setEnabled(false);
				clearButton.setEnabled(true);
				//System.out.println("default = " + status);
			}
		} else{
			
			pauseButton.setEnabled(false);
			resumeButton.setEnabled(false);
			cancelButton.setEnabled(false);
			clearButton.setEnabled(false);
			
		}
		
		
		
	}
	
	private void tableSelectionChandged(){
		
		if(selectDownload != null)
			selectDownload.deleteObserver(DownloadManager.this);
		if(!clearing && table.getSelectedRow() > -1)
			selectDownload = downloadTableModel.getDownload(table.getSelectedRow());
		
		selectDownload.addObserver(DownloadManager.this);
		updateButtons();
		
	}
	@Override
	public void update(Observable o, Object arg) {
		
		if(selectDownload != null && selectDownload.equals(o))
			updateButtons();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				DownloadManager manager = new DownloadManager();
				manager.setVisible(true);
			}
		});

	}

	

}
