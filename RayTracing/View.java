package RayTracing;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class View extends JFrame {
	
	public View(BufferedImage image){
		super();
		setTitle("Ray Tracer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		setResizable(false);
		setContentPane(new Canvas(image));
		setVisible(true);
		pack();
	}

}
