package RayTracing;

public class SceneSettings {
	final RGB background;
	final int shadowRaysNum;
	final int maxRecursionLevel;

	SceneSettings(
		double d, double e, double f,
		int shadowRaysNum, int maxRecursionLevel) {

		background = new RGB(d, e, f);
		this.shadowRaysNum = shadowRaysNum;
		this.maxRecursionLevel = maxRecursionLevel;
	}
}


