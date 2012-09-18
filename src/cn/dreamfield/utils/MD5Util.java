package cn.dreamfield.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	/**
     * 16位MD5加密
     * @param plainText
     * @return
     */
    public static String MD5Encode(String plainText) {
    	MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
    	md.update(plainText.getBytes());
	    byte b[] = md.digest();
	    int i;
	    StringBuffer buf = new StringBuffer("");
	    for (int offset = 0; offset < b.length; offset++) {
	    	i = b[offset];
	    	if (i < 0)
	    		i += 256;
	    	if (i < 16)
	    		buf.append("0");
	    	buf.append(Integer.toHexString(i));
	    }
	    //System.out.println("result: " + buf.toString().substring(8, 24));// 16位的加密
    	return buf.toString().substring(8, 24);
    }
}
