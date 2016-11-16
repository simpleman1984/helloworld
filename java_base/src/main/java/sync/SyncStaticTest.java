package sync;

class Book2 extends Thread {   
    
    private int count=0;   
    private int id;   
    private static Object lock=new Object();  
    public Book2(int v) {   
        id = v;   
    }   
    public void printVal(int v) {   
        synchronized(lock) {   
            while(count++<=10)  
                System.out.println(v);   
        }   
    }   
    public void run() {   
        printVal(id);   
    }   
}   
  
public class SyncStaticTest {  
  
    public static void main(String[] args) {  
        Book2 f1 = new Book2(0);   
        f1.start();   
        Book2 f2 = new Book2(1);  
        f2.start();   
    }  
}
