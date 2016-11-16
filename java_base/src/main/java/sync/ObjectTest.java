package sync;

public class ObjectTest {

	private static  byte[] b = new byte[0];
	
	public static void main(String[] args) throws InterruptedException {
		final A a = new A();

		Thread aThread = new Thread(new Runnable(){

			@Override
			public void run() {
//				try {
//					a.printThreadInfo();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
				synchronized (a) {
					try {
						a.printThreadInfo();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		});
		aThread.start();
		
//		aThread.join();
		
		Thread bThread = new Thread(new Runnable(){

			@Override
			public void run() {
				synchronized (a) {
					System.err.println("B===========任务运行结束，准备释放A");
					a.notify();
					System.err.println("B===========进程执行完成！");
				}
			}
			
		});
		bThread.start();
		
	}
}

class A {
	public void printThreadInfo() throws InterruptedException {
		Thread t = Thread.currentThread();
		System.err.println(t);
		// this.wait();//一直等待
		System.err.println("A==========开始执行，等5秒，或者等B执行完成则提前结束！！！");
		this.wait(5000);// 等待1000ms
		System.err.println("A==========执行结束。。。。。。。。。。。。。。。");
		// super.wait(1000);
	}
	
}
