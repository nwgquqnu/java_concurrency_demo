import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App1Deadlock {

	public static class LeftRightDeadlock {
		private final Object left = new Object();
		private final Object right = new Object();

		public void leftRight() {
			synchronized (left) {
				synchronized (right) {
					lockedLeftRight();
				}
			}
		}

		private void lockedLeftRight() {
			System.out.println("received locks for left right case");
			
		}

		public void rightLeft() {
			synchronized (right) {
				synchronized (left) {
					lockedRightLeft();
				}
			}
		}

		private void lockedRightLeft() {
			System.out.println("received locks for right left case");
			
		}
	}

	
	public static void main(String[] args) throws InterruptedException {
		ExecutorService service = Executors.newCachedThreadPool();
		LeftRightDeadlock obj = new LeftRightDeadlock();
		for (int i = 0; i < 5; i++) {
			service.submit(() -> obj.leftRight());
			service.submit(() -> obj.rightLeft());
		}
		
		service.shutdown();
		service.awaitTermination(1, TimeUnit.DAYS);
	}

}
