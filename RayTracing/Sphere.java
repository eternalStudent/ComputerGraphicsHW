package RayTracing;

public class Sphere extends Shape3D {
	final Vector center;
	final float radius;

	Sphere(float x, float y, float z, float radius, Material material) {
		super(material);

		center = new Vector(x, y, z);
		this.radius = radius;
	}

	public Hit getHit(Ray ray) {
	    double b = ray.p0.subtract(center).dot(ray.dir);
	    double c = center.distSquared(ray.p0) - radius*radius;

	    double discriminant = b*b-c;
	    if (discriminant < 0.0)
	    	return null;

	    discriminant = Math.sqrt(discriminant);
	    double t1 = (-b-discriminant);
	    double t2 = (-b+discriminant);
	    double t;

	    if (t1 < 0 && t2 > 0) {
	    	t = t2;
	    }
	   	else if (t1 < 0 && t2 < 0) {
	   		return null;
	   	}
	   	else {
	   		t = t1;
	   	}

	    return new Hit(this, t, ray);
  	}

	Vector getNormalAtSurfacePoint(Vector point) {
		return point.subtract(center);
	}
}
