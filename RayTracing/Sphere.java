package RayTracing;

import java.util.ArrayList;
import java.util.List;

public class Sphere extends Shape3D {
	final Vector center;
	final float radius;

	Sphere(float x, float y, float z, float radius, Material material) {
		super(material);

		center = new Vector(x, y, z);
		this.radius = radius;
	}

	@Override
	List<Vector> getIntersections(Ray ray) {
		ArrayList<Vector> intersections = new ArrayList<>();

		if ( ray.dir.dot( center.subtract(ray.p0) ) < 0) {
			return intersections;
		}
		if (ray.p0.distSquared(center) <= radius*radius) {
			intersections.add( ray.getPointAlongRay(radius) );

			return intersections;
		}

		if ( center.equals(Vector.ZERO) ) {
			Vector vect = ray.dir.toLength(ray.p0.norm());
			if (vect.add(ray.p0).norm() > radius) {
				return intersections;
			} else {
				System.out.println("We'll see");
				return intersections;
			}
		}

		Vector centerProjOntoRay = center.projectOntoVector(ray.dir);

		double distSqCenterToRay = centerProjOntoRay.distSquared(center);

		if (distSqCenterToRay > radius*radius) {
			return intersections;
		}

		double dist = Math.sqrt(radius*radius - distSqCenterToRay);

		intersections.add( ray.dir.toLength(centerProjOntoRay.norm() - dist) );
		intersections.add( ray.dir.toLength(centerProjOntoRay.norm() + dist) );

		return intersections;
	}

	Vector getClosestIntersection(Ray ray) {
		//List<Vector> intersections = getIntersections(ray);

		//return !intersections.isEmpty() ? intersections.get(0) : null;
		// return intersect(ray);
		return null;
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
		return point.subtract(center).normalize();
	}
}

