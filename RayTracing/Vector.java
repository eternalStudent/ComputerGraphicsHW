package RayTracing;

public class Vector {
	static final Vector ZERO = new Vector(0, 0, 0);
	final double x;
	final double y;
	final double z;

	Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	double distSquared(Vector other) {
		return (x - other.x)*(x - other.x) + (y - other.y)*(y - other.y) + (z - other.z)*(z - other.z);
	}

	Vector reverse(){
		return new Vector(-x, -y, -z);
	}

	Vector add(Vector other) {
		return new Vector(x + other.x, y + other.y, z + other.z);
	}

	static Vector add(Vector v1, Vector v2) {
		return v1.add(v2);
	}

	static Vector sum(Vector... vectors) {
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;

		for (Vector vect : vectors) {
			x += vect.x;
			y += vect.y;
			z += vect.z;
		}

		return new Vector(x, y, z);
	}

	Vector subtract(Vector other) {
		return new Vector(x - other.x, y - other.y, z - other.z);
	}

	static Vector subtract(Vector v1, Vector v2) {
		return v1.subtract(v2);
	}

	double dot(Vector other) {
		return (x*other.x + y*other.y + z*other.z);
	}

	static double dot(Vector v1, Vector v2) {
		return v1.dot(v2);
	}

	Vector cross(Vector other) {
		return new Vector(
			y*other.z - z*other.y,
			z*other.x - x*other.z,
			x*other.y - y*other.x
			);
	}

	static Vector cross(Vector v1, Vector v2) {
		return v1.cross(v2);
	}

	double normSquared() {
		return x*x + y*y + z*z;
	}

	double norm() {
		return Math.sqrt(normSquared());
	}

	Vector scale(double a) {
		return new Vector(a*x, a*y, a*z);
	}

	Vector toLength(double length) {
		return scale(length / norm());
	}

	boolean isNormalized() {
		return normSquared() == 1;
	}

	Vector normalize(){
		return scale(1/norm());
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Vector))
			return false;
		Vector other = (Vector) o;
		return ( Math.abs(x-other.x)<0.0005 && Math.abs(y-other.y)<0.0005 && Math.abs(z-other.z)<0.0005 );
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	Vector projectOntoPlane(Plane plane) {
		return subtract(projectOntoVector(plane.normal));
	}

	Vector projectOntoVector(Vector other) {
		return toLength(dot(other));
	}

	Vector pointwiseMultiply(Vector other) {
		return new Vector(x*other.x, y*other.y, z*other.z);
	}

	Vector getReflectionAroundNormal(Vector normal) {
		normal = normal.normalize();
		return subtract(normal.scale(2*dot(normal)));
	}

	double getCosOfAngle(Vector other) {
		return normalize().dot(other.normalize());
	}
	
	Plane getPerpendicularPlaneAtPoint(Vector point){
		return new Plane(this, dot(point), null);
	}

}
