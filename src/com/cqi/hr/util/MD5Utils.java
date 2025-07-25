package com.cqi.hr.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Utils {
	public static String hex(byte[] array) {
	      StringBuffer sb = new StringBuffer();
	      for (int i = 0; i < array.length; ++i) {
	      sb.append(Integer.toHexString((array[i]
	          & 0xFF) | 0x100).substring(1,3));        
	      }
	      return sb.toString();
	  }
	  public static String md5Hex (String value) {
	      try {
	      MessageDigest md = MessageDigest.getInstance("MD5");
	      return hex (md.digest(value.getBytes("CP1252")));
	      } catch (NoSuchAlgorithmException e) {
	      } catch (UnsupportedEncodingException e) {
	      }
	      return null;
	  }	
}
