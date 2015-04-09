package RayTracing;

public class Camera {
	final Vector position;
	final Vector lookAt;
	final Vector up;
	final float screenDistance;
	final float screenWidth;

	Camera(
		float px, float py, float pz,
		float lx, float ly, float lz,
		float ux, float uy, float uz,
		float screenDistance, float screenWidth) {

		this.position = new Vector(px, py, pz);
		this.lookAt = new Vector(lx, ly, lz);
		this.up = new Vector(ux, uy, uz);
		this.screenDistance = screenDistance;
		this.screenWidth = screenWidth;
	}
}