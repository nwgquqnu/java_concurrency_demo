import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class SimpleTask implements Runnable {

	private CountDownLatch latch;
	private int id;

	public SimpleTask(int id, CountDownLatch latch) {
		this.id = id;
		this.latch = latch;
	}

	private void threadSleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("started task " + id);
		threadSleep(3000);
		latch.countDown();

	}
}

public class App1Latch {

	public static void main(String[] args) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(3);
		ExecutorService executor = Executors.newFixedThreadPool(3);

		for (int i = 0; i < 3; i++) {
			executor.submit(new SimpleTask(i, latch));
		}

		latch.await();
		System.out.println("Completed.");
		
		latch.await();
		System.out.println("Completed with another await");
		
		shutdownExecutor(executor);
	}
	
	private static void shutdownExecutor(ExecutorService service) throws InterruptedException {
		service.shutdown();
		service.awaitTermination(1, TimeUnit.DAYS);
	}
}
