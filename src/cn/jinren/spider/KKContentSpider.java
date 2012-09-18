package cn.jinren.spider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
//KK.INFO(content);
		return content.toString();
	}
}
