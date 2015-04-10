package RayTracing;

import java.util.List;

public class Plane extends Shape3D{
	final Vector normal;
	final double offset;

	Plane(double nx, double ny, double nz, double offset, Material material) {
		super(material);

		normal = (new Vector(nx, ny, nz)).normalize();
		this.offset = offset;
	}

	Plane(Vector normal, double offset, Material material) {
		super(material);
		
		this.normal = normal.isNormalized() ? normal : normal.normalize();
		this.offset = offset;
	}

	@Override
	List<Vector> getIntersections(Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	Vector getClosestIntersectionWithRay(Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}
}