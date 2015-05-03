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


    private Color getPixelColor(int x, int y) {
		Ray ray = scene.camera.getRayByPixelCoordinate(x, y);
		return traceRay(ray, 0);
	}

	private Color traceRay(Ray ray, int iteration) {
		Hit closestHit = getClosestHit(ray.moveOriginAlongRay(0.005));

		if (closestHit == null || iteration == scene.settings.maxRecursionLevel) {
			return scene.settings.background;
		}

		Color baseColor = Color.BLACK;
		for (Light light : scene.lights) {
			Ray shadowRay = Ray.createRayByTwoPoints(
				light.position,
				closestHit.intersection);

			double illumination = getIlluminationLevel(shadowRay, light, closestHit);
			double occlusion = 1 - illumination;
			double lightIntensity = 1-light.shadow;
			Color lightColor = light.color;
			Color diffuse = getDiffuse(closestHit, shadowRay);
			Color specular = getSpecular(closestHit, shadowRay, light, ray);

			baseColor = baseColor.add(diffuse.add(specular).
					multiply(lightColor).
					scale(illumination+occlusion*lightIntensity));
		}
		
		//reflection
		Vector reflection = ray.dir.getReflectionAroundNormal(closestHit.normal);
		Color reflectionColor = Color.BLACK;
		if (!closestHit.getReflectColor().equals(Color.BLACK)){
			Ray reflectionRay = new Ray(closestHit.intersection, reflection);					
			reflectionColor = closestHit.getReflectColor().multiply(traceRay(reflectionRay, iteration + 1));
		}
		
		//transparency
		Color transparencyColor = Color.BLACK;
		float transparency = closestHit.getTransparency();
		float opacity = 1-transparency;
		if (transparency != 0) {
			Ray transRay = new Ray(closestHit.intersection, ray.dir);
			transparencyColor = traceRay(transRay, iteration + 1);
		}
		
		return Color.sum(
				transparencyColor.scale(transparency), 
				baseColor.scale(opacity), 
				reflectionColor
				);
	}

	private Hit getClosestHit(Ray ray) {
		Hit closestHit = null;
		double minDist = Double.MAX_VALUE;

		for (Primitive primitive : scene.primitives) {
			Hit hit = primitive.getHit(ray);

			if (hit != null && hit.dist < minDist) {
					closestHit = hit;
					minDist = hit.dist;
			}
		}

		return closestHit;
	}
	
	private double getIlluminationLevel(Ray shadowRay, Light light, Hit hit){
		Vector[] grid = getLightGrid(shadowRay, light);
		double sumExposure=0;
		for (int i=0; i<grid.length; i++){
			Ray ray = Ray.createRayByTwoPoints(grid[i], hit.intersection);
			sumExposure += getExposureLevel(ray, hit);
		}
		return sumExposure/(double)grid.length;
	}

	private Vector[] getLightGrid(Ray ray, Light light){
		//construct rectangle
		Plane plane = ray.getPerpendicularPlaneAtOrigion();
		Vector edge1 = plane.getRandomDirection();
		Vector edge2 = edge1.cross(plane.getNormalAtSurfacePoint(null));
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

	private double getExposureLevel(Ray shadowRay, Hit finalHit) {
		double exposure = 1;
		for (Primitive primitive : scene.primitives) {
			Hit hit = primitive.getHit(shadowRay);
			if (hit != null){
				if (hit.primitive == finalHit.primitive)
					break;
				exposure *= hit.getTransparency();
			}	
		}
		return exposure;
	}
	
	private Color getDiffuse(Hit hit, Ray shadowRay) {
		double cosOfAngle = hit.normal.getCosOfAngle(shadowRay.dir.reverse());

		if (cosOfAngle < 0)
			return Color.BLACK;
		return hit.getDiffuseColor().scale(cosOfAngle);
	}

	private Color getSpecular(Hit hit, Ray shadowRay, Light light, Ray ray){
		Vector reflection = shadowRay.dir.getReflectionAroundNormal(hit.normal);
		Vector viewDirection = ray.dir.reverse();
		double cosOfAngle = viewDirection.getCosOfAngle(reflection);

		if (cosOfAngle < 0 || hit.getSpecularColor().equals(Color.BLACK))
			return Color.BLACK;
		return hit.getSpecularColor().scale(light.spec * Math.pow(cosOfAngle, hit.getPhong()));
	}


	private void paintPixel(byte[] rgbData, int x, int y, Color pixelColor) {
		rgbData[(y * this.imageWidth + x) * 3] = pixelColor.getRByte();
		rgbData[(y * this.imageWidth + x) * 3 + 1] = pixelColor.getGByte();
		rgbData[(y * this.imageWidth + x) * 3 + 2] = pixelColor.getBByte();
	}

}
