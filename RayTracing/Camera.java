package RayTracing;

public class Camera {
	final Vector position;

	private final Vector screenNormal;
	private double screenWidth;

	private int imageHeight;
	private int imageWidth;
	
	private Vector xAxis;
	private Vector yAxis;
	private final Vector up;
	private final Vector walkingDistance;

	Camera(
		double px, double py, double pz,
		double lx, double ly, double lz,
		double ux, double uy, double uz,
		double screenDistance, double screenWidth) {

		this.position = new Vector(px, py, pz);
		this.screenWidth = screenWidth;

		//constructing up vector && walkingDistance
		Vector lookAt = new Vector(lx, ly, lz);
		screenNormal = lookAt.subtract(position).normalize();
		walkingDistance = screenNormal.toLength(screenDistance);
		Vector screenCenter = position.add(screenNormal.toLength(screenDistance));
		Plane screenPlane = screenNormal.getPerpendicularPlaneAtPoint(screenCenter);
		this.up = new Vector(ux, -uy, uz).projectOntoPlane(screenPlane);
	}
	
	public void build(int imageWidth, int imageHeight){
		this.imageHeight = imageHeight;
		this.imageWidth = imageWidth;
		double screenHeight = (screenWidth*this.imageHeight) / (double)this.imageWidth;
		
		xAxis = screenNormal.cross(up).toLength(screenWidth / 2);
		yAxis = up.toLength(screenHeight / 2);
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
