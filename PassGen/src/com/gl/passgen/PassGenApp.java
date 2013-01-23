package com.gl.passgen;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class PassGenApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 JFrame.setDefaultLookAndFeelDecorated(true);
				 new GUIPassGen();
			}
		});
		
	}

}
