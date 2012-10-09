package cn.dreamfield.in;

import org.apache.log4j.Logger;

public class TestLog4J {
	
	static Logger log = Logger.getLogger(TestLog4J.class);

	public void test() {
		log.info("Test...");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestLog4J().test();
		
	}

}
