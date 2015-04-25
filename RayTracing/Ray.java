package RayTracing;

public class Ray {

	public final Vector dir;
	public final Vector p0;

	public Ray(Vector p0, Vector dir){
		if (dir.equals(new Vector(0, 0, 0))) {
			throw new IllegalArgumentException("Direction can't be the zero vector.\n");
		}

		this.dir = dir.normalize();
		this.p0 = p0;
	}

	public Vector getPointAlongRay(double dist) {
		return p0.add(dir.toLength(dist));
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Ray))
			return false;
		Ray other = (Ray) o;
		return p0.equals(other.p0) && dir.equals(other.dir);
	}

	@Override
	public String toString() {
		return "Ray origin: " + p0 + ", Ray direction: " + dir;
	}

	public static Ray createRayByTwoPoints(Vector v1, Vector v2) {
		return new Ray(v1, v2.subtract(v1));
	}

	Ray moveOriginAlongRay(double epsilon) {
		return new Ray(getPointAlongRay(epsilon), dir);
	}

	Plane getPerpendicularPlaneAtOrigion(){
		return dir.getPerpendicularPlaneAtPoint(p0);
	}
}
