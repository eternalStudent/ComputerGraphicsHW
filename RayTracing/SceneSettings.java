package RayTracing;

public class SceneSettings {
	final RGB background;
	final int shadowRaysNum;
	final int maxRecursionLevel;

	SceneSettings(
		byte bgr, byte bgg, byte bgb,
		int shadowRaysNum, int maxRecursionLevel) {

		background = new RGB(bgr, bgg, bgb);
		this.shadowRaysNum = shadowRaysNum;
		this.maxRecursionLevel = maxRecursionLevel;
	}
}


