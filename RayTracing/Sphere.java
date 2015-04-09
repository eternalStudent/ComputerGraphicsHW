package RayTracing;

public class Sphere {
	final Vector center;
	final float radius;
	final Material material;

	Sphere(float x, float y, float z, float radius, Material material) {
		center = new Vector(x, y, z);
		this.radius = radius;
		this.material = material;
	}
}

