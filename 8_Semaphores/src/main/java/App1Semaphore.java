import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class App1Semaphore {

	private Semaphore semaphore = new Semaphore(5, true);
	
	private int connections = 0;
	
	private void connect() {
		try {
			doConnect();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void doConnect() throws InterruptedException{
		semaphore.acquire();
		try {
			synchronized(this) {
				connections++;
				System.out.println("Current connections: " + connections);
			}
			
			Thread.sleep(2000);
			
			synchronized(this) {
				connections--;
				System.out.println("Current connections after sleep: " + connections);
			}
		} finally {
			semaphore.release();
		}
		
	}

	public static void main(String[] args) throws InterruptedException {
		ExecutorService service = Executors.newCachedThreadPool();
		
		App1Semaphore app = new App1Semaphore();
		for (int i = 0; i < 20; i++) {
			service.submit(() -> {
				app.connect();
			});
		}
		
		service.shutdown();
		service.awaitTermination(1, TimeUnit.DAYS);
	}
}
