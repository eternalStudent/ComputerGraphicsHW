package raytracing;

public class Test {
	final static String HERP_DERP = "You're an idiot.";
	final static String TIS_ALL_GOOD = "You can probably tie your shoelaces.";

	public static void main(String[] args) {
		// testRayOriginatesFromSphere();
		// testTwoIntersectionPointsOfSphere();
		testCamera();
	}

	static void testRayOriginatesFromSphere() {
		Sphere sphere = new Sphere(0, 0, 0, 5, null);
		Ray ray = new Ray(new Vector(0, 1, 0), new Vector(0, 0, 1));

		if (sphere.getIntersections(ray).size() != 1) {
			System.out.println(HERP_DERP);
		} else {
			System.out.println(TIS_ALL_GOOD);
		}
	}

	static void testTwoIntersectionPointsOfSphere() {
		Sphere sphere = new Sphere(0, 0, 9, 5, null);
		Ray ray = new Ray(new Vector(0, 0, 0), new Vector(0, 0, 1));

		for (Vector vect : sphere.getIntersections(ray)) {
			System.out.println(vect);
		}
	}

	static voice testCamera() {
		Camera camera = new Camera(
			0,  0,  0,
			lx,  ly,  lz,
			ux,  uy,  uz,
			screenDistance,  screenWidth,
			500,  500));
	}
}