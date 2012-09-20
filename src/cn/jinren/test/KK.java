package cn.jinren.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.dreamfield.utils.UtilConst;

/**
 * SIMPLE LOG FOR TEST
 * @author KING
 * @date 2012.9.15
 */
public class KK {
	
	private static String LOG_FILE_ROOT = UtilConst.FILE_ROOT + "log/";
	
	public static void LOG(Object o) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String log = "[LOG " + format.format(date) + "]: " + o;
		System.out.println(log);
		LOG2FIEL(log, date);
	}
	
	private static void LOG2FIEL(String log, Date date) {
		File file = new File(LOG_FILE_ROOT);
		if(!file.exists()) {
			file.mkdirs();
		}
		SimpleDateFormat ymFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			FileOutputStream fos = new FileOutputStream(LOG_FILE_ROOT + ymFormat.format(date) + "_LOG.txt", true);
			log = log + "\r\n";
			fos.write(log.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void LOG(String para, Object o) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String log = "[LOG " + format.format(date) + "]: " + "{" + para + ":" + "{" + o + "}}";
		System.out.println(log);
		LOG2FIEL(log, date);
	}
	
	public static void LOG(String[] para, Object[] o) {
		String jsonstr = JSON(para, o);
		if("ERROR".equals(jsonstr)) {
			return;
		}
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String log = "[LOG " + format.format(date) + "]: " + jsonstr;
		System.out.println(log);
		LOG2FIEL(log, date);
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
