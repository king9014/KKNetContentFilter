package cn.dreamfield.conf;

import java.util.ArrayList;
import java.util.HashMap;

public class KKConf {
	public static String FILE_ROOT = "c:/kdownload/";
	public static HashMap<String, Boolean> IS_IMAGE_DOWNLOAD = new HashMap<String, Boolean>();
	public static HashMap<String, Boolean> CHANGE_IMAGE_URL = new HashMap<String, Boolean>();
	public static double IMG_SMALL_WIDTH = 122; //ËõÂÔÍ¼¿í¶È
	public static double IMG_SMALL_HIGTH = 110; //ËõÂÔÍ¼¸ß¶È
	
	public static ArrayList<WebsiteConf> websiteConfs = new ArrayList<WebsiteConf>();
}
