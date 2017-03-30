class TestVolatile extends Thread {
	//volatile should be here
	volatile boolean keepRunning = true;

	public void run() {
		long count = 0;
		while (keepRunning) {
			count++;
		}

		System.out.println("Thread terminated." + count);
	}
}

public class AppVolatile {

	public static void main(String[] args) throws InterruptedException {
		System.out.println("App started.");
		TestVolatile t = new TestVolatile();
		t.start();
		Thread.sleep(1000);
		t.keepRunning = false;
		System.out.println("keepRunning set to false.");
	}
}
