package RayTracing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Canvas extends JPanel {
	
	private BufferedImage image;
	
	public Canvas(BufferedImage image){
		super();
		this.image = image;
		this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		g.drawImage(image, 0, 0, null);
	}

}
