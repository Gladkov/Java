package com.gl.passgen;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class GUIPassGen extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 */
	private JPasswordField masterPass;
	private JTextField nameOrDomain;
	private JTextField password;
	
	private JButton generateButton;
	private JButton copyButton;
	
	private JPanel textPanel;
	private JPanel buttonPanel;
	
	private JComboBox<Integer> sizeComboBox;
	
	GUIPassGen(){
		JFrame window = new JFrame();
		
		masterPass = new JPasswordField(20);
		nameOrDomain = new JTextField(20);
		password = new JTextField(30);
		
		window.setTitle("Password Generator");
		window.setLocationRelativeTo(null);
		window.setLayout(new FlowLayout());
		
		textPanel = new JPanel(new GridLayout(6,1));
		textPanel.add(new JLabel("Master Password"));
		textPanel.add(masterPass);
		textPanel.add(new JLabel("Name or Domain"));
		textPanel.add(nameOrDomain);
		textPanel.add(new JLabel("Password"));
		textPanel.add(password);
		
		generateButton = new JButton("Generate");
		generateButton.addActionListener(new GenerateAction());
		
		copyButton = new JButton("Copy");
		copyButton.addActionListener(new CopyAction());
		
		Integer[] items = new Integer[27];
		for(int i = 4; i < 31; i++)
			items[i-4] = i;
		
		
		sizeComboBox = new JComboBox<Integer>(items);
		
		buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(generateButton);
		buttonPanel.add(copyButton);
		buttonPanel.add(new JLabel("Size:"));
		buttonPanel.add(sizeComboBox);
		
		
		
		window.add(textPanel);
		window.add(buttonPanel);
	
		 
		window.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		window.setSize(400, 200);
		window.setResizable(false);
		window.setVisible(true);
		
		
		
		
	}
	
	class GenerateAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			String pass = PassGen.generetedPass(new String(masterPass.getPassword()), nameOrDomain.getText(), (int)sizeComboBox.getSelectedItem());
			password.setText(pass);
			password.setFocusable(true);
		}	
	}
	class CopyAction implements ActionListener{
		
		private void pasteToClipBoard() {
		    Toolkit toolkit = Toolkit.getDefaultToolkit();
		    Clipboard clipboard = toolkit.getSystemClipboard();
		    StringSelection selection = new StringSelection(password.getText());
		    clipboard.setContents(selection, null);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub			
			pasteToClipBoard();
		
		}	
	}
	

}
