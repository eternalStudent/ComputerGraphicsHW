package RayTracing;

import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

			if (args.length > 3){
				tracer.imageWidth = Integer.parseInt(args[2]);
				tracer.imageHeight = Integer.parseInt(args[3]);
			}

			// Parse scene file:
			tracer.parseScene(sceneFileName);

			// Render scene:
			tracer.renderScene(outputFileName);

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

			if (line.isEmpty() || (line.charAt(0) == '#')){
				// This line in the scene file is a comment
				continue;
			}
			else{
				String code = line.substring(0, 3).toLowerCase();
				// Split according to white space characters:
				String[] params = line.substring(3).trim().toLowerCase().split("\\s+");
				if (code.equals("cam")){
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
				else if (code.equals("set")){
                    scene.settings = new SceneSettings(
                    	Double.parseDouble(params[0]),
						Double.parseDouble(params[1]),
						Double.parseDouble(params[2]),
						Integer.parseInt(params[3]),
						Integer.parseInt(params[4])
					);

					System.out.println(String.format("Parsed general settings (line %d)", lineNum));
				}
				else if (code.equals("mtl")){
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
				else if (code.equals("sph")){
	                scene.primitives.add(new Primitive(new Sphere(
	                	Float.parseFloat(params[0]),
	                	Float.parseFloat(params[1]),
		                Float.parseFloat(params[2]),
		                Float.parseFloat(params[3])),
		                scene.materials.get(Integer.parseInt(params[4]) - 1))
	                );

					System.out.println(String.format("Parsed sphere (line %d)", lineNum));
				}
				else if (code.equals("pln")){
                    scene.primitives.add(new Primitive(new Plane(
	                	Double.parseDouble(params[0]),
	                	Double.parseDouble(params[1]),
		                Double.parseDouble(params[2]),
		                Double.parseDouble(params[3])),
		                scene.materials.get(Integer.parseInt(params[4]) - 1))
	                );

					System.out.println(String.format("Parsed plane (line %d)", lineNum));
				}
				else if (code.equals("box")){
					scene.primitives.add(new Primitive(new Box(
		                Double.parseDouble(params[0]),
		                Double.parseDouble(params[1]),
		                Double.parseDouble(params[2]),
		                Double.parseDouble(params[3]),
		                Double.parseDouble(params[4]),
		                Double.parseDouble(params[5]),
		                Double.parseDouble(params[6]),
		                Double.parseDouble(params[7]),
		                Double.parseDouble(params[8])),
		                scene.materials.get(Integer.parseInt(params[9]) - 1))
		            );
					
					System.out.println(String.format("Parsed box (line %d)", lineNum));
				}
				else if (code.equals("lgt")){
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
				else{
					System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
				}
			}
		}
		r.close();

                // It is recommended that you check here that the scene is valid,
                // for example camera settings and all necessary materials were defined.
		if (scene.camera == null)
			throw new RayTracerException("camera is undefined");

		System.out.println("Finished parsing scene file " + sceneFileName);

	}

	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 */
	public void renderScene(String outputFileName){
		long startTime = System.currentTimeMillis();

		byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];
		
		int numOfThreads = 4;
		RayTracingWorker[] workers = new RayTracingWorker[numOfThreads];
		int hPixels = imageHeight / numOfThreads;
		ExecutorService es = Executors.newCachedThreadPool();
		
		System.out.print("Rendering");
		for (int i = 0; i < numOfThreads - 1; i++) {
			workers[i] = new RayTracingWorker(i*hPixels, (i+1)*hPixels, imageWidth, scene, rgbData);
			es.execute(workers[i]);
		}

		// In case N doesn't divide imageHeight, the last thread gets to do the additional
		// remainder.
		workers[numOfThreads-1] = new RayTracingWorker(
				(numOfThreads-1)*hPixels,
				( numOfThreads*hPixels + (imageHeight % numOfThreads) ),
				imageWidth, scene, rgbData);
		es.execute(workers[numOfThreads-1]);
		
		es.shutdown();

		try {
			es.awaitTermination(10, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println();
		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;
		System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");
		saveImage(this.imageWidth, rgbData, outputFileName);
		System.out.println("Saved file " + outputFileName);

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
