package com.cqi.hr.tool;

public class VerifyCode{
	
	public static String[] numberAndLetter = { "0", "1", "2", "3", "4", "5", "6", "7", 
	     "8", "9"};
	/*, "A", "B","C", "D", "E", "F", "G", "H", "I", "J", "K", "L", 
    "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"*/
	
	public static String getVerifyCode(int verifyCodeLength){
		//Random random = new Random();
		StringBuilder verifyCode = new StringBuilder("");
		for(int i=0;i<verifyCodeLength;i++){
			verifyCode.append(numberAndLetter[(int)(Math.random() * 10)]);
		}
		return verifyCode.toString();
	}
}