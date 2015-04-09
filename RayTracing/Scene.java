package RayTracing;

import java.util.ArrayList;

public class Scene {
	public ArrayList<Material> materials = new ArrayList<>();
	public ArrayList<Sphere> spheres = new ArrayList<>();
	public ArrayList<Light> lights = new ArrayList<>();
	public Camera camera;
	public SceneSettings settings;
}