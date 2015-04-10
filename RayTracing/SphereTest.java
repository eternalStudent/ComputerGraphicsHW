package RayTracing;

import static org.junit.Assert.*;

import org.junit.Test;

public class SphereTest {

	@Test
	public void getIntersectionsTest1() {
		Sphere sphere = new Sphere(0, 0, 0, 5, null);
		Ray ray = new Ray(new Vector(0, 1, 0), new Vector(0, 0, 1));

		assertEquals(sphere.getIntersections(ray).size(), 1);
	}
	
	@Test
	public void getIntersectionsTest2() {
		Sphere sphere = new Sphere(0, 0, 9, 5, null);
		Ray ray = new Ray(new Vector(0, 0, 0), new Vector(0, 0, 1));

		assertEquals(sphere.getIntersections(ray).size(), 2);
	
		boolean flag = true;
	
		for (Vector vect : sphere.getIntersections(ray)) {
			if ( !vect.equals( new Vector(0, 0, 4) ) && !vect.equals( new Vector(0, 0, 14) ) ) {
				System.out.println(vect);
				flag = false;
				break;
			}
		}
	
		assertTrue(flag);
	}

}
