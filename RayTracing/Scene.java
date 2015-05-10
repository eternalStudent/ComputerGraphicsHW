package RayTracing;

import java.util.ArrayList;
import java.util.List;

public class Scene {
	public final List<Material> materials = new ArrayList<>();
	public final List<Primitive> primitives = new ArrayList<>();
	public final List<Light> lights = new ArrayList<>();
	public Camera camera;
	public SceneSettings settings = new SceneSettings(1, 1, 1, 6, 12);
}