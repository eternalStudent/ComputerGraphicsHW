package RayTracing;

public class Material {
	final RGB diffuse;
	final RGB specular;
	final RGB reflection;
	final float phong;
	final float trans;

	Material(
		double dr, double dg, double db,
		double sr, double sg, double sb,
		double rr, double rg, double rb,
		float phong,
		float trans) {

		this.diffuse = new RGB(dr, dg, db);
		this.specular = new RGB(sr, sg, sb);
		this.reflection = new RGB(rr, rg, rb);
		this.phong = phong;
		this.trans = trans;
	}
}