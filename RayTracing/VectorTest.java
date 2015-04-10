package RayTracing;

import static org.junit.Assert.*;

import org.junit.Test;

public class VectorTest {

	@Test
	public void equalsTest() {
		assertEquals(new Vector(0, 0, 0), Vector.ZERO);
	}

}