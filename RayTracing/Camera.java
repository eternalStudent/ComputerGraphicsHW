package RayTracing;

public class Camera {
	final Vector position;
	
	private final Vector screenNormal;
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

		this.screenHeight = screenWidth*(imageHeight / imageWidth);
		this.imageHeight = imageHeight;
		this.imageWidth = imageWidth;
		this.position = new Vector(px, py, pz);
		
		//constructing screenNormal & screenPlane
		Vector lookAt = new Vector(lx, ly, lz);
		screenNormal = lookAt.subtract(position);
		Vector screenCenter = position.add(screenNormal.toLength(screenDistance));
		Plane screenPlane = screenNormal.getPerpendicularPlaneAtPoint(screenCenter);

		//constructing axes
		Vector up = new Vector(ux, -uy, uz);
		up = up.projectOntoPlane(screenPlane);
		xAxis = screenNormal.cross(up).toLength(screenWidth / 2);
		yAxis = up.toLength(screenHeight / 2);
	}

	public Ray getRayByPixelCoordinate(int x, int y) {
		// Alpha and Beta are normalized to the range -1 to 1
		double alpha = (2*x - imageWidth) / (double) imageWidth;
		double beta = (2*y - imageHeight) / (double) imageHeight;

		Vector vectOfPixel = Vector.sum(screenNormal.normalize(),
			xAxis.scale(alpha), yAxis.scale(beta));

		return new Ray(position, vectOfPixel);
	}
}