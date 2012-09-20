package cn.jinren.filter;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.dreamfield.dao.NetArticleDao;
import cn.dreamfield.model.NetArticle;
import cn.dreamfield.spiderable.SpiderableConst;
import cn.dreamfield.utils.HttpDownloadUtil;
import cn.dreamfield.utils.SpringUtil;
import cn.jinren.spider.Spiderable;
import cn.jinren.test.KK;

public class PaginationFilter implements StrFilter {
	
	private NetArticle netArticle;
	
	public PaginationFilter(NetArticle netArticle) {
		this.netArticle = netArticle;
	}

	@Override
	public String doFilter(String str) {
		String result = str;
		String pagination = "";
		Pattern p = Pattern.compile("<div align=\"center\" class=\"page_fenye\">.+?</div>");
		Matcher m = p.matcher(str);
		if(m.find()) {
			pagination = m.group();
			Pattern pa = Pattern.compile("<a id=\"after_this_page\" href=\"(.+?)\">��һҳ</a>");
			Matcher ma = pa.matcher(pagination);
			if(ma.find()) {
				String nextUrl = ma.group(1);
				KK.DEBUG("[START NEXT]: " + nextUrl);
				NetArticle originArticle = SpringUtil.ctx.getBean(NetArticleDao.class).getNetArticleEntity(nextUrl);
				if(null == originArticle) {
					NetArticle cArticle = new NetArticle();
					cArticle.setPid(netArticle.getId());	//��Ҫ �ݹ����Ҫ����
					cArticle.setOriginUrl(nextUrl); 		//item0 --- url
					cArticle.setIsExist("N"); 				//���»�û�б��ػ�����ʱ��ΪN
					cArticle.setOptDate(new Date());
					SpringUtil.ctx.getBean(NetArticleDao.class).saveNetArticle(cArticle);
					Spiderable contentSpiderable = null;
					//����xml�����ļ���ö�Ӧ��ContentSpiderable
					try {
						contentSpiderable = (Spiderable)Class.forName(SpiderableConst.CONTENT_SPIDERABLE_NAME).newInstance();
						contentSpiderable.setURL(nextUrl);
					} catch (Exception e) {
						e.printStackTrace();
					}
					HttpDownloadUtil httpDownloadUtils = SpringUtil.ctx.getBean(HttpDownloadUtil.class);
					//���������������� ---GameNewsContentSpiderable
					httpDownloadUtils.DownloadHtmlFromURL(contentSpiderable);
				} else if("N".equals(originArticle.getIsExist())) {
					Spiderable contentSpiderable = null;
					//����xml�����ļ���ö�Ӧ��ContentSpiderable
					try {
						contentSpiderable = (Spiderable)Class.forName(SpiderableConst.CONTENT_SPIDERABLE_NAME).newInstance();
						contentSpiderable.setURL(nextUrl);
					} catch (Exception e) {
						e.printStackTrace();
					}
					HttpDownloadUtil h = SpringUtil.ctx.getBean(HttpDownloadUtil.class);
					//���������������� ---GameNewsContentSpiderable
					h.DownloadHtmlFromURL(contentSpiderable);
				}
			}
			pa = Pattern.compile("class=\"currpage\"[\\D]+?([\\d]+?)[\\D]+?");
			ma = pa.matcher(pagination);
			if(ma.find()) {
				netArticle.setPageCorrent(Long.parseLong(ma.group(1)));
				KK.DEBUG("��ǰҳ", ma.group(1));
			}
			pa = Pattern.compile("��([\\d]+?)ҳ");
			ma = pa.matcher(pagination);
			if(ma.find()) {
				netArticle.setPageTotal(Long.parseLong(ma.group(1)));
				KK.DEBUG("��ҳ��", ma.group(1));
			}
		} else {
			netArticle.setPageCorrent(1l);
			netArticle.setPageTotal(1l);
		}
		result = result.replace(pagination, "");
		return result;
	}

}
