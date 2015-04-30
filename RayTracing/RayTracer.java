package RayTracing;

import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 *  Main class for ray tracing exercise.
 */
public class RayTracer {

	public int imageWidth;
	public int imageHeight;

	public Scene scene;
	/**
	 * Runs the ray tracer. Takes scene file, output image file and image size as input.
	 */
	public static void main(String[] args) {

		try {

			RayTracer tracer = new RayTracer();

                        // Default values:
			tracer.imageWidth = 500;
			tracer.imageHeight = 500;

			if (args.length < 2)
				throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

			String sceneFileName = args[0];
			String outputFileName = args[1];

			if (args.length > 3)
			{
				tracer.imageWidth = Integer.parseInt(args[2]);
				tracer.imageHeight = Integer.parseInt(args[3]);
			}


			// Parse scene file:
			tracer.parseScene(sceneFileName);

			// Render scene:
			tracer.renderScene(outputFileName);

//		} catch (IOException e) {
//			System.out.println(e.getMessage());
		} catch (RayTracerException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}


	}

	/**
	 * Parses the scene file and creates the scene. Change this function so it generates the required objects.
	 */
	public void parseScene(String sceneFileName) throws IOException, RayTracerException
	{
		FileReader fr = new FileReader(sceneFileName);

		BufferedReader r = new BufferedReader(fr);
		String line = null;
		int lineNum = 0;
		System.out.println("Started parsing scene file " + sceneFileName);

		scene = new Scene();

		while ((line = r.readLine()) != null)
		{
			line = line.trim();
			++lineNum;

			if (line.isEmpty() || (line.charAt(0) == '#'))
			{  // This line in the scene file is a comment
				continue;
			}
			else
			{
				String code = line.substring(0, 3).toLowerCase();
				// Split according to white space characters:
				String[] params = line.substring(3).trim().toLowerCase().split("\\s+");
				if (code.equals("cam"))
				{
                    scene.camera = new Camera(
                    	Float.parseFloat(params[0]),
						Float.parseFloat(params[1]),
						Float.parseFloat(params[2]),
						Float.parseFloat(params[3]),
						Float.parseFloat(params[4]),
						Float.parseFloat(params[5]),
						Float.parseFloat(params[6]),
						Float.parseFloat(params[7]),
						Float.parseFloat(params[8]),
						Double.parseDouble(params[9]),
						Double.parseDouble(params[10]),
						imageWidth, imageHeight);

					System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
				}
				else if (code.equals("set"))
				{
                    scene.settings = new SceneSettings(
                    	Double.parseDouble(params[0]),
						Double.parseDouble(params[1]),
						Double.parseDouble(params[2]),
						Integer.parseInt(params[3]),
						Integer.parseInt(params[4])
					);

					System.out.println(String.format("Parsed general settings (line %d)", lineNum));
				}
				else if (code.equals("mtl"))
				{
					scene.materials.add(new Material(
						Double.parseDouble(params[0]),
						Double.parseDouble(params[1]),
						Double.parseDouble(params[2]),
						Double.parseDouble(params[3]),
						Double.parseDouble(params[4]),
						Double.parseDouble(params[5]),
						Double.parseDouble(params[6]),
						Double.parseDouble(params[7]),
						Double.parseDouble(params[8]),
						Float.parseFloat(params[9]),
						Float.parseFloat(params[10]))
					);

					System.out.println(String.format("Parsed material (line %d)", lineNum));
				}
				else if (code.equals("sph"))
				{
	                scene.shapes.add(new Sphere(
	                	Float.parseFloat(params[0]),
	                	Float.parseFloat(params[1]),
		                Float.parseFloat(params[2]),
		                Float.parseFloat(params[3]),
		                scene.materials.get(Integer.parseInt(params[4]) - 1))
	                );

					System.out.println(String.format("Parsed sphere (line %d)", lineNum));
				}
				else if (code.equals("pln"))
				{
                    scene.shapes.add(new Plane(
	                	Double.parseDouble(params[0]),
	                	Double.parseDouble(params[1]),
		                Double.parseDouble(params[2]),
		                Double.parseDouble(params[3]),
		                scene.materials.get(Integer.parseInt(params[4]) - 1))
	                );

					System.out.println(String.format("Parsed plane (line %d)", lineNum));
				}
				else if (code.equals("box"))
				{
                                        // Add code here to parse box parameters

					System.out.println(String.format("Parsed box (line %d)", lineNum));
				}
				else if (code.equals("lgt"))
				{
                    scene.lights.add(new Light(
                    	Double.parseDouble(params[0]),
	                	Double.parseDouble(params[1]),
		                Double.parseDouble(params[2]),
                    	Double.parseDouble(params[3]),
						Double.parseDouble(params[4]),
						Double.parseDouble(params[5]),
						Double.parseDouble(params[6]),
						Double.parseDouble(params[7]),
						Double.parseDouble(params[8]))
                    );

					System.out.println(String.format("Parsed light (line %d)", lineNum));
				}
				else
				{
					System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
				}
			}
		}
		r.close();

                // It is recommended that you check here that the scene is valid,
                // for example camera settings and all necessary materials were defined.

		System.out.println("Finished parsing scene file " + sceneFileName);

	}

	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 */
	public void renderScene(String outputFileName)
	{
		long startTime = System.currentTimeMillis();

		// Create a byte array to hold the pixel data:
		byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];

		Color pixelColor;

		int progress = 0;
		System.out.print("rendering");
		for (int i = 0; i < imageWidth; i++) {

			int temp = (i*60)/imageWidth;
			if (temp>progress){
				progress = temp;
				System.out.print('.');
			}

			for (int j = 0; j < imageHeight; j++) {
				pixelColor = getPixelColor(i ,j);
				paintPixel(rgbData, i, j, pixelColor);
			}
		}
		System.out.println();
				// Put your ray tracing code here!
                //
                // Write pixel color values in RGB format to rgbData:
                // Pixel [x, y] red component is in rgbData[(y * this.imageWidth + x) * 3]
                //            green component is in rgbData[(y * this.imageWidth + x) * 3 + 1]
                //             blue component is in rgbData[(y * this.imageWidth + x) * 3 + 2]
                //
                // Each of the red, green and blue components should be a byte, i.e. 0-255


		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;

                // The time is measured for your own convenience, rendering speed will not affect your score
                // unless it is exceptionally slow (more than a couple of minutes)
		System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

                // This is already implemented, and should work without adding any code.
		saveImage(this.imageWidth, rgbData, outputFileName);

		System.out.println("Saved file " + outputFileName);

	}

	Color getPixelColor(int x, int y) {
		Ray ray = scene.camera.getRayByPixelCoordinate(x, y);
		return traceRay(ray, 0);
	}

	Color traceRay(Ray ray, int iteration) {
		Hit closestHit = getClosestHit(ray.moveOriginAlongRay(0.005));

		if (closestHit == null || iteration == scene.settings.maxRecursionLevel) {
			return scene.settings.background;
		}

		Color pixelColor = Color.BLACK;
		for (Light light : scene.lights) {
			Ray shadowRay = Ray.createRayByTwoPoints(
				light.position,
				closestHit.intersection);
			
			//reflection
			Vector reflection = ray.dir.getReflectionAroundNormal(closestHit.normal);
			Color reflectRGB = Color.BLACK;
			if (!closestHit.getReflectRGB().equals(Color.BLACK)){
				Ray reflectionRay = new Ray(closestHit.intersection, reflection);
				reflectRGB = closestHit.getReflectRGB().multiply(traceRay(reflectionRay, iteration + 1));
			}
			
			//light intensity
			double illumination = getIlluminationLevel(shadowRay, light, closestHit);
			Color lightIntensity = light.rgb.scale(illumination).add(light.rgb.scale((1-illumination)*(1-light.shadow)));

			//color
			Color diffuse = getDiffuse(closestHit, shadowRay);
			Color specular = getSpecular(closestHit, reflection, light);
			pixelColor = Color.sum(
					specular.add(diffuse).multiply(lightIntensity),
					pixelColor, reflectRGB);
		}

		return pixelColor;
	}

	Hit getClosestHit(Ray ray) {
		Hit closestHit = null;
		double minDist = Double.MAX_VALUE;

		for (Shape3D shape : scene.shapes) {
			Hit hit = shape.getHit(ray);

			if (hit != null && hit.dist < minDist) {
				closestHit = hit;
				minDist = hit.dist;
			}
		}

		return closestHit;
	}

	boolean isOccluded(Ray shadowRay, Hit hit) {
		Hit closestHit = getClosestHit(shadowRay);

		if (closestHit == null)
			return true;
		if (closestHit.shape != hit.shape)
			return true;
		return closestHit.dist*closestHit.dist < hit.intersection.distSquared(shadowRay.p0)-0.05;
	}

	double getIlluminationLevel(Ray shadowRay, Light light, Hit hit){
		Vector[] grid = getLightGrid(shadowRay, light);
		int sum=0;
		for (int i=0; i<grid.length; i++){
			Ray ray = Ray.createRayByTwoPoints(grid[i], hit.intersection);
			if (!isOccluded(ray, hit))
				sum++;
		}
		return (double)sum/(double)grid.length;
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

	Color getSpecular(Hit hit, Vector reflection, Light light){
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

	//////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////

	/*
	 * Saves RGB data as an image in png format to the specified location.
	 */
	public static void saveImage(int width, byte[] rgbData, String fileName)
	{
		try {

			BufferedImage image = bytes2RGB(width, rgbData);
			ImageIO.write(image, "png", new File(fileName));

		} catch (IOException e) {
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}

	}

	/*
	 * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
	 */
	public static BufferedImage bytes2RGB(int width, byte[] buffer) {
	    int height = buffer.length / width / 3;
	    ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	    ColorModel cm = new ComponentColorModel(cs, false, false,
	            Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    SampleModel sm = cm.createCompatibleSampleModel(width, height);
	    DataBufferByte db = new DataBufferByte(buffer, width * height);
	    WritableRaster raster = Raster.createWritableRaster(sm, db, null);
	    BufferedImage result = new BufferedImage(cm, raster, false, null);

	    return result;
	}

	@SuppressWarnings("serial")
	public static class RayTracerException extends Exception {
		public RayTracerException(String msg) {  super(msg); }
	}


}
