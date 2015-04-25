package RayTracing;

public class Light {
	final Vector position;
	final Color rgb;
	final double spec;
	final double shadow;
	final double width;

	Light(
		double px, double py, double pz,
		double r, double g, double b,
		double spec, double shadow, double width) {

		this.position = new Vector(px, py, pz);
		this.rgb = new Color(r, g, b);
		this.shadow = shadow;
		this.width = width;
		this.spec = spec;
	}
}