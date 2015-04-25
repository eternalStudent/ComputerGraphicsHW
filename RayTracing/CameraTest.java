package RayTracing;

import static org.junit.Assert.*;

import org.junit.Test;

public class CameraTest {

	@Test
	public void getRayByPixelCoordinateTest() {
		Camera camera = new Camera(
				5, 0, 0,
				0, 0, 0,
				0, 1, 0,
				1,  3,
				500,  500);

		Ray ray = camera.getRayByPixelCoordinate(250, 250);
		assertEquals(ray, new Ray(new Vector(5, 0, 0), new Vector(-1, 0, 0)));
	}

}
