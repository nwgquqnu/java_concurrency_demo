import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App1ThreadPool {
	
	private static void threadSleep(int timeMs) {
		try {
			Thread.sleep(timeMs);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static class SimpleTask implements Runnable {

		private int id;
		public SimpleTask(int id) {
			this.id = id;
		}

		@Override
		public void run() {
			System.out.println("starting task " + id);
			threadSleep(5000);
			System.out.println("finished task " + id);
			
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		ExecutorService service = Executors.newFixedThreadPool(3);
		
		for (int i = 0; i < 10; i++) {
			service.submit(new SimpleTask(i));
			
		}
		
		System.out.println("all tasks submitted. finishing executor");
		service.shutdown();
		
		service.awaitTermination(1, TimeUnit.DAYS);
		System.out.println("tasks completed");
	}
}
