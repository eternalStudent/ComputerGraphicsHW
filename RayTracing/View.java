package RayTracing;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class View extends JFrame {
	
	public View(BufferedImage image){
		this();
		setImage(image);
	}
	
	public View(){
		super();
		setTitle("Ray Tracer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		setVisible(true);
		setPreferredSize(new Dimension(360, 120));
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(new java.awt.Point((screenSize.width-this.getWidth())/3, (screenSize.height-this.getHeight())/3));
	}
	
	public void setImage(BufferedImage image){
		setPreferredSize(null);
		setContentPane(new Canvas(image));
		pack();
		setResizable(false);
	}

}
