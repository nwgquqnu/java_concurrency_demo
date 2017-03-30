import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class App2Callable {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        
        Future<Integer> future = executor.submit(new Callable<Integer>() {

            @Override
            public Integer call() throws Exception {
                Random random = new Random();
                int duration = random.nextInt(2000);
                
                if(duration > 1000) {
                    throw new IllegalStateException("Invalid sleep duration");
                }
                
                System.out.println("Start sleeping");
                
                Thread.sleep(duration);
                
                System.out.println("Finished.");
                
                return duration;
            }
            
        });
        
        executor.shutdown();
        
        try {
            Integer executionResult = future.get();
			System.out.println("Result is: " + executionResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
        	IllegalStateException ex = (IllegalStateException) e.getCause();
            
            System.out.println(ex.getMessage());
        }
    }
}
