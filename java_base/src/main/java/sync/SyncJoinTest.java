package sync;

public class SyncJoinTest {

	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(new ThreadTesterA());
		Thread t2 = new Thread(new ThreadTesterB());
		t1.start();
		t1.join(); // t1线程完成run方法后才会继续执行下面的代码！
		t2.start();
		t2.join();
		
	}

	static class ThreadTesterA implements Runnable {

		private int i;

		@Override
		public void run() {
			while (i < 10) {
				System.out.println("111111111111111111111111=" + i + " ");
				i++;
			}
		}
	}

	static class ThreadTesterB implements Runnable {

		private int j;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (j < 10) {
				System.out.println("2222222222222222222222222=" + j + " ");
				j++;
			}
		}
	}
}
