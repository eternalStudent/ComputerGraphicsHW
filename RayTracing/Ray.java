package RayTracing;

public class Ray {

	public final Vector dir;
	public final Vector p0;

	public Ray(Vector p0, Vector dir){
		if (dir.equals(new Vector(0, 0, 0))) {
			throw new IllegalArgumentException("Direction can't be the zero vector.\n");
		}

		this.dir = dir;
		this.p0 = p0;
	}

	public boolean contains(Vector p) {
		double dx = p.x-p0.x;
		if (dir.x ==0) {
			System.out.println("derp dir.x ==0");
		}
		double scale = dx/dir.x;
		return p0.add(dir.scale(scale)).equals(p);
	}

	public Vector getVectAlongRay(double dist) {
		return p0.add(dir.toLength(dist));
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Ray))
			return false;
		Ray other = (Ray) o;
		return ( p0.equals(other.p0) && dir.normalize().equals( other.dir.normalize() ) );
	}

	@Override
	public String toString() {
		return "Ray origin: " + p0 + ", Ray direction: " + dir;
	}

	public static Ray createRayByTwoVects(Vector v1, Vector v2) {
		return new Ray(v1, v2.subtract(v1));
	}
}
