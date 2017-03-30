import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class App2ReadWriteLock {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Map<String, String> map = new HashMap<>();
		ReadWriteLock lock = new ReentrantReadWriteLock();

		executor.submit(() -> {
		    lock.writeLock().lock();
		    try {
		        sleep(1);
		        map.put("foo", "bar");
		    } finally {
		        lock.writeLock().unlock();
		    }
		});
		
		Runnable readTask = () -> {
		    lock.readLock().lock();
		    try {
		        System.out.println(map.get("foo"));
		        sleep(1);
		    } finally {
		        lock.readLock().unlock();
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
