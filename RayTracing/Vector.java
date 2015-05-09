package RayTracing;

public class Vector {
	static final Vector ZERO = new Vector(0, 0, 0);
	static final boolean DEGREES = true;
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

	double dot(Vector other) {
		return (x*other.x + y*other.y + z*other.z);
	}

	Vector cross(Vector other) {
		return new Vector(
			y*other.z - z*other.y,
			z*other.x - x*other.z,
			x*other.y - y*other.x
			);
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

	Vector normalize(){
		return scale(1/norm());
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Vector))
			return false;
		Vector other = (Vector) o;
		return ( Math.abs(x-other.x)<RayTracer.EPSILON
			  && Math.abs(y-other.y)<RayTracer.EPSILON
			  && Math.abs(z-other.z)<RayTracer.EPSILON);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	Vector projectOntoPlane(Plane plane) {
		return subtract(projectOntoVector(plane.getNormalAtSurfacePoint(null)));
	}

	Vector projectOntoVector(Vector other) {
		return other.toLength(dot(other));
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
		return new Plane(this, dot(point));
	}

	private Vector rotateAroundX(double t){
		if (DEGREES)
			t = (t*Math.PI)/180;
		double cost = Math.cos(t);
		double sint = Math.sin(t);
		return new Vector(x, cost*y-sint*z, sint*y+cost*z);
	}

	private Vector rotateAroundY(double t){
		if (DEGREES)
			t = (t*Math.PI)/180;
		double cost = Math.cos(t);
		double sint = Math.sin(t);
		return new Vector(cost*x+sint*z, y, -sint*x+cost*z);
	}

	private Vector rotateAroundZ(double t){
		if (DEGREES)
			t = (t*Math.PI)/180;
		double cost = Math.cos(t);
		double sint = Math.sin(t);
		return new Vector(cost*x-sint*y, sint*x+cost*y, z);
	}

	Vector rotate(Vector r){
		return rotateAroundX(r.x).rotateAroundY(r.y).rotateAroundZ(r.z);
	}

	Vector reverseRotate(Vector r){
		r = r.reverse();
		return rotateAroundZ(r.z).rotateAroundY(r.y).rotateAroundX(r.x);
	}

}
