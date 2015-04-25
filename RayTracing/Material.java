package RayTracing;

public class Material {
	final Color diffuse;
	final Color specular;
	final Color reflection;
	final float phong;
	final float trans;

	Material(
		double dr, double dg, double db,
		double sr, double sg, double sb,
		double rr, double rg, double rb,
		float phong,
		float trans) {

		this.diffuse = new Color(dr, dg, db);
		this.specular = new Color(sr, sg, sb);
		this.reflection = new Color(rr, rg, rb);
		this.phong = phong;
		this.trans = trans;
	}
}