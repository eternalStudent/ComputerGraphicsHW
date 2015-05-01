package RayTracing;

public class Hit {
	final double dist;
	final Shape3D shape;
	final Vector normal;
	final Vector intersection;

	Hit(Shape3D shape, double dist, Ray ray) {
		this.dist = dist;
		this.shape = shape;

		intersection = ray.getPointAlongRay(dist);
		normal = shape.getNormalAtSurfacePoint(intersection);
	}

	Color getDiffuseColor() {
		return shape.material.diffuse;
	}

	Color getSpecularColor() {
		return shape.material.specular;
	}

	float getPhong() {
		return shape.material.phong;
	}

	Color getReflectColor() {
		return shape.material.reflection;
	}

	float getTransparency() {
		return shape.material.trans;
	}
}
