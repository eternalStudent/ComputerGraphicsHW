package RayTracing;

public class RGB {
	final byte[] rgb;

	RGB(byte r, byte g, byte b) {
		byte[] temp = {r, g, b};
		rgb = temp;
	}

	RGB add(RGB other) {
		return new RGB(
			(byte) (rgb[0] + other.rgb[0]),
			(byte) (rgb[1] + other.rgb[0]),
			(byte) (rgb[2] + other.rgb[0]));
	}

	RGB multiply(RGB other) {
		return new RGB(
			(byte) (rgb[0]*other.rgb[0]),
			(byte) (rgb[1]*other.rgb[1]),
			(byte) (rgb[2]*other.rgb[2]));
	}

	RGB scale(double s) {
		return new RGB (
			(byte) Math.round(rgb[0]*s),
			(byte) Math.round(rgb[1]*s),
			(byte) Math.round(rgb[2]*s));
	}
}