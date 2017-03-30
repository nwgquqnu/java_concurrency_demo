import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

public class App4StampedLockOptimistic {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		StampedLock lock = new StampedLock();

		executor.submit(() -> {
			long stamp = lock.tryOptimisticRead();
			try {
				System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
				sleep(1);
				System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
				sleep(2);
				System.out.println("Optimistic Lock Valid: " + lock.validate(stamp));
			} finally {
				lock.unlock(stamp);
			}
		});

		executor.submit(() -> {
			long stamp = lock.writeLock();
			try {
				System.out.println("Write Lock acquired");
				sleep(2);
			} finally {
				lock.unlock(stamp);
				System.out.println("Write done");
			}
		});

		stop(executor);
	}

	private static void sleep(int s) {
		try {
			Thread.sleep(s * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private static void stop(ExecutorService executor) throws InterruptedException {
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.DAYS);

	}
}
