package RayTracing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class RayTracingWorker implements Runnable {
	private static boolean ANTI_ALIASING = false;
    private final RayTracer tracer; 
    private final int bottomRow;
	private final int topRow;

    RayTracingWorker(int bottomRow, int topRow, RayTracer tracer) {
    	this.tracer     = tracer;
    	this.bottomRow  = bottomRow;
        this.topRow     = topRow;
    }

    @Override
    public void run() {
        int progress = 0;
        for (int i = bottomRow; i < topRow; i++) {
        	int temp = (i*60)/tracer.imageWidth;
			if (temp>progress){
				progress = temp;
				System.out.print('.');
			}

        	for (int j = 0; j < tracer.imageWidth; j++) {
                Color pixelColor = getPixelColor(i, j);
                tracer.paintPixel(i, j, pixelColor);
            }
        }
    }

    private Color getPixelColor(int x, int y) {
		if (!RayTracingWorker.ANTI_ALIASING) {
			Ray ray = tracer.getCamera().getRayByPixelCoordinate(x, y);
			return traceRay(ray, 0);
		}
		else {
			int multiplier = 2;
			Random r = new Random();
			double red = 0;
			double green = 0;
			double blue = 0;

			for (int i = 0; i < multiplier; i++) {
				double randX = x + r.nextDouble();
				double randY = y + r.nextDouble();
				Ray ray = tracer.getCamera().getRayByPixelCoordinate(randX, randY);
				Color color = traceRay(ray, 0);
				red += color.getR();
				green += color.getG();
				blue += color.getB();
			}
			red /= multiplier;
			green /= multiplier;
			blue /= multiplier;

			return new Color(red, green, blue);
		}
	}

	private Color traceRay(Ray ray, int iteration) {
		Hit closestHit = getClosestHit(ray.moveOriginAlongRay(RayTracer.EPSILON));

		if (closestHit == null || iteration == tracer.getSettings().maxRecursionLevel) {
			return tracer.getSettings().background;
		}

		Color baseColor = Color.BLACK;
		for (Light light : tracer.scene.lights) {
			Ray shadowRay = Ray.createRayByTwoPoints(
				light.position,
				closestHit.intersection);

			double illumination = getIlluminationLevel(shadowRay, light, closestHit.intersection);
			double occlusion    = 1 - illumination;
			double lightIntensity = 1-light.shadow;

			Color lightColor = light.color;
			Color diffuse    = getDiffuse(closestHit, shadowRay);
			Color specular   = getSpecular(closestHit, shadowRay, light, ray);

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
		double transparency = closestHit.getTransparency();
		double opacity = 1-transparency;
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

		for (Primitive primitive : tracer.scene.primitives) {
			Hit hit = primitive.getHit(ray);

			if (hit != null && hit.dist < minDist) {
				closestHit = hit;
				minDist = hit.dist;
			}
		}

		return closestHit;
	}

	private double getIlluminationLevel(Ray shadowRay, Light light, Vector intersection){
		Vector[] grid = getLightGrid(shadowRay, light);
		double sumExposure=0;
		for (int i=0; i<grid.length; i++){
			Ray ray = Ray.createRayByTwoPoints(grid[i], intersection);
			sumExposure += getExposureLevel(ray, intersection);
		}
		return sumExposure/(double)grid.length;
	}

	private Vector[] getLightGrid(Ray ray, Light light){
		//construct rectangle
		Plane plane = ray.getPerpendicularPlaneAtOrigion();
		Vector edge1 = plane.getRandomDirection();
		Vector edge2 = edge1.cross(plane.getNormalAtSurfacePoint(null));
		Vector vertex = Vector.sum(ray.p0, edge1.toLength(-light.width/2), edge2.toLength(-light.width/2));

		int shadowRaysNum = tracer.getSettings().shadowRaysNum;
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

	private double getExposureLevel(Ray shadowRay, Vector intersection) {
		double exposure = 1;
		List<Hit> hits = getOrderedHits(shadowRay);
		for (Hit hit: hits) {
			if (hit.dist*hit.dist > intersection.distSquared(shadowRay.p0)-RayTracer.EPSILON || exposure == 0)
				break;
			exposure *= hit.getTransparency();
		}
		return exposure;
	}

	private List<Hit> getOrderedHits(Ray shadowRay){
		List<Hit> hits = new ArrayList<Hit>();
		for (Primitive primitive : tracer.scene.primitives) {
			Hit hit = primitive.getHit(shadowRay);
			if (hit != null){
				hits.add(hit);
			}
		}
		Collections.sort(hits);
		return hits;
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

		if (cosOfAngle < 0 || hit.getSpecularColor().equals(Color.BLACK) || getExposureLevel(shadowRay, hit.intersection) < 1)
			return Color.BLACK;
		return hit.getSpecularColor().scale(light.spec * Math.pow(cosOfAngle, hit.getPhong()));
	}

}
