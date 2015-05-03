package RayTracing;

public abstract class Shape3D {

	abstract double getHitDistance(Ray ray);
	abstract Vector getNormalAtSurfacePoint(Vector point);
	
}
