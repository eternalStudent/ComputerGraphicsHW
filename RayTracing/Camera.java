package RayTracing;

public class Camera {
	final Vector position;

	private final Vector screenNormal;
	private final double screenHeight;

	private final int imageHeight;
	private final int imageWidth;

	private final Vector xAxis;
	private final Vector yAxis;

	private final Vector walkingDistance;

	Camera(
		double px, double py, double pz,
		double lx, double ly, double lz,
		double ux, double uy, double uz,
		double screenDistance, double screenWidth,
		int imageWidth, int imageHeight) {

		this.screenHeight = (screenWidth*imageHeight) / (double)imageWidth;
		this.imageHeight = imageHeight;
		this.imageWidth = imageWidth;
		this.position = new Vector(px, py, pz);

		//constructing screenNormal & screenPlane
		Vector lookAt = new Vector(lx, ly, lz);
		screenNormal = lookAt.subtract(position).normalize();
		Vector screenCenter = position.add(screenNormal.toLength(screenDistance));
		Plane screenPlane = screenNormal.getPerpendicularPlaneAtPoint(screenCenter);

		//constructing axes
		Vector up = new Vector(ux, -uy, uz);
		up = up.projectOntoPlane(screenPlane);
		xAxis = screenNormal.cross(up).toLength(screenWidth / 2);
		yAxis = up.toLength(screenHeight / 2);

		walkingDistance = screenNormal.toLength(screenDistance);
	}

	public Ray getRayByPixelCoordinate(double x, double y) {
		// Alpha and Beta are normalized to the range -1 to 1
		double alpha = (2*x - imageWidth) / (double) imageWidth;
		double beta = (2*y - imageHeight) / (double) imageHeight;

		Vector vectOfPixel = Vector.sum(walkingDistance,
			xAxis.scale(alpha), yAxis.scale(beta));

		return new Ray(position, vectOfPixel);
	}
}
