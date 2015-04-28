package RayTracing;

public abstract class Shape3D {
	final Material material;

	Shape3D(Material material) {
		this.material = material;
	}

	abstract Hit getHit(Ray ray);
	abstract Vector getNormalAtSurfacePoint(Vector point);
}
