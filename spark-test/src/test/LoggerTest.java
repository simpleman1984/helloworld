package test;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

public class LoggerTest {

	transient static Logger logger = org.apache.log4j.LogManager.getLogger("myLogger");
	
	public static void main(String[] args) {
		Timer timer = new Timer();

		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				logger.error("11111111111111111111111" + new Date());
			}
			
		}, 10000, 2000);
	}

}
