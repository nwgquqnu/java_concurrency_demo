
public class App1Synchronized {

	private static class Incrementer {
		// volatile won't help here
		private int count = 0;

		// but synchronized will
		private synchronized void increment() {
			count++;
		}

		public void incrementBy40K() throws InterruptedException {
			Thread thread1 = new Thread(get20KIncrementingRunnable());
			Thread thread2 = new Thread(get20KIncrementingRunnable());

			thread1.start();
			thread2.start();

			thread1.join();
			thread2.join();

		}

		public Runnable get20KIncrementingRunnable() {
			return () -> {
				for (int i = 0; i < 20000; i++) {
					increment();
				}
			};
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Incrementer incrementer = new Incrementer();

		long start = System.currentTimeMillis();
		incrementer.incrementBy40K();

		System.out.printf("Result count is %d and it took %dms\n", incrementer.count, (System.currentTimeMillis() - start));

	}
}
