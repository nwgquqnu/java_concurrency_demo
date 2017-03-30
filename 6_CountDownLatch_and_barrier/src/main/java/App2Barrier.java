import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class App2Barrier {
	private static class SimpleTask implements Runnable {
		
		private int id;
		CyclicBarrier barrier;
		
		public SimpleTask(int id, CyclicBarrier barrier) {
			this.id = id;
			this.barrier = barrier;
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
			System.out.println("started task " + id + " in thread: " + Thread.currentThread().getName());
			threadSleep(3000);
			try {
				barrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
			System.out.println("task finished " + id);
		}
	}

	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		CyclicBarrier barrier = new CyclicBarrier(4, () -> {
			System.out.println("barrier fell. Executing in thread: " + Thread.currentThread().getName());
		});
		ExecutorService executor = Executors.newFixedThreadPool(3);

		for (int i = 0; i < 3; i++) {
			executor.submit(new SimpleTask(i, barrier));
		}

		barrier.await();
		System.out.println("Completed.");
		
		System.out.println("Starting another await");
		for (int i = 5; i < 8; i++) {
			executor.submit(new SimpleTask(i, barrier));
		}
		barrier.await();
		System.out.println("Completed with another await");
		shutdownExecutor(executor);
	}
	
	private static void shutdownExecutor(ExecutorService service) throws InterruptedException {
		service.shutdown();
		service.awaitTermination(1, TimeUnit.DAYS);
	}
	
}
