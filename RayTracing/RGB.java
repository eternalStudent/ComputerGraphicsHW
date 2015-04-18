package RayTracing;
public class RGB {
	final Vector rgb;
	public static final RGB BLACK = new RGB(0, 0, 0);

	RGB(double r, double g, double b) {
		rgb = new Vector(Math.min(1, r), Math.min(1, g), Math.min(1, b));
	}

	RGB add(RGB other) {
		Vector tmp = rgb.add(other.rgb);

		return new RGB(tmp.x, tmp.y, tmp.z);
	}

	double getR() {
		return rgb.x;
	}
	byte getRByte() {
		return (byte) Math.round(255*rgb.x);
	}
	double getG() {
		return rgb.y;
	}
	byte getGByte() {
		return (byte) Math.round(255*rgb.y);
	}
	double getB() {
		return rgb.z;
	}
	byte getBByte() {
		return (byte) Math.round(255*rgb.z);
	}

	RGB multiply(RGB other) {
		Vector product = rgb.pointwiseMultiply(other.rgb);

		return new RGB(product.x, product.y, product.z);
	}

	static RGB sum(RGB... colors) {
		RGB result = RGB.BLACK;

		for (RGB color : colors) {
			result = result.add(color);
		}

		return result;
	}

	RGB scale(double a) {
		return new RGB(a*getR(), a*getG(), a*getB());
	}
	
	@Override
	public boolean equals(Object o){
		if (!(o instanceof RGB))
			return false;
		RGB other = (RGB) o;
		return rgb.equals(other.rgb);
	}

}