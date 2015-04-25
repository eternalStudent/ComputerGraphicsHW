package RayTracing;

import static org.junit.Assert.*;

import org.junit.Test;

public class VectorTest {

	@Test
	public void equalsTest() {
		assertEquals(new Vector(0, 0, 0), Vector.ZERO);
	}

	@Test
	public void reflectionTest1() {
		Vector v1 = new Vector(-1, -1, 0);
		Vector normal = new Vector(0, 1, 0);

		assertEquals(v1.getReflectionAroundNormal(normal), new Vector(-1, 1, 0));
	}

	@Test
	public void reflectionTest2() {
		Vector v1 = new Vector(1, 1, 0);
		Vector normal = new Vector(0, 1, 0);

		System.out.println(v1.getReflectionAroundNormal(normal));
		assertEquals(v1.getReflectionAroundNormal(normal), new Vector(-1, 1, 0));
	}

}