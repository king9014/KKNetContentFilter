package cn.dreamfield.in;

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
import cn.dreamfield.utils.HttpDownloadUtils;
import cn.dreamfield.utils.SpringUtils;
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
		HttpDownloadUtils h = SpringUtils.ctx.getBean(HttpDownloadUtils.class);
		KK.LOG(h.DownloadHtmlFromURL(new GameNewsContentSpiderable()));
	}

}
