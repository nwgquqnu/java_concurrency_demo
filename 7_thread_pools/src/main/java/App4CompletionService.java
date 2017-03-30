import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class App4CompletionService {

	public static class RandomSleepTask implements Callable<String> {

		public String call() {
			long sleep = Math.abs(new Random().nextLong() % 5000);
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			return Thread.currentThread().getName() + " slept for: " + sleep;
		}
	}

	public static List<Callable<String>> createCallableList() {
		List<Callable<String>> callables = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			callables.add(new RandomSleepTask());
		}
		return callables;
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		CompletionService<String> completionService = new ExecutorCompletionService<>(executorService);

		List<Callable<String>> callables = createCallableList();
		for (Callable<String> callable : callables) {
			completionService.submit(callable);
		}

		for (int i = 0; i < callables.size(); i++) {
			Future<String> result = completionService.take();
			System.out.println(result.get());
		}

		executorService.shutdown();
		executorService.awaitTermination(1, TimeUnit.DAYS);
	}
}
