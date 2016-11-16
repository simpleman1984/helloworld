package sync;

class Book1 extends Thread {   
    private int count=0;   
    private int id;   
    public Book1(int v) {   
        id = v;   
    }   
    public void printVal(int v) {   
        synchronized(Book.class) {   
            while(count++<=10)  
                System.out.println(v);   
        }   
    }   
    public void run() {   
        printVal(id);   
    }   
}   
  
public class SyncClassTest {  
  
    public static void main(String[] args) {  
    	Book1 b1 = new Book1(0);   
        b1.start();   
        Book1 b2 = new Book1(1);  
        b2.start();   
    }  
} 
