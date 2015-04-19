package RayTracing;

public class Camera {
	final Vector position;
	
	private final Vector screenDirection;
	private final double screenHeight;

	private final int imageHeight;
	private final int imageWidth;

	private final Vector xAxis;
	private final Vector yAxis;

	Camera(
		float px, float py, float pz,
		float lx, float ly, float lz,
		float ux, float uy, float uz,
		double screenDistance, double screenWidth,
		int imageWidth, int imageHeight) {

		this.position = new Vector(px, py, pz);
		Vector lookAt = new Vector(lx, ly, lz);

		screenHeight = screenWidth*(imageHeight / imageWidth);

		this.imageHeight = imageHeight;
		this.imageWidth = imageWidth;

		screenDirection = lookAt.subtract(position);
		Vector screenCenter = position.add(screenDirection.toLength(screenDistance));

		// This is the plane that the direction vector is perpendicular to.
		Plane screenPlane = screenDirection.getPerpendicularPlaneAtPoint(screenCenter);

		Vector up = new Vector(ux, -uy, uz);
		up = up.projectOntoPlane(screenPlane);

		xAxis = screenDirection.cross(up).toLength(screenWidth / 2);
		yAxis = up.toLength(screenHeight / 2);
	}

	public Ray getRayByPixelCoordinate(int x, int y) {
		// Alpha and Beta are normalized to the range -1 to 1
		double alpha = (2*x - imageWidth) / (double) imageWidth;
		double beta = (2*y - imageHeight) / (double) imageHeight;

		Vector vectOfPixel = Vector.sum(screenDirection.normalize(),
			xAxis.scale(alpha), yAxis.scale(beta));

		return new Ray(position, vectOfPixel);
	}
}