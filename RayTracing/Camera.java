package RayTracing;

public class Camera {
	final Vector position;
	final Vector lookAt;
	final Vector up;
	final float screenDistance;
	final float screenWidth;

	Camera(
		int px, int py, int pz,
		int lx, int ly, int lz,
		int ux, int uy, int uz,
		int screenDistance, int screenWidth) {

		this.position = new Vector(px, py, pz);
		this.lookAt = new Vector(lx, ly, lz);
		this.up = new Vector(ux, uy, uz);
		this.screenDistance = screenDistance;
		this.screenWidth = screenWidth;
	}
}