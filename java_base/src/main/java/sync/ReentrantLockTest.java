package sync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Outputter1 {    
    private Lock lock = new ReentrantLock();// 锁对象    
  
    public void output(String name) {           
        lock.lock();      // 得到锁    
  
        try {    
            for(int i = 0; i < name.length(); i++) {    
                System.out.print(name.charAt(i));    
            }    
        } finally {    
            lock.unlock();// 释放锁    
        }    
    }    
}  
public class ReentrantLockTest {

	public static void main(String[] args) {
		final Outputter1 o = new Outputter1();

		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				o.output("xxxxxxxxxxx");
			}
			
		});
		t.start();
		
		Thread t2 = new Thread(new Runnable(){

			@Override
			public void run() {
				o.output("yyyyyyyyyyyyyyyyyy");
			}
			
		});
		t2.start();
	}

}
