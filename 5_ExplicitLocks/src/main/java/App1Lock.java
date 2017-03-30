import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class App1Lock {

	private static class Incrementor {

		private int count = 0;
		private Lock lock = new ReentrantLock();
		private Condition cond = lock.newCondition();

		private void increment() {
			for (int i = 0; i < 10000; i++) {
				count++;
			}
		}

		public void firstThread() throws InterruptedException {
			if (lock.tryLock(5, TimeUnit.MINUTES)) {
				try {
					System.out.println("Waiting for signal");
					cond.await();

					System.out.println("Woken up");

					increment();
				} finally {
					lock.unlock();
				}
			} else {
				System.out.println("Haven't acquired lock in time in first thread");
			}
		}

		public void secondThread() throws InterruptedException {
			Thread.sleep(1000);
			if (lock.tryLock(5, TimeUnit.MINUTES)) {
				try {
					System.out.println("Press ENTER");
					new Scanner(System.in).nextLine();
					System.out.println("ENTER pressed");

					cond.signal();

					increment();
				} finally {
					lock.unlock();
				}
			} else {
				System.out.println("Haven't acquired lock in time in second thread");
			}
		}

		public int getCount() {
			return count;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Incrementor incrementor = new Incrementor();
		Thread thread1 = new Thread(() -> {
			try {
				incrementor.firstThread();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		Thread thread2 = new Thread(() -> {
			try {
				incrementor.secondThread();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		thread1.start();
		thread2.start();

		thread1.join();
		thread1.join();

		System.out.println("Count: " + incrementor.getCount());
	}
}
