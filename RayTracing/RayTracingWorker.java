package RayTracing;

import java.util.Random;

class RayTracingWorker implements Runnable {
    private Scene scene;
    private int imageWidth;
	private int bottomRow;
	private byte[] rgbData;
	private int topRow;

    RayTracingWorker(int bottomRow, int topRow, int imageWidth, Scene scene,
            byte[] rgbData) {
    	this.bottomRow = bottomRow;
        this.topRow = topRow;
    	this.scene = scene;
        this.imageWidth = imageWidth;
        this.rgbData = rgbData;
    }

    @Override
    public void run() {

        Color pixelColor;
        int progress = 0;

        for (int i = bottomRow; i < topRow; i++) {

        	int temp = (i*60)/imageWidth;
			if (temp>progress){
				progress = temp;
				System.out.print('.');
			}

        	for (int j = 0; j < imageWidth; j++) {
                pixelColor = getPixelColor(i, j);
                paintPixel(rgbData, i, j, pixelColor);
            }
        }
    }


    Color getPixelColor(int x, int y) {
		Ray ray = scene.camera.getRayByPixelCoordinate(x, y);
		return traceRay(ray, 0);
	}

	Color traceRay(Ray ray, int iteration) {
		Hit closestHit = getClosestHit(ray.moveOriginAlongRay(0.005), null);

		if (closestHit == null || iteration == scene.settings.maxRecursionLevel) {
			return scene.settings.background;
		}

		Color pixelColor = Color.BLACK;
		for (Light light : scene.lights) {
			Ray shadowRay = Ray.createRayByTwoPoints(
				light.position,
				closestHit.intersection);

			//light intensity
			double illumination = getIlluminationLevel(shadowRay, light, closestHit);
			Color lightIntensity = light.rgb.scale(illumination + (1-illumination)*(1-light.shadow));

			//color
			Color diffuse = getDiffuse(closestHit, shadowRay);
			Color specular = getSpecular(closestHit, shadowRay, light);


			//transparency
			Color transparency = Color.BLACK;
			float opacity = closestHit.getTransparency();

			if (opacity != 0) {
				Hit secondHit = getClosestHit(ray.moveOriginAlongRay(0.005), closestHit.shape);

				if (secondHit != null) {
					Ray transRay = new Ray(secondHit.intersection, ray.dir);
					transparency = traceRay(transRay, iteration + 1);
				} else {
					transparency = scene.settings.background;
				}
			}

			pixelColor = Color.sum(
				pixelColor,
				diffuse.add(specular).multiply(lightIntensity).scale(1 - opacity),
				transparency.scale(opacity)
			);
		}

		//reflection
		Vector reflection = ray.dir.getReflectionAroundNormal(closestHit.normal);
		Color reflectRGB = Color.BLACK;
		if (!closestHit.getReflectRGB().equals(Color.BLACK)){
			Ray reflectionRay = new Ray(closestHit.intersection, reflection);
			reflectRGB = closestHit.getReflectRGB().multiply(traceRay(reflectionRay, iteration + 1));
		}
		return pixelColor.add(reflectRGB);
	}

	Hit getClosestHit(Ray ray, Shape3D ignore) {
		Hit closestHit = null;
		double minDist = Double.MAX_VALUE;

		for (Shape3D shape : scene.shapes) {
			Hit hit = shape.getHit(ray);

			if (hit != null && hit.dist < minDist) {
				if (ignore == null || ignore != hit.shape) {
					closestHit = hit;
					minDist = hit.dist;
				}
			}
		}

		return closestHit;
	}

	boolean isOccluded(Ray shadowRay, Hit hit) {
		Hit closestHit = getClosestHit(shadowRay, null);

		if (closestHit == null)
			return true;
		if (closestHit.shape != hit.shape)
			return true;
		return closestHit.dist*closestHit.dist < hit.intersection.distSquared(shadowRay.p0)-0.000005;
	}

	double getIlluminationLevel(Ray shadowRay, Light light, Hit hit){
		Vector[] grid = getLightGrid(shadowRay, light);
		int sum=0;
		for (int i=0; i<grid.length; i++){
			Ray ray = Ray.createRayByTwoPoints(grid[i], hit.intersection);
			if (!isOccluded(ray, hit))
				sum++;
		}
		return (double)sum/grid.length;
	}

	Vector[] getLightGrid(Ray ray, Light light){
		//construct rectangle
		Plane plane = ray.getPerpendicularPlaneAtOrigion();
		Vector edge1 = plane.getArbitraryDirection();
		Vector edge2 = edge1.cross(plane.normal);
		Vector vertex = Vector.sum(ray.p0, edge1.toLength(-light.width/2), edge2.toLength(-light.width/2));

		int shadowRaysNum = scene.settings.shadowRaysNum;
		Vector[] grid = new Vector[shadowRaysNum*shadowRaysNum];
		double tileWidth = light.width/shadowRaysNum;
		Random r = new Random();
		for (int i=0; i<shadowRaysNum; i++){
			for (int j=0; j<shadowRaysNum; j++){
				double alpha = tileWidth*(i+r.nextDouble());
				double beta = tileWidth*(j+r.nextDouble());
				grid[i*shadowRaysNum+j] = Vector.sum(vertex, edge1.toLength(alpha), edge2.toLength(beta));
			}
		}

		return grid;
	}

	Color getDiffuse(Hit hit, Ray shadowRay) {
		double cosOfAngle = hit.normal.getCosOfAngle(shadowRay.dir.reverse());

		if (cosOfAngle < 0)
			return Color.BLACK;
		return hit.getDiffuse().scale(cosOfAngle);
	}

	Color getSpecular(Hit hit, Ray shadowray, Light light){
		Vector reflection = shadowray.dir.getReflectionAroundNormal(hit.normal);
		Vector viewDirection = scene.camera.position.subtract(hit.intersection);
		double cosOfAngle = viewDirection.getCosOfAngle(reflection);

		if (cosOfAngle < 0 || hit.getSpecular().equals(Color.BLACK))
			return Color.BLACK;
		return hit.getSpecular().scale(light.spec * Math.pow(cosOfAngle, hit.getPhong()));
	}


	void paintPixel(byte[] rgbData, int x, int y, Color pixelColor) {
		rgbData[(y * this.imageWidth + x) * 3] = pixelColor.getRByte();
		rgbData[(y * this.imageWidth + x) * 3 + 1] = pixelColor.getGByte();
		rgbData[(y * this.imageWidth + x) * 3 + 2] = pixelColor.getBByte();
	}

}
