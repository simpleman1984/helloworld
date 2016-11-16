package sync;

class Book extends Thread {
	private int id;

	public Book(int v) {
		id = v;
	}

	public synchronized void Print(int v) {
		while (true)
			System.out.println(v);
	}

	public void run() {
		Print(id);
	}
}

public class SyncTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Book b1 = new Book(0);
		b1.start();
		Book b2 = new Book(1);
		b2.start();
	}
}