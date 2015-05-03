package RayTracing;

public class Primitive {
	
	final Shape3D shape;
	final Material material;
	
	public Primitive(Shape3D shape, Material material){
		this.shape = shape;
		this.material = material;
	}
	
	public Hit getHit(Ray ray){
		double dist = shape.getHit(ray);
		if (dist == -1)
			return null;
		return new Hit(this, dist, ray);
	}

}
