package RayTracing;

public class Ray {
	
	public final Vector dir;
	public final Vector p0;
	
	public Ray(Vector dir, Vector p0){
		this.dir = dir;
		this.p0 = p0;
	}
	
	public boolean contains(Vector p){
		double dx = p.x-p0.x;
		double scale = dx/dir.x;
		return p0.add(dir.scale(scale)).equals(p);
	}

}
