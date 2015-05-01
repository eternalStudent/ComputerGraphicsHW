package RayTracing;

import java.util.Random;

public class Plane extends Shape3D{
	final Vector normal;
	final double offset;

	Plane(double nx, double ny, double nz, double offset, Material material) {
		super(material);

		normal = (new Vector(nx, ny, nz));
		this.offset = offset;
	}

	Plane(Vector normal, double offset) {
		super(null);

		this.normal = normal.normalize();
		this.offset = offset;
	}

	@Override
	Hit getHit(Ray ray) {
		double cosOfAngle = ray.dir.getCosOfAngle(normal);

		if (Math.abs(cosOfAngle) < 0.005) {
			return null;
		}

		double t = (offset - normal.dot(ray.p0)) / cosOfAngle;
		if (t < 0) {
			return null;
		}

		return new Hit(this, t, ray);
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
