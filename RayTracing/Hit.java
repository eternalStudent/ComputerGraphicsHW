package RayTracing;

public class Hit implements Comparable<Hit>{
	final double dist;
	final Primitive primitive;
	final Vector normal;
	final Vector intersection;

	Hit(Primitive primitive, double dist, Ray ray) {
		this.dist = dist;
		this.primitive = primitive;

		intersection = ray.getPointAlongRay(dist);
		normal = primitive.shape.getNormalAtSurfacePoint(intersection);
	}

	Color getDiffuseColor() {
		return primitive.material.diffuse;
	}

	Color getSpecularColor() {
		return primitive.material.specular;
	}

	double getPhong() {
		return primitive.material.phong;
	}

	Color getReflectColor() {
		return primitive.material.reflection;
	}

	double getTransparency() {
		return primitive.material.trans;
	}

	@Override
	public int compareTo(Hit other) {
		return Double.compare(dist, other.dist);
	}

}
