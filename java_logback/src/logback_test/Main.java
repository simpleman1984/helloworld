package logback_test;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import logback_test.xx.xx.adf.TestExec;

public class Main {

	private static Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void main(String args[]) throws InterruptedException
	{
		logger.debug("xxxxxxxxxxx" + new Date());
//		while(true){
//		try{
//			logger.error("xxxxxxxxxxx" + new Date());
//			int i = 1/0;
//		}catch(Exception e){
//			logger.error(e.getLocalizedMessage(),e);
//		}
//		
//		try{
//
//			TestExec c = new TestExec();
//			c.e();
//			
//		}catch(Exception e)
//		{
//			e.printStackTrace();
//		}
		
		Thread.sleep(10000);
//		}
	}
	
}
