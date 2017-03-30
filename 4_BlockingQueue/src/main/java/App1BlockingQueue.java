import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class App1BlockingQueue {

	public static void main(String[] args) throws InterruptedException {
		BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10);
		Thread t1 = new Thread(new Producer(queue));
		Thread t2 = new Thread(new Consumer(queue));

		t1.start();
		t2.start();

		
		try(Scanner scanner = new Scanner(System.in)) {
			scanner.nextLine();
		}

		t1.interrupt();
		t2.interrupt();

		t1.join();
		t2.join();
	}

	
	//Producer
	private static class Producer extends BlockingQueueRunnable {
		public Producer(BlockingQueue<Integer> queue) {
			super(queue);
		}

		@Override
		public void doSomething(Random random, BlockingQueue<Integer> queue) throws InterruptedException {
			queue.put(random.nextInt(100));
		}
	}
	//Consumer
	private static class Consumer extends BlockingQueueRunnable {
		public Consumer(BlockingQueue<Integer> queue) {
			super(queue);
		}

		@Override
		public void doSomething(Random random, BlockingQueue<Integer> queue) throws InterruptedException {
			Thread.sleep(100);

			if (random.nextInt(10) == 0) {
				Integer value = queue.take();

				System.out.println("Taken value: " + value + "; Queue size is: " + queue.size());
			}
		}
	}
	//Abstract blocking queue runnable
	private static abstract class BlockingQueueRunnable implements Runnable {
		private BlockingQueue<Integer> queue;
		
		public BlockingQueueRunnable(BlockingQueue<Integer> queue) {
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
		
		protected abstract void doSomething(Random random, BlockingQueue<Integer> queue) throws InterruptedException;
		
	}

}
