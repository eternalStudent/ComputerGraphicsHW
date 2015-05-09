package RayTracing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.UIManager;

import RayTracing.RayTracer.RayTracerException;

public class UserInterface implements ActionListener{
	
	private View view = new View();
	private Scene scene;
	private RayTracer tracer;
	
	public UserInterface(){
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
		catch (Exception e) { e.printStackTrace(); }
		view.setJMenuBar(new MenuBar(this));
		view.pack();
	}

	public static void main(String[] args){
		new UserInterface();
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();
		JFileChooser chooser = new JFileChooser();
		
		if (cmd.equals("Open")){
			chooser.setDialogTitle("Open Scene File");
			if (chooser.showSaveDialog(view.getContentPane()) == JFileChooser.APPROVE_OPTION) {
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
			tracer =  new RayTracer(scene, 500, 500);
			view.setImage(tracer.getImage());
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

}
