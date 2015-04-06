package RayTracing;

public class Vector {
	final int x;
	final int y;
	final int z;

	Vector(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	Vector add(Vector other) {
		return new Vector(x + other.x, y + other.y, z + other.z);
	}

	static Vector add(Vector v1, Vector v2) {
		return v1.add(v2);
	}

	int dot(Vector other) {
		return (x*other.x + y*other.y + z*other.z);
	}

	static int dot(Vector v1, Vector v2) {
		return v1.dot(v2);
	}

	Vector cross(Vector other) {
		return new Vector(
			y*other.z - z*other.y,
			z*other.x - x*other.z,
			x*other.y - y*other.x);
	}

	static Vector cross(Vector v1, Vector v2) {
		return v1.cross(v2);
	}

	int normSquared() {
		return x*x + y*y + z*z;
	}

	double norm() {
		return Math.sqrt(normSquared());
	}

	Vector scale(int a) {
		return new Vector(a*x, a*y, a*z);
	}

	boolean isNormalized() {
		return normSquared() == 1;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Vector))
			return false;
		
		Vector other = (Vector) o;
		return ( (x == other.x) && (y == other.y) && (z == other.z) );
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

}
