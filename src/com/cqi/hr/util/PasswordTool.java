package com.cqi.hr.util;

import java.util.Random;

public class PasswordTool {
	/**產生密碼亂數*/
	public static String getRandomPassword(){
		
	    StringBuilder newPW = new StringBuilder();
	    Random ran = new Random();
	    int randomNum = 0;
	    for (int i=0;i< 8;i++) {
	      randomNum = ran.nextInt(3);
	      switch(randomNum){
		  	case 0 :// 放數字		     
		      newPW.append((int) ((Math.random() * 10) + 48));
		      break;
		    case 1 :// 放大寫英文		     
		      newPW.append((char) (((Math.random() * 26) + 65)));
		      break;
		    case 2 :// 放小寫英文		      
		      newPW.append(((char) ((Math.random() * 26) + 97)));
		      break;	    	 
	      }	     
	    }
	    return newPW.toString();		
	}
}

