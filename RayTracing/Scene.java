package RayTracing;

import java.util.ArrayList;
import java.util.List;

public class Scene {
	public List<Material> materials = new ArrayList<>();
	public List<Primitive> primitives = new ArrayList<>();
	public List<Light> lights = new ArrayList<>();
	public Camera camera;
	public SceneSettings settings;
}