package cn.dreamfield.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import cn.dreamfield.spiderable.GameNewsContentSpiderable;
import cn.dreamfield.utils.HttpDownloadThread;
import cn.jinren.filter.ATagFilter;
import cn.jinren.filter.NetImageFilter;
import cn.jinren.filter.PubDateFilter;
import cn.jinren.filter.ScriptFilter;
import cn.jinren.filter.SpecialStrFilter;
import cn.jinren.filter.StrFilterChain;
import cn.jinren.spider.KKContentSpider;
import cn.jinren.test.KK;

@Component
public class Test {
	@Autowired
	HttpDownloadThread http;
	
	public static ApplicationContext ctx;

	public static void main(String[] args) throws IOException {
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		Test t = (Test)ctx.getBean(Test.class);
		KK.DEBUG(t.http);
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
