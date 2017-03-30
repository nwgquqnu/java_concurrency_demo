
public class App1Exception {
	
	private static void threadSleep() {
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static class ExceptionThrowingRunnable implements Runnable {
		@Override
		public void run() {
			System.out.println("I'm runnable prepare to sleep");
			threadSleep();
			System.out.println("I'm runnable waked from sleep and throwing RuntimeException");
			throw new RuntimeException("Something really bad happened");
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread runnableThread = new Thread(new ExceptionThrowingRunnable());
		runnableThread.setUncaughtExceptionHandler((Thread t, Throwable e) -> 
			System.out.printf("Caught unhandled exception '%s' from thread: %s\n", e.getMessage(), t.getName())
		);
		
		runnableThread.start();
		System.out.println("I'm main thread: " + Thread.currentThread().getName());
		
		runnableThread.join();
	}

}
