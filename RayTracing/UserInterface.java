package RayTracing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.Timer;

import javax.swing.JFileChooser;
import javax.swing.UIManager;

import RayTracing.RayTracer.RayTracerException;

public class UserInterface implements ActionListener{
	
	private View view = new View();
	private Scene scene;
	private RayTracer tracer;
	private Timer timer = new Timer(100, this);
	private RenderSettings settings = new RenderSettings(500, 500, 10, 4, false, 4);
	
	public UserInterface(){
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
		catch (Exception e) { e.printStackTrace(); }
		view.setJMenuBar(new MenuBar(this));
		view.pack();
		timer.setInitialDelay(100);
	}

	public static void main(String[] args){
		new UserInterface();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();
		JFileChooser chooser = new JFileChooser();

		if (cmd == null) {
			view.repaint();
			return;
		}

		if (cmd.equals("Open")){
			chooser.setDialogTitle("Open Scene File");
			if (chooser.showOpenDialog(view.getContentPane()) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                view.setTitle(file.getName());
				view.setContentPane(new Canvas(null));
                try {
					scene = RayTracer.parseScene(file);
					settings.maxRecursionLevel = scene.settings.maxRecursionLevel;
				} catch (IOException | RayTracerException e) {
					e.printStackTrace();
				}
			}    
		}
		
		if (cmd.equals("Render") && scene != null){
			tracer = new RayTracer(scene, settings);
			view.setImage(tracer.getImage());
			new Thread(new Renderer()).start();
			timer.start();
		}
		
		if (cmd.equals("Save") && tracer != null){
			chooser.setDialogTitle("Save rendered Scene as image");
			if (chooser.showSaveDialog(view.getContentPane()) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                view.setTitle(file.getName());
				tracer.saveImage(file);
			} 
		}
		
		if (cmd.equals("Settings")){
			RenderSettings temp = Dialogs.showSettingsDialog(view, settings);
			if (temp != null)
				settings = temp;
		}
		
		if (cmd.equals("Add Sphere") && scene != null){
			Primitive primitive = Dialogs.showAddSphereDialog(view, scene.materials);
			if (primitive != null)
				scene.primitives.add(primitive);
		}
		
		if (cmd.equals("Add Plane") && scene != null){
			Primitive primitive = Dialogs.showAddPlaneDialog(view, scene.materials);
			if (primitive != null)
				scene.primitives.add(primitive);
		}
		
		if (cmd.equals("Set Camera") && scene != null){
			Camera camera = Dialogs.showSetCameraDialog(view, scene.camera);
			if (camera != null)
				scene.camera = camera;
		}
		
		if (cmd.equals("Set Background Color") && scene != null){
			Color color = Dialogs.showBackgroundColorDialog(view, scene.settings.background);
			if (color != null)
				scene.settings.background = color;
		}
		
	}

	class Renderer implements Runnable {
		public void run() {
			tracer.renderScene();
		}
	}

}
