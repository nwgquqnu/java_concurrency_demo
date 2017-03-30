
public class App {
	
	private static void threadSleep() {
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static class RunnableClass implements Runnable {
		@Override
		public void run() {
			for (int i = 0; i < 5; i++) {
				threadSleep();
				System.out.println("I'm runnable");
			}
		}
	}
	
	private static class ThreadClass extends Thread {
		@Override
		public void run() {
			for (int i = 0; i < 5; i++) {
				threadSleep();
				System.out.println("I'm extended thread");
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread runnableThread = new Thread(new RunnableClass());
		ThreadClass extendedThread = new ThreadClass();
		
		runnableThread.start();
		extendedThread.start();
		System.out.println("I'm main thread");
		
		runnableThread.join();
		extendedThread.join();
	}

}
