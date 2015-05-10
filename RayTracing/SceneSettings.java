package RayTracing;

public class SceneSettings {
	Color background;
	final int shadowRaysNum;
	final int maxRecursionLevel;

	SceneSettings(
		double red, double green, double blue,
		int shadowRaysNum, int maxRecursionLevel) {

		background = new Color(red, green, blue);
		this.shadowRaysNum = shadowRaysNum;
		this.maxRecursionLevel = maxRecursionLevel;
	}
}


