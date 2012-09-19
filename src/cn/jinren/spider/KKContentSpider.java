package cn.jinren.spider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jinren.test.KK;

public class KKContentSpider {
	/**
	 * ����Spiderable��URL����ʼ��������־��ý�ȡ����
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
	 * ͨ��ListSpiderable�����ҳ��Ԫ����
	 * @param spiderable
	 * @param elements
	 * @param decode
	 * @throws IOException 
	 */
	public static void getElementsFromWeb(ListSpiderable spiderable, ArrayList<Element> elements, String decode) throws IOException {
		String content = getContentString(spiderable, decode);
        Pattern p = Pattern.compile(spiderable.getElementPatt());
        Matcher m = p.matcher(content);//ƥ��Element
        while(m.find()) {
        	Boolean isSpidered = true;
        	Element element = new Element();
        	element.setCount(spiderable.getItemCount());
        	for(int i = 0; i < spiderable.getItemCount(); i++) {//ȡ��Element��Item��Ŀ�������ƥ��
        		Pattern pa = Pattern.compile(spiderable.getItemPatt(i));
            	Matcher ma = pa.matcher(m.group(1));
            	if(ma.find()) {
            		element.setItem(i, ma.group(1));
            		KK.DEBUG("-" + i + "-", ma.group(1));
            	} else {
            		if(i == 0) {//δƥ�䵽��һ��Item�����������Element
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
}
