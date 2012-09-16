package cn.jinren.test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SIMPLE LOG FOR TEST
 * @author KING
 * @date 2012.9.15
 */
public class KK {
	
	public static void LOG(Object o) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("[LOG " + format.format(date) + "]: " + o);
	}
	
	public static void LOG(String para, Object o) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("[LOG " + format.format(date) + "]: " + "{" + para + ":" + "{" + o + "}}");
	}
	
	public static void LOG(String[] para, Object[] o) {
		String jsonstr = JSON(para, o);
		if("ERROR".equals(jsonstr)) {
			return;
		}
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("[LOG " + format.format(date) + "]: " + jsonstr);
	}
	
	public static void INFO(Object o) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		System.out.println("[INFO " + format.format(date) + "]: " + o);
	}
	
	public static void INFO(String para, Object o) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		System.out.println("[INFO " + format.format(date) + "]: " + "{" + para + ":" + "{" + o + "}}");
	}
	
	public static void INFO(String[] para, Object[] o) {
		String jsonstr = JSON(para, o);
		if("ERROR".equals(jsonstr)) {
			return;
		}
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		System.out.println("[INFO " + format.format(date) + "]: " + jsonstr);
	}
	
	public static void DEBUG(Object o) {
		System.out.println("[DEBUG]: " + o);
	}
	
	public static void DEBUG(String para, Object o) {
		System.out.println("[DEBUG]: " + "{" + para + ":" + "{" + o + "}}");
	}
	
	public static void DEBUG(String[] para, Object[] o) {
		String jsonstr = JSON(para, o);
		if("ERROR".equals(jsonstr)) {
			return;
		}
		System.out.println("[DEBUG]: " + jsonstr);
	}
	
	public static void WARN(Object o) {
		System.out.println("[WARN]: " + o);
	}
	
	public static void ERROR(Object o) {
		System.out.println("[ERROR]: " + o);
	}
	
	public static String JSON(String[] para, Object[] o) {
		if(para.length != o.length) {
			ERROR("parameter length error");
			return "ERROR";
		}
		String jsonstr = "{";
		for(int i=0; i<para.length; i++) {
			if(i == (para.length - 1)) {
				jsonstr += para[i] + ":" + "{" + o[i] + "}}";
			} else {
				jsonstr += para[i] + ":" + "{" + o[i] + "}, ";
			}
		}
		return jsonstr;
	}

}
