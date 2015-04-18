package RayTracing;

public class Camera {
	final Vector position;
	
	private final Vector directionVect;
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

		directionVect = lookAt.subtract(position).toLength(screenDistance);

		// This is the plane that the direction vector is perpendicular to.
		Plane directionPlane = position.getPerpendicularPlaneAtPoint(directionVect);

		Vector up = new Vector(ux, -uy, uz);
		up = up.projectOntoPlane(directionPlane);

		xAxis = directionVect.cross(up).toLength(screenWidth / 2);
		yAxis = up.toLength(screenHeight / 2);
	}

	public Ray getRayByPixelCoordinate(int x, int y) {
		// Alpha and Beta are normalized to the range -1 to 1
		double alpha = (2*x - imageWidth) / (double) imageWidth;
		double beta = (2*y - imageHeight) / (double) imageHeight;

		Vector vectOfPixel = Vector.sum(directionVect.normalize(),
			xAxis.scale(alpha), yAxis.scale(beta));

		return new Ray(position, vectOfPixel);
	}
}