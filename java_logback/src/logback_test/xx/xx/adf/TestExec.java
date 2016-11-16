package logback_test.xx.xx.adf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestExec {

	private Logger logger = LoggerFactory.getLogger(TestExec.class);
	
	public void e(){
		logger.error("xxxxxx");
		int i = 1/0;
	}
}
