package RayTracing;

import java.util.Random;

public class Plane extends Shape3D{
	private final Vector normal;
	private final double offset;

	Plane(double nx, double ny, double nz, double offset) {
		normal = (new Vector(nx, ny, nz));
		this.offset = offset;
	}

	Plane(Vector normal, double offset) {
		this.normal = normal;
		this.offset = offset;
	}

	@Override
	double getHitDistance(Ray ray) {
		double cosOfAngle = ray.dir.getCosOfAngle(normal);

		if (Math.abs(cosOfAngle) < 0.005) {
			return -1;
		}

		double t = (offset - normal.dot(ray.p0)) / cosOfAngle;
		if (t < 0) {
			return -1;
		}

		return t;
	}

	@Override
	Vector getNormalAtSurfacePoint(Vector point) {
		return normal;
	}

	Vector getRandomDirection(){
		Random r = new Random();
		double x = r.nextDouble();
		double y = r.nextDouble();
		return new Vector(x, y, -x*normal.x/normal.z-y*normal.y/normal.z);
	}
}
