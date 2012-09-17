package cn.dreamfield.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.dreamfield.spiderable.GameNewsContentSpiderable;
import cn.jinren.filter.ATagFilter;
import cn.jinren.filter.NetImageFilter;
import cn.jinren.filter.PubDateFilter;
import cn.jinren.filter.ScriptFilter;
import cn.jinren.filter.SpecialStrFilter;
import cn.jinren.filter.StrFilterChain;
import cn.jinren.spider.KKContentSpider;
import cn.jinren.test.KK;

public class Test {

	public static void main(String[] args) throws IOException {
		String str = KKContentSpider.getContentString(new GameNewsContentSpiderable(), "gbk");
		StrFilterChain chain = new StrFilterChain();
		chain.addStrFilter(new NetImageFilter())
			.addStrFilter(new ScriptFilter())
			//.addStrFilter(new ATagFilter())
			.addStrFilter(new PubDateFilter())
			.addStrFilter(new SpecialStrFilter());
		String result = chain.doFilter(str);
		File file = new File("c:/kdownload/html/201209/");
		if(!file.exists()) { //判断路径是否存在，不存在则创建
			file.mkdirs();
		}
		FileOutputStream fos = new FileOutputStream("c:/kdownload/html/201209/result.html");//建立文件
		fos.write(result.getBytes());
	}

}
