package RayTracing;
public class Color {
	final Vector rgb;
	public static final Color BLACK = new Color(0, 0, 0);

	Color(double r, double g, double b) {
		rgb = new Vector(Math.min(1, r), Math.min(1, g), Math.min(1, b));
	}

	Color add(Color other) {
		Vector tmp = rgb.add(other.rgb);

		return new Color(tmp.x, tmp.y, tmp.z);
	}

	double getR() { return rgb.x; }
	double getG() {
		return rgb.y;
	}
	double getB() { return rgb.z; }

	Color multiply(Color other) {
		Vector product = rgb.pointwiseMultiply(other.rgb);

		return new Color(product.x, product.y, product.z);
	}

	static Color sum(Color... colors) {
		Color result = Color.BLACK;

		for (Color color : colors) {
			result = result.add(color);
		}

		return result;
	}

	Color scale(double a) {
		return new Color(a*getR(), a*getG(), a*getB());
	}
	
	int getRGB(){
		return (int)(rgb.x*255)<<16 | (int)(rgb.y*255)<<8 | (int)(rgb.z*255);
	}
	
	@Override
	public boolean equals(Object o){
		if (!(o instanceof Color))
			return false;
		Color other = (Color) o;
		return rgb.equals(other.rgb);
	}

}