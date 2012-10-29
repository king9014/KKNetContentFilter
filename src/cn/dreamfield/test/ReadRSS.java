package cn.dreamfield.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


public class ReadRSS {
	public static void main(String[] args) {
		new ReadRSS().readRss();
	}
	@SuppressWarnings("unchecked")
	public void readRss() {
		URL feedSource = null; 
		try {
			feedSource = new URL("http://www.u148.net/rss/");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = null; 
		try {
			feed = input.build(new XmlReader(feedSource));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (FeedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String author = feed.getAuthor();
		String title = feed.getTitle();
		String link = feed.getLink();
		Date pubDate = feed.getPublishedDate();
		System.out.println("作者：" + author + "主题：" + title + "主页链接：" + link + "发布日期：" + pubDate);
		List<SyndEntry> syndEntries = (List<SyndEntry>)feed.getEntries();
		for (SyndEntry syndEntry : syndEntries) {
			String entryTitle = syndEntry.getTitle();
			String entryLink = syndEntry.getLink();
			SyndContent entryDescription = syndEntry.getDescription();
			String content = entryDescription.getValue();
			System.out.println("标题：" + entryTitle + "\n" + "链接：" + entryLink + "\n" + "内容：" + content);
		}
	}

}