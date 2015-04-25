package RayTracing;

public class SceneSettings {
	final Color background;
	final int shadowRaysNum;
	final int maxRecursionLevel;

	SceneSettings(
		double d, double e, double f,
		int shadowRaysNum, int maxRecursionLevel) {

		background = new Color(d, e, f);
		this.shadowRaysNum = shadowRaysNum;
		this.maxRecursionLevel = maxRecursionLevel;
	}
}


