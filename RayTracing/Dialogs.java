package RayTracing;

import java.awt.Component;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

public class Dialogs {
		
	public static RenderSettings showSettingsDialog(Component parent, RenderSettings defaultSettings){
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		JPanel attributes = new JPanel();
		attributes.add(new JLabel("width"));
		JTextField width = new JTextField(Integer.toString(defaultSettings.imageWidth));
		attributes.add(width);
		attributes.add(new JLabel("height"));
		JTextField height = new JTextField(Integer.toString(defaultSettings.imageHeight));
		attributes.add(height);
		panel.add(attributes);
		JPanel recursionPanel = new JPanel();
		recursionPanel.add(new JLabel("max recursion level"));
		JTextField recursion = new JTextField(Integer.toString(defaultSettings.maxRecursionLevel));
		recursionPanel.add(recursion);
		panel.add(recursionPanel);
		JPanel threadsPanel = new JPanel();
		threadsPanel.add(new JLabel("number of threads"));
		JTextField threads = new JTextField(Integer.toString(defaultSettings.numOfThreads));
		threadsPanel.add(threads);
		panel.add(threadsPanel);
		JCheckBox antiAliasing = new JCheckBox("anti-aliasing",defaultSettings.antiAliasing);
		panel.add(antiAliasing);
		JPanel multiplierPanel = new JPanel();
		multiplierPanel.add(new JLabel("number of samples"));
		JTextField samples = new JTextField(Integer.toString(defaultSettings.numOfSamples));
		multiplierPanel.add(samples);
		panel.add(multiplierPanel);
		int option = JOptionPane.showConfirmDialog(parent, panel, "Render Settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (option == JOptionPane.OK_OPTION){
			try{
				return new RenderSettings(
					Integer.parseInt(width.getText()), 
					Integer.parseInt(height.getText()),
					Integer.parseInt(recursion.getText()),
					Integer.parseInt(threads.getText()), 
					antiAliasing.isSelected(),
					Integer.parseInt(samples.getText()));
				}
			catch(Exception e){}
		}
		return null;
	}
	
	public static Primitive showAddSphereDialog(Component parent, List<Material> materials){
		JPanel panel = new JPanel();
		panel.add(new JLabel("px"));
		JTextField px = new JTextField(3);
		panel.add(px);
		panel.add(new JLabel("py"));
		JTextField py = new JTextField(3);
		panel.add(py);
		panel.add(new JLabel("pz"));
		JTextField pz = new JTextField(3);
		panel.add(pz);
		panel.add(new JLabel("radius"));
		JTextField radius = new JTextField(3);
		panel.add(radius);
		panel.add(new JLabel("material index"));
		JTextField material = new JTextField(3);
		panel.add(material);
		int option = JOptionPane.showConfirmDialog(parent, panel, "Add Sphere", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (option == JOptionPane.OK_OPTION){
			try{
				return new Primitive(new Sphere(
					Double.parseDouble( px.getText() ), 
					Double.parseDouble( py.getText() ), 
					Double.parseDouble( pz.getText() ), 
					Double.parseDouble( radius.getText() )  ),
					materials.get(  Integer.parseInt( material.getText() )-1  ));
				}
			catch(Exception e){}
		}
		return null;
	}
	
	public static Primitive showAddPlaneDialog(Component parent, List<Material> materials){
		JPanel panel = new JPanel();
		panel.add(new JLabel("nx"));
		JTextField nx = new JTextField(3);
		panel.add(nx);
		panel.add(new JLabel("ny"));
		JTextField ny = new JTextField(3);
		panel.add(ny);
		panel.add(new JLabel("nz"));
		JTextField nz = new JTextField(3);
		panel.add(nz);
		panel.add(new JLabel("offset"));
		JTextField offset = new JTextField(3);
		panel.add(offset);
		panel.add(new JLabel("material index"));
		JTextField material = new JTextField(3);
		panel.add(material);
		int option = JOptionPane.showConfirmDialog(parent, panel, "Add Plane", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (option == JOptionPane.OK_OPTION){
			try{
				return new Primitive(new Plane(
					Double.parseDouble( nx.getText() ), 
					Double.parseDouble( ny.getText() ), 
					Double.parseDouble( nz.getText() ), 
					Double.parseDouble( offset.getText() )  ),
					materials.get(  Integer.parseInt( material.getText() )-1  ));
				}
			catch(Exception e){}
		}
		return null;
	}
	
	public static Camera showSetCameraDialog(Component parent, Camera defaultCamera){
		JPanel panel = new JPanel();
		panel.add(new JLabel("px"));
		JTextField px = new JTextField(Double.toString(defaultCamera.position.x));
		panel.add(px);
		panel.add(new JLabel("py"));
		JTextField py = new JTextField(Double.toString(defaultCamera.position.y));
		panel.add(py);
		panel.add(new JLabel("pz"));
		JTextField pz = new JTextField(Double.toString(defaultCamera.position.z));
		panel.add(pz);
		panel.add(new JLabel("nx"));
		JTextField nx = new JTextField(Double.toString(defaultCamera.screenNormal.x));
		panel.add(nx);
		panel.add(new JLabel("ny"));
		JTextField ny = new JTextField(Double.toString(defaultCamera.screenNormal.y));
		panel.add(ny);
		panel.add(new JLabel("nz"));
		JTextField nz = new JTextField(Double.toString(defaultCamera.screenNormal.z));
		panel.add(nz);
		panel.add(new JLabel("ux"));
		JTextField ux = new JTextField(Double.toString(defaultCamera.up.x));
		panel.add(ux);
		panel.add(new JLabel("uy"));
		JTextField uy = new JTextField(Double.toString(defaultCamera.up.y));
		panel.add(uy);
		panel.add(new JLabel("uz"));
		JTextField uz = new JTextField(Double.toString(defaultCamera.up.z));
		panel.add(uz);
		panel.add(new JLabel("screen distance"));
		JTextField distance = new JTextField(3);
		panel.add(distance);
		panel.add(new JLabel("screen width"));
		JTextField width = new JTextField(Double.toString(defaultCamera.screenWidth));
		panel.add(width);
		
		int option = JOptionPane.showConfirmDialog(parent, panel, "Set Camera", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (option == JOptionPane.OK_OPTION){
			try{
				return new Camera(
					Double.parseDouble( px.getText() ), 
					Double.parseDouble( py.getText() ), 
					Double.parseDouble( pz.getText() ),
					Double.parseDouble( nx.getText() ), 
					Double.parseDouble( ny.getText() ), 
					Double.parseDouble( nz.getText() ),
					Double.parseDouble( ux.getText() ), 
					Double.parseDouble( uy.getText() ), 
					Double.parseDouble( uz.getText() ),
					Double.parseDouble( distance.getText() ),
					Double.parseDouble( width.getText() ));
				}
			catch(Exception e){}
		}
		return null;
	}
	
	public static Color showBackgroundColorDialog(Component parent, Color color){
		java.awt.Color awtColor = new java.awt.Color(color.getRGB());
		awtColor = JColorChooser.showDialog(parent, "Set Background Color", awtColor);
		if (awtColor != null){
			color = new Color(awtColor.getRed()/255.0, awtColor.getGreen()/255.0, awtColor.getBlue()/255.0);
		}
		return color;
	}

}
