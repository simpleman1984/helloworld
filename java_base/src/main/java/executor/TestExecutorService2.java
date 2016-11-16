package executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestExecutorService2 {
	public static void main(String[] args) throws InterruptedException {
		List<String> data = new ArrayList<String>();

		for (int i = 0; i < 10; i++) {
			String str = String.valueOf(i);
			data.add(str);
		}
		
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		
		Callable<String> task = new Callable<String>() {
			@Override
			public String call() throws Exception {
//				System.err.println("---------");
				return "";
			}
		};
		executorService.submit(task);

		executorService.submit(new Runnable(){

			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.err.println("---------");
			}
			
		});
		
		executorService.shutdown();
		System.err.println("after shutdown");
		executorService.awaitTermination(1, TimeUnit.MINUTES);
		
		System.err.println("Ö´ÐÐ½áÊø");
//		System.exit(0);
	}
}
