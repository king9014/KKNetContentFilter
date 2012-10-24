package cn.dreamfield.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUploadUtil {
	public static void main(String[] args) {
		File file = new File("C:\\2.html");
		try {
			postUpload(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void postUpload(File file) throws IOException {
		URL url = new URL("http://dreamfield.cn/lib/upload.php");
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
        while((line = bufferedReader.readLine()) != null) {
        	System.out.println(line);
        }
        bufferedReader.close();
        connection.disconnect();
	}
}
