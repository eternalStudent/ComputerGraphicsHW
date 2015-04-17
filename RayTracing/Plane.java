package RayTracing;

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
	Hit getHit(Ray ray) {
		double dirDotNorm = ray.dir.dot(normal);

		if (Math.abs(dirDotNorm) < 0.005) {
			return null;
		}

		double t = (offset - normal.dot(ray.p0)) / dirDotNorm;
		if (t < 0) {
			return null;
		}

		return new Hit(this, t, ray);
	}

	@Override
	Vector getClosestIntersection(Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Vector getNormalAtSurfacePoint(Vector point) {
		return normal;
	}

	@Override
	List<Vector> getIntersections(Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}
}