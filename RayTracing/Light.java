package RayTracing;

public class Light {
	final Vector position;
	final RGB rgb;
	final int spec;
	final int shadow;
	final int width;

	Light(
		int px, int py, int pz,
		byte r, byte g, byte b,
		int spec, int shadow, int width) {

		this.position = new Vector(px, py, pz);
		this.rgb = new RGB(r, g, b);
		this.shadow = shadow;
		this.width = width;
		this.spec = spec;
	}
}