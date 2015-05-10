package RayTracing;

public class RenderSettings {
	
	public int imageWidth, imageHeight;
	public int numOfThreads;
	public boolean antiAliasing;
	public int numOfSamples;

	public RenderSettings(int width, int height, int threads, boolean alias, int multiplier) {
		this.imageWidth = width;
		this.imageHeight = height;
		numOfThreads = threads;
		antiAliasing = alias;
		numOfSamples = multiplier;
	}

}
