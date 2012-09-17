package cn.dreamfield.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringUtils {
	public static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
}
