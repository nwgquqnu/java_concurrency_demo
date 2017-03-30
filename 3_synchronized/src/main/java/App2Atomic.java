import java.util.concurrent.atomic.AtomicInteger;

public class App2Atomic {
	
	private static class Incrementer {
		private AtomicInteger count = new AtomicInteger(0);
		
		//but synchronized will 
		private void increment() {
			count.getAndIncrement();
		}
		
		public void incrementBy80K() throws InterruptedException {
			Thread thread1 = new Thread(get20KIncrementingRunnable());
			Thread thread2 = new Thread(get20KIncrementingRunnable());
			Thread thread3 = new Thread(get20KIncrementingRunnable());
			Thread thread4 = new Thread(get20KIncrementingRunnable());
			
			thread1.start();
			thread2.start();
			thread3.start();
			thread4.start();
			
			thread1.join();
			thread2.join();
			thread3.join();
			thread4.join();
			
		}
		
		public Runnable get20KIncrementingRunnable() {
			return () -> {
				for (int i = 0; i < 20000; i++) {
					increment();
				}
			};
		}
		
		public int getCount() {
			return count.get();
		}
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		Incrementer incrementer = new Incrementer();

		long start = System.currentTimeMillis();
		incrementer.incrementBy80K();

		System.out.printf("Result count is %d and it took %dms\n", incrementer.getCount(), (System.currentTimeMillis() - start));

	}
}
