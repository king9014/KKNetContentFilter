package cn.dreamfield.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.jinren.test.KK;

public class HttpUploadUtil {
	
	public static String postUpload(File file, String uploadUrl) throws IOException {
		URL url = new URL(uploadUrl);
		String BOUNDARY = "---------7d4a6d158c9"; // 定义数据分隔线
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
        connection.setRequestProperty("Charsert", "UTF-8");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());
        byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes(); // 定义最后数据分隔线
        
        StringBuilder sb = new StringBuilder();
        sb.append("--");
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] data = sb.toString().getBytes();
        out.write(data);
        
        //读取文件上传到办事器
        FileInputStream fileInputStream = new FileInputStream(file);

        byte[] bytes = new byte[1024];
        while(fileInputStream.read(bytes, 0, 1024) > 0)
        {
            out.write(bytes, 0, bytes.length);
        }
        out.write(end_data);
        out.flush();
        out.close();
        fileInputStream.close();

        //读取URLConnection的响应
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null) {
        	result += line;
        }
        bufferedReader.close();
        connection.disconnect();
        return result;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean postUrl(String urlAddr, Map map) throws Exception {
		HttpURLConnection conn = null;
		boolean isSuccess = false;
		StringBuffer params = new StringBuffer();

		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry element = (Entry) it.next();
			params.append(element.getKey());
			params.append("=");
			params.append(element.getValue());
			params.append("&");
		}

		if (params.length() > 0) {
			params.deleteCharAt(params.length() - 1);
		}

		try {
			URL url = new URL(urlAddr);
			conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-Length", String.valueOf(params.length()));
			conn.setDoInput(true);
			conn.connect();

			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			out.write(params.toString());
			out.flush();
			out.close();

			int code = conn.getResponseCode();
			if (code != 200) {
				System.out.println("ERROR===" + code);
			} else {
				isSuccess = true;
				InputStream is = conn.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		        String line = "";
		        while((line = bufferedReader.readLine()) != null) {
	        		System.out.println(line);
		        }
		        bufferedReader.close();
		        is.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			conn.disconnect();
		}
		return isSuccess;
	}
	
	public static String visitUrl(String getUrl) throws IOException {
		StringBuffer content = new StringBuffer();

		URL myurl = new URL(getUrl);
		HttpURLConnection httpurlconn = (HttpURLConnection)myurl.openConnection();
		int responsecode = httpurlconn.getResponseCode();
		if(responsecode == HttpURLConnection.HTTP_OK) {
			InputStream is = httpurlconn.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
	        String line = "";
	        while((line = bufferedReader.readLine()) != null) {
        		content.append(line);
	        }
	        bufferedReader.close();
	        is.close();
	        httpurlconn.disconnect();
		} else {
			KK.ERROR("responsecode" + responsecode);
			throw new IOException();
		}
		String resCont = content.toString();
		if(resCont.trim().equals("")) {
			throw new IOException();
		}
		return resCont;
	}
}
