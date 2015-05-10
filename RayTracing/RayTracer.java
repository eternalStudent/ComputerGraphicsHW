package RayTracing;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

/**
 *  Main class for ray tracing exercise.
 */
public class RayTracer {

	public static final double EPSILON = 5e-10;
	final Scene scene;
	public AtomicInteger curColumn = new AtomicInteger();
	public int progress = 0;
	private BufferedImage image;
	ArrayList<Integer> shuffledArray;
	RenderSettings settings;
	public boolean halt;

	public RayTracer(Scene scene, RenderSettings settings){
		this.scene = scene;
		this.settings = settings;
		image = new BufferedImage(settings.imageWidth, settings.imageHeight, BufferedImage.TYPE_INT_RGB);
		shuffledArray = new ArrayList<>();
		for (int i = 0; i < settings.imageWidth; i++) {
			shuffledArray.add(i);
		}
		Collections.shuffle(shuffledArray);
		
	}
	
/**
  * Runs the ray tracer. Takes scene file, output image file and image size as input.
  */
	public static void main(String[] args) {

		try {
			if (args.length < 2)
				throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");
			
			// Determine image attributes
			int imageWidth = 500;
			int imageHeight = 500;
			if (args.length > 3){
				imageWidth = Integer.parseInt(args[2]);
				imageHeight = Integer.parseInt(args[3]);
			}

			// Parse scene file:
			String sceneFileName = args[0];
			System.out.println("Started parsing scene file " + sceneFileName);
			Scene scene = parseScene(sceneFileName);
			System.out.println("Finished parsing scene file " + sceneFileName);
			
			// Render scene
			RenderSettings settings = new RenderSettings(imageWidth, imageHeight, scene.settings.maxRecursionLevel, 4, false, 4); 
			RayTracer tracer = new RayTracer(scene, settings);
			tracer.renderScene();
			
//			new View(tracer.getImage());

			// Save rendered scene as image:
			String outputFileName = args[1];
			tracer.saveImage(outputFileName);
			System.out.println("Saved file " + outputFileName);

		} catch (RayTracerException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

/**
  * Parses the scene file and creates the scene. Change this function so it generates the required objects.
  */
	public static Scene parseScene(String sceneFileName) throws IOException, RayTracerException{
		FileReader fr = new FileReader(sceneFileName);
		return parseScene(fr);
	}
	
	public static Scene parseScene(File sceneFile) throws IOException, RayTracerException{
		FileReader fr = new FileReader(sceneFile);
		return parseScene(fr);
	}
	
	public static Scene parseScene(FileReader fr) throws IOException, RayTracerException{
		BufferedReader r = new BufferedReader(fr);
		String line = null;
		int lineNum = 0;
		Scene scene = new Scene();

		while ((line = r.readLine()) != null){
			line = line.trim();
			++lineNum;

			// This line in the scene file is a comment
			if (line.isEmpty() || (line.charAt(0) == '#'))
				continue;
			
			// Split according to white space characters:
			String[] params = line.substring(3).trim().toLowerCase().split("\\s+");
			String code = line.substring(0, 3).toLowerCase();
			
			//Parse camera
			if (code.equals("cam")){
				scene.camera = new Camera(
                    	Double.parseDouble(params[0]),
						Double.parseDouble(params[1]),
						Double.parseDouble(params[2]),
						Double.parseDouble(params[3]),
						Double.parseDouble(params[4]),
						Double.parseDouble(params[5]),
						Double.parseDouble(params[6]),
						Double.parseDouble(params[7]),
						Double.parseDouble(params[8]),
						Double.parseDouble(params[9]),
						Double.parseDouble(params[10]));
			}
			
			//Parse scene settings
			else if (code.equals("set")){
				scene.settings = new SceneSettings(
                    	Double.parseDouble(params[0]),
						Double.parseDouble(params[1]),
						Double.parseDouble(params[2]),
						Integer.parseInt(params[3]),
						Integer.parseInt(params[4])
					);
			}
			
			//Parse materials
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
						Double.parseDouble(params[9]),
						Double.parseDouble(params[10]))
					);
			}
			
			//Parse spheres
			else if (code.equals("sph")){
				scene.primitives.add(new Primitive(new Sphere(
	                	Double.parseDouble(params[0]),
	                	Double.parseDouble(params[1]),
		                Double.parseDouble(params[2]),
		                Double.parseDouble(params[3])),
		                scene.materials.get(Integer.parseInt(params[4]) - 1))
	                );
			}
			
			//Parse planes
			else if (code.equals("pln")){
				scene.primitives.add(new Primitive(new Plane(
	                	Double.parseDouble(params[0]),
	                	Double.parseDouble(params[1]),
		                Double.parseDouble(params[2]),
		                Double.parseDouble(params[3])),
		                scene.materials.get(Integer.parseInt(params[4]) - 1))
	                );
			}
			
			//Parse boxes
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
		                scene.materials.get(0))
		            );
			}
			
			//Parse lights
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
			}
				
			else{
				System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
			}
		}
		r.close();

        if (scene.camera == null)
			throw new RayTracerException("camera is undefined");
		
		return scene;
	}

/**
  * Renders the loaded scene and saves it to the specified file location.
  */

	public void renderScene() {
		long startTime = System.currentTimeMillis();
		int imageWidth = settings.imageWidth;
		int imageHeight = settings.imageHeight;
		scene.camera.build(imageWidth, imageHeight);
		
		RayTracingWorker[] workers = new RayTracingWorker[settings.numOfThreads];

		ExecutorService es = Executors.newCachedThreadPool();

		System.out.print("Rendering");
		for (int i = 0; i < settings.numOfThreads; i++) {
			workers[i] = new RayTracingWorker(this);
			es.execute(workers[i]);
		}
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
	}

	public Camera getCamera(){
		return scene.camera;
	}
	
	public SceneSettings getSettings(){
		return scene.settings;
	}
	
	public void paintPixel(int x, int y, Color pixelColor) {
		getImage().setRGB(x, y, pixelColor.getRGB());
	}

	//////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////

/**
  * Saves RGB data as an image in png format to the specified location.
  */
	public void saveImage(String fileName){
		saveImage(new File(fileName));
	}
	
	public void saveImage(File file){
		try {
			ImageIO.write(getImage(), "png", file);

		} catch (IOException e) {
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}
	}

	public BufferedImage getImage() {
	return image;
}

	@SuppressWarnings("serial")
	public static class RayTracerException extends Exception {
		public RayTracerException(String msg) {  super(msg); }
	}
	
}
