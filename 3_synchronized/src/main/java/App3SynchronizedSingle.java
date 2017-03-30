import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App3SynchronizedSingle {

	private static class AnimalUpdater {
		private Random rand = new Random();
		private List<Integer> cats = new ArrayList<>();
		private List<Integer> dogs = new ArrayList<>();

		private synchronized void addRandomCat() {
			sleepForSomeTime();
			cats.add(rand.nextInt());
		}

		private synchronized void addRandomDog() {
			sleepForSomeTime();
			dogs.add(rand.nextInt());
		}

		private void sleepForSomeTime() {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public void massiveUpdate() throws InterruptedException {
			Thread thread1 = new Thread(getUpdateListsRunnable());
			Thread thread2 = new Thread(getUpdateListsRunnable());

			thread1.start();
			thread2.start();

			thread1.join();
			thread2.join();

		}

		public Runnable getUpdateListsRunnable() {
			return () -> {
				for (int i = 0; i < 1000; i++) {
					addRandomCat();
					addRandomDog();
				}
			};
		}
	}

	public static void main(String[] args) throws InterruptedException {
		AnimalUpdater incrementer = new AnimalUpdater();

		long start = System.currentTimeMillis();
		incrementer.massiveUpdate();

		System.out.printf("Update took %dms\n", (System.currentTimeMillis() - start));

	}
}
