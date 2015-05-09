package RayTracing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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
                try {
					scene = RayTracer.parseScene(file, 500, 500);
				} catch (IOException | RayTracerException e) {
					e.printStackTrace();
				}
			}    
		}
		
		if (cmd.equals("Render") && scene != null){
			tracer = new RayTracer(scene, 500, 500);
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
	}

	class Renderer implements Runnable {
		public void run() {
			tracer.renderScene();
		}
	}

}
