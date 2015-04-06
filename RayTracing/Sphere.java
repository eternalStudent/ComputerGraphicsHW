package RayTracing;

public class Sphere {
	final Vector center;
	final int radius;
	final Material material;

	Sphere(int x, int y, int z, int radius, Material material) {
		center = new Vector(x, y, z);
		this.radius = radius;
		this.material = material;
	}
}

