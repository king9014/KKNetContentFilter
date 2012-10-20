package cn.dreamfield.in;

import java.io.IOException;

import org.apache.log4j.Logger;

public class TestLog4J {
	
	static Logger log = Logger.getLogger(TestLog4J.class);

	public void test() {
		log.info("Test...");
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		//new TestLog4J().test();
	}

}
