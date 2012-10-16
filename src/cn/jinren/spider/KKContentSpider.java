package cn.jinren.spider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.dreamfield.model.NetInfo;
import cn.jinren.test.KK;

public class KKContentSpider {
	/**
	 * 根据Spiderable的URL和起始、结束标志获得截取内容
	 * @param spiderable
	 * @param decode
	 * @return
	 * @throws IOException 
	 */
	public static String getContentString(Spiderable spiderable, String decode) throws IOException {
		StringBuffer content = new StringBuffer();

		URL myurl = new URL(spiderable.getURL());
		HttpURLConnection httpurlconn = (HttpURLConnection)myurl.openConnection();
		int responsecode = httpurlconn.getResponseCode();
		if(responsecode == HttpURLConnection.HTTP_OK) {
			InputStream is = httpurlconn.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, decode));
	        String line = "";
	        Boolean copyFlag = false;
	        while((line = bufferedReader.readLine()) != null) {
	        	if(line.contains(spiderable.getStart())) {
	        		copyFlag = true;
	        	}
	        	if(line.contains(spiderable.getEnd())) {
	        		break;
	        	}
	        	if(copyFlag) {
	        		content.append(line + "\r\n");
	        	}
	        }
	        bufferedReader.close();
	        is.close();
	        httpurlconn.disconnect();
		} else {
			KK.ERROR(responsecode);
			throw new IOException();
		}
		String resCont = content.toString();
		if(resCont.trim().equals("")) {
			throw new IOException();
		}
//KK.INFO(content);
		return resCont;
	}
	
	/**
	 * 通过ListSpiderable获得网页的元素组
	 * @param spiderable
	 * @param elements
	 * @param decode
	 * @throws IOException 
	 */
	public static void getElementsFromWeb(ListSpiderable spiderable, ArrayList<Element> elements, String decode) throws IOException {
		String content = getContentString(spiderable, decode);
        Pattern p = Pattern.compile(spiderable.getElementPatt().replace("[KKSP]", "([\\w[\\W]]+?)"));
        Matcher m = p.matcher(content);//匹配Element
        while(m.find()) {
        	Boolean isSpidered = true;
        	Element element = new Element();
        	element.setCount(spiderable.getItemCount());
        	for(int i = 0; i < spiderable.getItemCount(); i++) {//取得Element中Item数目，并逐个匹配
        		Pattern pa = Pattern.compile(spiderable.getItemPatt(i).replace("[KKSP]", "([\\w[\\W]]+?)"));
            	Matcher ma = pa.matcher(m.group(1));
            	if(ma.find()) {
            		element.setItem(i, ma.group(1));
            		KK.DEBUG("-" + i + "-", ma.group(1));
            	} else {
            		if(i == 0) {//未匹配到第一个Item，则放弃整个Element
            			isSpidered = false;
            			break;
            		}
            	}
            	if(i == (spiderable.getItemCount() - 1)) {
            		KK.DEBUG("================================================");
            	}
        	}
        	if(isSpidered) {
        		elements.add(element);
        	}
        }
	}
	
	public static void getElementsFromWebx(ListSpiderable spiderable, ArrayList<NetInfo> elements, String decode) throws IOException {
		String content = getContentString(spiderable, decode);
        Pattern p = Pattern.compile(spiderable.getElementPatt().replace("[KKSP]", "([\\w[\\W]]+?)"));
        Matcher m = p.matcher(content);//匹配Element
        while(m.find()) {
        	Boolean isSpidered = true;
        	NetInfo netInfo = new NetInfo();
        	Pattern pa = Pattern.compile(spiderable.getItemPatt(0).replace("[KKSP]", "([\\w[\\W]]+?)"));
        	Matcher ma = pa.matcher(m.group(1));
        	if(ma.find()) {
        		netInfo.setInfoName(ma.group(1));
        		KK.DEBUG("-Name-", ma.group(1));
        	} else {
        		isSpidered = false;
        	}
        	
        	pa = Pattern.compile(spiderable.getItemPatt(1).replace("[KKSP]", "([\\w[\\W]]+?)"));
        	ma = pa.matcher(m.group(1));
        	if(ma.find()) {
        		String url = ma.group(1);
        		if(url.startsWith("/")) {
        			url = spiderable.getURL().substring(0, spiderable.getURL().indexOf("/", spiderable.getURL().indexOf("//") + 2)) + url;
        		}
        		netInfo.setInfoOriginUrl(url);
        		KK.DEBUG("-Url-", url);
        	}
        	
        	pa = Pattern.compile(spiderable.getItemPatt(2).replace("[KKSP]", "([\\w[\\W]]+?)"));
        	ma = pa.matcher(m.group(1));
        	if(ma.find()) {
        		String imgUrl = ma.group(1);
        		if(imgUrl.startsWith("/")) {
        			imgUrl = spiderable.getURL().substring(0, spiderable.getURL().indexOf("/", spiderable.getURL().indexOf("//") + 2)) + imgUrl;
        		}
        		netInfo.setInfoImgUrl(imgUrl);
        		KK.DEBUG("-ImgUrl-", imgUrl);
        	}
        	
        	pa = Pattern.compile(spiderable.getItemPatt(3).replace("[KKSP]", "([\\w[\\W]]+?)"));
        	ma = pa.matcher(m.group(1));
        	if(ma.find()) {
        		try {
					netInfo.setInfoDate(new SimpleDateFormat("yyyy-MM-dd").parse(ma.group(1)));
				} catch (ParseException e) {
					KK.ERROR(e);
				}
        		KK.DEBUG("-Date-", ma.group(1));
        	}
        	
        	pa = Pattern.compile(spiderable.getItemPatt(4).replace("[KKSP]", "([\\w[\\W]]+?)"));
        	ma = pa.matcher(m.group(1));
        	if(ma.find()) {
        		netInfo.setInfoIntro(ma.group(1));
        		KK.DEBUG("-Intro-", ma.group(1));
        	}
        	
        	if(isSpidered) {
        		elements.add(netInfo);
        	}
        }
	}
}
