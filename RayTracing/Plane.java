package RayTracing;

import java.util.ArrayList;
import java.util.List;

public class Plane extends Shape3D{
	final Vector normal;
	final double offset;

	Plane(double nx, double ny, double nz, double offset, Material material) {
		super(material);

		normal = (new Vector(nx, ny, nz)).normalize();
		this.offset = offset;
	}

	Plane(Vector normal, double offset, Material material) {
		super(material);

		this.normal = normal.isNormalized() ? normal : normal.normalize();
		this.offset = offset;
	}

	@Override
	List<Vector> getIntersections(Ray ray) {
		List<Vector> intersections = new ArrayList<>();

		double dirDotNorm = Math.abs(ray.dir.dot(normal));

		if (dirDotNorm < 0.005) {
			return intersections;
		}

		double t = -(ray.p0.dot(normal) + offset) / dirDotNorm;

		intersections.add(ray.getVectAlongRay(t));

		return intersections;
	}

	@Override
	Vector getClosestIntersection(Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}
}