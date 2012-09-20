package cn.dreamfield.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.dreamfield.dao.NetArticleDao;
import cn.dreamfield.model.NetArticle;
import cn.jinren.test.KK;

@Component
public class GenerateListFileUtil {

	@Autowired
	private NetArticleDao netArticleDao;
	
	private static String LIST_FILE_ROOT = UtilConst.FILE_ROOT + "list/";
	private static String IMG_FILE_ROOT = UtilConst.FILE_ROOT + "image/";
	private static double IMG_SMALL_WIDTH = 136; //Àı¬‘ÕºøÌ
	private static double IMG_SMALL_HIGTH = 76;  //Àı¬‘Õº∏ﬂ
	
	public void sys() {
		KK.INFO(LIST_FILE_ROOT);
	}
	
	public GenerateListFileUtil() {
		File file = new File(LIST_FILE_ROOT);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
	
	public void generateList(String category) {
		List<NetArticle> list = netArticleDao.getNetArticleIn(category);
		String str = "";
		for(NetArticle na : list) {
			str += "<a href='../html/" + na.getHtmlUrl() + "'>" + na.getName() + "</a>&nbsp;&nbsp;";
			if(null == na.getImgUrl() || "null".equals(na.getImgUrl())) {
			} else {
				str += "<a href='../image/" + na.getImgUrl() + "'>Õº∆¨</a>&nbsp;&nbsp;";
				//…˙≥…Õº∆¨Àı¬‘Õº
				String sImgUrl = ImageCutUtil.ImageCut(IMG_FILE_ROOT + na.getImgUrl(), IMG_SMALL_WIDTH, IMG_SMALL_HIGTH);
				na.setImgUrlS(sImgUrl);
				netArticleDao.updateNetArticle(na);
			}
			str = getArticleChild(na.getId(), str);
			str += "<br>\r\n";
		}
		try {
			FileOutputStream fos = new FileOutputStream(LIST_FILE_ROOT + category + ".html");
			fos.write(str.getBytes());
			fos.close();
		} catch (IOException e) {
			KK.ERROR(e);
		}
	}
	
	private String getArticleChild(Long id, String str) {
		NetArticle childArticle = netArticleDao.getNetArticleChild(id);
		if(null != childArticle) {
			str += "<a href='../html/" + childArticle.getHtmlUrl() + "'>" + childArticle.getPageCorrent() + "</a>&nbsp;&nbsp;";
			KK.DEBUG("<a href='../html/" + childArticle.getHtmlUrl() + "'>" + childArticle.getPageCorrent() + "</a>&nbsp;&nbsp;");
			str = getArticleChild(childArticle.getId(), str);
		}
		return str;
	}
}
