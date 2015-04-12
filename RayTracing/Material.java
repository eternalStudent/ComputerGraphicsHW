package RayTracing;

public class Material {
	RGB diffuse;
	RGB specular;
	RGB reflection;
	final float phong;
	final float trans;

	Material(
		byte dr, byte dg, byte db,
		byte sr, byte sg, byte sb,
		byte rr, byte rg, byte rb,
		float phong,
		float trans) {

		this.diffuse = new RGB(dr, dg, db);
		this.specular = new RGB(sr, sg, sb);
		this.reflection = new RGB(rr, rg, rb);
		this.phong = phong;
		this.trans = trans;
	}
}