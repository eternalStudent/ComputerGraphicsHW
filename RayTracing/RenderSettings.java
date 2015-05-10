package RayTracing;

public class RenderSettings {
	
	public int imageWidth, imageHeight;
	public int maxRecursionLevel;
	public int numOfThreads;
	public boolean antiAliasing;
	public int numOfSamples;

	public RenderSettings(int width, int height, int max, int threads, boolean alias, int multiplier) {
		this.imageWidth = width;
		this.imageHeight = height;
		maxRecursionLevel = max;
		numOfThreads = threads;
		antiAliasing = alias;
		numOfSamples = multiplier;
	}

}
