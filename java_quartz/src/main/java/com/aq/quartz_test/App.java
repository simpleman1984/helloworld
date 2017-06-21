package com.aq.quartz_test;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import com.aq.quartz_test.trigger.CustomScheduleBuilder;
import com.aq.quartz_test.trigger.CustomTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.aq.quartz_test.job.HelloJob;

/**
 * 定时器逻辑，使用手册 !!!!!!!!!!!!!!!!!!!!!!!!!! <br>
 * http://www.quartz-scheduler.org/documentation/quartz-2.2.x/cookbook/
 */
public class App {

	/**
	 * 语法详细解释<br/>
	 * http://www.quartz-scheduler.org/documentation/quartz-2.2.x/tutorials/
	 * crontrigger.html <br/>
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		try {
			System.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
			System.setProperty("org.quartz.scheduler.skipUpdateCheck","false");
			// Grab the Scheduler instance from the Factory
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

			// and start it off
			scheduler.start();

			// define the job and tie it to our HelloJob class
			JobDetail job = newJob(HelloJob.class).withIdentity("job1", "group1")
					.usingJobData("jobSays", "Hello World!").usingJobData("myFloatValue", 3.141f).storeDurably().build();

			// Trigger the job to run now, and then repeat every 40 seconds
			// Trigger trigger = newTrigger().withIdentity("trigger1",
			// "group1").startNow()
			// .withSchedule(simpleSchedule().withIntervalInSeconds(2).repeatForever()).build();

//			Trigger trigger = newTrigger().withIdentity("trigger3", "group1")
////					.withSchedule(cronSchedule("0/2 * * * * ?"))
//					.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(2))
//					.forJob("job1", "group1")
//
//					.build();

			CustomScheduleBuilder customScheduleBuilder = new CustomScheduleBuilder();
			Trigger trigger = newTrigger().withSchedule(customScheduleBuilder).build();

			// Tell quartz to schedule the job using our trigger
			scheduler.scheduleJob(job, trigger);
			
			Thread.sleep(10000);
				
			System.err.println("移除当前的定时器~~~~~~~~~~~~");
			scheduler.unscheduleJob(trigger.getKey());

			Thread.sleep(10000);

			//http://www.quartz-scheduler.org/documentation/quartz-2.2.x/cookbook/ShutdownScheduler.html
			scheduler.shutdown();
		} catch (Exception se) {
			se.printStackTrace();
		}
	}
}
