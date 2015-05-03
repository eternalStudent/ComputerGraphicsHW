package RayTracing;

public class Sphere extends Shape3D {
	private final Vector center;
	private final float radius;

	Sphere(float x, float y, float z, float radius) {
		center = new Vector(x, y, z);
		this.radius = radius;
	}

	public double getHitDistance(Ray ray) {
	    double b = ray.p0.subtract(center).dot(ray.dir);
	    double c = center.distSquared(ray.p0) - radius*radius;

	    double discriminant = b*b-c;
	    if (discriminant < 0.0)
	    	return -1;

	    discriminant = Math.sqrt(discriminant);
	    double t1 = (-b-discriminant);
	    double t2 = (-b+discriminant);
	    double t;

	    if (t1 < 0 && t2 > 0) {
	    	t = t2;
	    }
	   	else if (t1 < 0 && t2 < 0) {
	   		return -1;
	   	}
	   	else {
	   		t = t1;
	   	}

	    return t;
  	}

	Vector getNormalAtSurfacePoint(Vector point) {
		return point.subtract(center);
	}
}
