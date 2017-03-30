import java.awt.Point;

interface Image {
	void drawMarker(Point point);
}

public class FakeImage implements Image {

	@Override
	public void drawMarker(Point point) {
		System.out.println("drawing image");
	}

}
