package com.gl.passgen;

class PassGen {

	/**
	 * @param args
	 */
	private static String alfabet = "1234567890" +
			"ZXCVBNMASDFGHJKLQWERTYUIOP" +
			"zxcvbnmasdfghjklqwertyuiop";
	
	public static String generetedPass(String masterPass, String nameOrDomain, int lenghtOfPass){
		char[] pass = new char[lenghtOfPass];
		StringBuilder tempString = new StringBuilder(masterPass);
		tempString.append(nameOrDomain);
		char[] temp = tempString.reverse().toString().toCharArray();
		for(int i = 0; i < lenghtOfPass && temp.length > 0; i++)
		{
			pass[i] = alfabet.charAt(temp[i % temp.length] * (i + 5) % 62);
		}
		String password = new String(pass);
		return password;
	}
	
	

}
