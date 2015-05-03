package RayTracing;

public abstract class Shape3D {

	abstract double getHit(Ray ray);
	abstract Vector getNormalAtSurfacePoint(Vector point);
	
}
