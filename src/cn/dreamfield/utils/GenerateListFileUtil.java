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
	private static double IMG_SMALL_WIDTH = 122; //Àı¬‘ÕºøÌ
	private static double IMG_SMALL_HIGTH = 110; //Àı¬‘Õº∏ﬂ
	
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
		String str = "<table width=\"600\" border=\"0\" align=\"center\">";
		for(NetArticle na : list) {
			str += "<tr><td colspan=\"2\"><a href='../html/" + na.getHtmlUrl() + "'>" + na.getName() + "</a></td></tr>";
			str += "<tr><td width=\"442\">" + na.getIntro() + "</td>";
			if(null == na.getImgUrl() || "null".equals(na.getImgUrl())) {
				str += "<td width=\"142\" rowspan=\"2\">&nbsp;</td></tr>";
			} else {
				//…˙≥…Õº∆¨Àı¬‘Õº
				String sImgUrl = ImageCutUtil.ImageCut(IMG_FILE_ROOT + na.getImgUrl(), IMG_SMALL_WIDTH, IMG_SMALL_HIGTH);
				na.setImgUrlS(sImgUrl);
				netArticleDao.updateNetArticle(na);
				str += "<td width=\"142\" rowspan=\"2\"><img src='../ico/" + na.getImgUrlS() + "'/></td></tr>";
			}
			str += "<tr><td width=\"442\"><a href='../html/" + na.getHtmlUrl() + "'>" + na.getPageCorrent() + "</a>&nbsp;";
			str = getArticleChild(na.getId(), str);
		}
		str += "</td></tr></table>";
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
			str += "<a href='../html/" + childArticle.getHtmlUrl() + "'>" + childArticle.getPageCorrent() + "</a>&nbsp;";
			if(childArticle.getPageCorrent() % 20 == 0) {
				str += "<br>";
			}
			KK.DEBUG("<a href='../html/" + childArticle.getHtmlUrl() + "'>" + childArticle.getPageCorrent() + "</a>&nbsp;");
			str = getArticleChild(childArticle.getId(), str);
		}
		return str;
	}
}
