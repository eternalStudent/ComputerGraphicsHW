package RayTracing;

public class Box extends Shape3D {

	final double x0, y0, z0, x1, y1, z1;
	final Vector position;
	final Vector rotation;

	public Box(double px, double py, double pz,
			double sx, double sy, double sz,
			double rx, double ry, double rz) {
		x0 = px-sx/2;
		y0 = py-sy/2;
		z0 = pz-sz/2;
		x1 = px+sx/2;
		y1 = py+sy/2;
		z1 = pz+sz/2;
		position = new Vector(px, py, pz);
		rotation = new Vector(rx, ry, rz);
	}

	@Override
	double getHitDistance(Ray ray) {
		double inv1 = (1 / ray.dir.x);
		double inv2 = (1 / ray.dir.y);
		double inv3 = (1 / ray.dir.z);

		double tmin = Double.NEGATIVE_INFINITY;
		double tmax = Double.POSITIVE_INFINITY;

        double tx1 = (x0 - ray.p0.x)*inv1;
        double tx2 = (x1 - ray.p0.x)*inv1;

        tmin = Math.max(tmin, Math.min(tx1, tx2));
        tmax = Math.min(tmax, Math.max(tx1, tx2));

        double ty1 = (y0 - ray.p0.y)*inv2;
        double ty2 = (y1 - ray.p0.y)*inv2;

        tmin = Math.max(tmin, Math.min(ty1, ty2));
        tmax = Math.min(tmax, Math.max(ty1, ty2));

        double tz1 = (z0 - ray.p0.z)*inv3;
        double tz2 = (z1 - ray.p0.z)*inv3;

        tmin = Math.max(tmin, Math.min(tz1, tz2));
        tmax = Math.min(tmax, Math.max(tz1, tz2));

	    if (tmax >= tmin)
	    	return tmin;
	    return -1;
	}

	@Override
	Vector getNormalAtSurfacePoint(Vector point) {
		if (isVertex(point)){
			return point.subtract(position);
		}
		double EPSILON = RayTracer.EPSILON;

		if (point.y+EPSILON >= y0 && point.y-EPSILON <= y1 && point.z+EPSILON >= z0 && point.z-EPSILON <= z1){
			if (Math.abs(point.x-x0)<EPSILON)
				return new Vector(-1, 0, 0);
			return new Vector(1, 0, 0);
		}
		if (point.x+EPSILON >= x0 && point.x-EPSILON <= x1 && point.z+EPSILON >= z0 && point.z-EPSILON <= z1){
			if (Math.abs(point.y-y0)<EPSILON)
				return new Vector(0, -1, 0);
			return new Vector(0, 1, 0);
		}
		if (point.x+EPSILON >= x0 && point.x-EPSILON <= x1 && point.y+EPSILON >= y0 && point.y-EPSILON <= y1){
			if (Math.abs(point.z-z0)<EPSILON)
				return new Vector(0, 0, -1);
			return new Vector(0, 0, 1);
		}
		System.out.println("Herp!");
		return Vector.ZERO;
	}

	boolean isVertex(Vector point){
		return (Math.abs(point.x-x0)<RayTracer.EPSILON || Math.abs(point.x-x1)<RayTracer.EPSILON)
				&& (Math.abs(point.y-y0)<RayTracer.EPSILON || Math.abs(point.y-y1)<RayTracer.EPSILON)
				&& (Math.abs(point.z-z0)<RayTracer.EPSILON || Math.abs(point.z-z1)<RayTracer.EPSILON);
	}

}
