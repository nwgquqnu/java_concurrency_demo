import java.util.Scanner;

public class App3WaitNotifySimple {
	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(new SimpleProducer());
		Thread t2 = new Thread(new SimpleConsumer());

		t1.start();
		t2.start();

		t1.join();
		t2.join();
	}

	private static class SimpleProducer implements Runnable {
		@Override
		public void run() {
			try {
				produce();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		private void produce() throws InterruptedException {
			synchronized (App3WaitNotifySimple.class) {
				System.out.println("Producer thread is running");
				
				App3WaitNotifySimple.class.wait();

				System.out.println("Resumed");
			}
		}
	}

	private static class SimpleConsumer implements Runnable {

		@Override
		public void run() {
			try {
				consume();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		private void consume() throws InterruptedException {
			Scanner scanner = new Scanner(System.in);
			Thread.sleep(2000);

			synchronized (App3WaitNotifySimple.class) {
				System.out.println("Press ENTER");
				scanner.nextLine();
				System.out.println("ENTER is pressed");
				App3WaitNotifySimple.class.notify();
				Thread.sleep(5000);
			}
		}
	}
}
