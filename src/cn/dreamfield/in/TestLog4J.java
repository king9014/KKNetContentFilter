package cn.dreamfield.in;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
		File f = new File("c://xx.txt");
		FileReader fis = new FileReader(f);
		BufferedReader br = new BufferedReader(fis);
		br.readLine();
	}

}
