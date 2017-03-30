import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

public class App3StampedLock {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Map<String, String> map = new HashMap<>();
		StampedLock lock = new StampedLock();

		executor.submit(() -> {
		    long stamp = lock.writeLock();
		    try {
		        sleep(1);
		        map.put("foo", "bar");
		    } finally {
		        lock.unlockWrite(stamp);
		    }
		});

		Runnable readTask = () -> {
		    long stamp = lock.readLock();
		    try {
		        System.out.println(map.get("foo"));
		        sleep(1);
		    } finally {
		        lock.unlockRead(stamp);
		    }
		};

		executor.submit(readTask);
		executor.submit(readTask);

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
