package executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestExecutorService {

	public static void main(String[] args) {
		List<String> data = new ArrayList<String>();

		for (int i = 0; i < 1000; i++) {
			String str = String.valueOf(i);
			data.add(str);
		}

		int size = data.size();
		int threadNum = 10;

		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);

		List<Future<String>> futures = new ArrayList<Future<String>>();

		for (int i = 0; i < threadNum; i++) {
			final List<String> subList = data.subList(size / threadNum * i, size / threadNum * (i + 1));

			Callable<String> task = new Callable<String>() {
				@Override
				public String call() throws Exception {
					StringBuffer sb = new StringBuffer();
					for (String str : subList) {
						sb.append(str + ",");
					}
					return sb.toString();
				}
			};

			Future<String> taskResult = executorService.submit(task);

			futures.add(taskResult);
		}

		long a = System.currentTimeMillis();

		// shutdown() 方法在终止前允许执行以前提交的任务
		// 第一阶段调用 shutdown 拒绝传入任务，然后调用 shutdownNow（如有必要）取消所有遗留的任务
		// 提交的任务运行结束后关闭线程池
		executorService.shutdown();

		while (true) {
			/**
			 * 通过不断运行ExecutorService.isTerminated()方法检测全部的线程是否都已经运行结束
			 */
			if (executorService.isTerminated()) {
				System.out.println("所有任务执行完毕");
				System.out.println("时间差=" + String.valueOf(System.currentTimeMillis() - a));
				break;
			}
			try {
				// milliseconds
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		StringBuffer result = new StringBuffer();

		for (Future<String> future : futures) {
			try {
				/**
				 * V get() throws InterruptedException, ExecutionException;
				 * 会抛出异常，可以捕获异常，当发生异常时，可以选择立即shutdown其他任务
				 */
				System.out.println("本次任务的执行结果：" + future.get());
				result.append(future.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				// 立即shutdown其他任务
				executorService.shutdownNow();
				e.printStackTrace();
			}
		}

		System.out.println("最终结果：" + result.toString());

	}

}
