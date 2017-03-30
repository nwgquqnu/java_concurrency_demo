import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class App2WaitNotify {
	private static final int MAX_SIZE = 10;

	public static void main(String[] args) throws InterruptedException {
		Queue<Integer> queue = new LinkedList<Integer>();
		Thread t1 = new Thread(new Producer(queue));
		Thread t2 = new Thread(new Consumer(queue));

		t1.start();
		t2.start();

		try (Scanner scanner = new Scanner(System.in)) {
			scanner.nextLine();
		}

		t1.interrupt();
		t2.interrupt();

		t1.join();
		t2.join();
	}

	// Producer
	private static class Producer extends QueueRunnable {

		public Producer(Queue<Integer> queue) {
			super(queue);
		}

		@Override
		public void doSomething(Random random, Queue<Integer> queue) throws InterruptedException {
			Thread.sleep(10);
			synchronized (queue) {
				while (queue.size() == MAX_SIZE) {
					queue.wait();
				}
				queue.add(random.nextInt(100));
				queue.notify();
			}
		}
	}

	// Consumer
	private static class Consumer extends QueueRunnable {
		public Consumer(Queue<Integer> queue) {
			super(queue);
		}

		@Override
		public void doSomething(Random random, Queue<Integer> queue) throws InterruptedException {
			Thread.sleep(100);

			if (random.nextInt(10) == 0) {
				synchronized (queue) {
					while (queue.isEmpty()) {
						queue.wait();
					}
					Integer value = queue.poll();
					queue.notify();// lock is not released here
					Thread.sleep(5000);
					System.out.println("Taken value: " + value + "; Queue size is: " + queue.size());
				}
			}
		}
	}

	// Abstract blocking queue runnable
	private static abstract class QueueRunnable implements Runnable {
		private Queue<Integer> queue;

		public QueueRunnable(Queue<Integer> queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			Random random = new Random();

			while (!Thread.currentThread().isInterrupted()) {
				try {
					doSomething(random, queue);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					e.printStackTrace();
				}
			}
		}

		protected abstract void doSomething(Random random, Queue<Integer> queue) throws InterruptedException;

	}
}
