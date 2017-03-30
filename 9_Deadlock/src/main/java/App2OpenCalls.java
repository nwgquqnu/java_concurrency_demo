import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App2OpenCalls {
	private static class Taxi {
		private Point location, destination;
		private final Dispatcher dispatcher;

		public Taxi(Dispatcher dispatcher) {
			this.dispatcher = dispatcher;
		}

		public synchronized Point getLocation() {
			return location;
		}

		public synchronized void setLocation(Point location) {
			this.location = location;
			if (location.equals(destination)) {
				System.out.println("notifying dispatcher");
				dispatcher.notifyAvailable(this);
			}
		}

		public synchronized void setDestination(Point destination) {
			this.destination = destination;
		}
	}

	private static class Dispatcher {
		private final Set<Taxi> taxis;
		private final Set<Taxi> availableTaxis;

		public Dispatcher() {
			taxis = new HashSet<Taxi>();
			availableTaxis = new HashSet<Taxi>();
		}

		public synchronized void notifyAvailable(Taxi taxi) {
			availableTaxis.add(taxi);
		}

		public synchronized Image getImage() {
			Image image = new FakeImage();
			for (Taxi t : taxis) {
				System.out.println("drawing location");
				image.drawMarker(t.getLocation());
			}
			return image;
		}

		public synchronized void addTaxiToPark(Taxi taxi) {
			taxis.add(taxi);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Dispatcher dispatcher = new Dispatcher();
		Taxi taxi = new Taxi(dispatcher);
		taxi.setDestination(new Point(0, 0));
		dispatcher.addTaxiToPark(taxi);

		ExecutorService service = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			service.execute(() -> taxi.setLocation(new Point(0, 0)));
			service.execute(() -> dispatcher.getImage());
		}

		service.shutdown();
		service.awaitTermination(1, TimeUnit.DAYS);

		System.out.println("all taxi routes processed");

	}
}
