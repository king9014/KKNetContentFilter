package cn.dreamfield.conf;

import java.util.ArrayList;
import java.util.HashMap;

public class KKConf {
	public static String FILE_ROOT = "c:/kdownload/";
	public static HashMap<String, Boolean> IS_IMAGE_DOWNLOAD = new HashMap<String, Boolean>();
	public static HashMap<String, Boolean> CHANGE_IMAGE_URL = new HashMap<String, Boolean>();
	public static double IMG_SMALL_WIDTH = 122; //Àı¬‘ÕºøÌ∂»
	public static double IMG_SMALL_HIGTH = 110; //Àı¬‘Õº∏ﬂ∂»
	
	public static ArrayList<WebsiteConf> websiteConfs = new ArrayList<WebsiteConf>();
	
	public static String HTML_UPLOAD_URL = "http://dreamfield.cn/lib/uploadhtml.php";
	public static String IMG_UPLOAD_URL = "http://dreamfield.cn/lib/uploadimg.php";
	public static String SEARCH_INFO_EXIST_URL = "http://dreamfield.cn/lib/infoexist.php?originUrl=";
	public static String INSERT_INFO_URL = "http://dreamfield.cn/lib/insertinfo.php";
}
