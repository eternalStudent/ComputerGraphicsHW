package RayTracing;

import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

		RGB pixelColor;
		
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

	RGB getPixelColor(int x, int y) {
		Ray ray = scene.camera.getRayByPixelCoordinate(x, y);

		return traceRay(ray, 0);
	}

	RGB traceRay(Ray ray, int iteration) {
		Hit closestHit = getClosestHit(ray);

		if (closestHit == null || iteration == scene.settings.maxRecursionLevel) {
			return scene.settings.background;
		}

		RGB color = RGB.BLACK;

		for (Light light : scene.lights) {
			Ray shadowRay = Ray.createRayByTwoVects(
				light.position,
				closestHit.intersection);
			//not a single ray, but N*N rays from grid, see below function

			Vector reflection = shadowRay.dir.getReflectionAroundNormal(closestHit.normal);
			Ray reflectionRay = Ray.createRayByTwoVects(closestHit.intersection, reflection);

			RGB reflectRGB = closestHit.getReflectRGB().equals(RGB.BLACK) ?
					RGB.BLACK :
					closestHit.getReflectRGB().multiply(traceRay(reflectionRay, iteration + 1));

			RGB lightRGB = light.rgb;

			if ( isOccluded(shadowRay, closestHit) ) {
				lightRGB = lightRGB.scale(light.shadow);
			}

			color = RGB.sum(
				color,
				getDiffuse(closestHit, shadowRay, lightRGB),
				getSpecular(closestHit, reflection, light, lightRGB),
				reflectRGB);
		}

		return color;
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

		if (closestHit == null) {
			return false;
		}
		return (closestHit.shape != hit.shape);
	}

	RGB getDiffuse(Hit hit, Ray shadowRay, RGB lightRGB) {
		double cosOfAngle = hit.normal.getCosOfAngle(shadowRay.dir.reverse());

		return cosOfAngle > 0 ?
			hit.getDiffuse().multiply(lightRGB).scale(cosOfAngle) :
			RGB.BLACK;
	}

	RGB getSpecular(Hit hit, Vector reflection, Light light, RGB lightRGB){
		double cosOfAngle = scene.camera.position.getCosOfAngle(reflection);

		return cosOfAngle > 0 && !hit.getSpecular().equals(RGB.BLACK) ?
				hit.getSpecular().scale(light.spec * Math.pow(cosOfAngle, hit.getPhong())) :
				RGB.BLACK;
	}
	
	List<Vector> getLightGrid(Ray ray, Light light){
		List<Vector> grid = new ArrayList<>();
		Plane plane = ray.getPerpendicularPlaneAtOrigion();
		int sahdowRayNums = scene.settings.shadowRaysNum;
		//chose a random vector on 'plane' that goes through 'ray' origin;
		//find a perpendicular vector to form a rectangle with width equals to light.width 
		//	and center at ray origion;
		//for i=0..shadowRayNum for j=0..shadowRayNums choose a Vector in the rectangle
		//	in a corresponding place and add to list;
		return grid;
	}

	void paintPixel(byte[] rgbData, int x, int y, RGB pixelColor) {
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
