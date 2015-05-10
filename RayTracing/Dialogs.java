package RayTracing;

import java.awt.Component;
import java.util.List;

import javax.swing.BoxLayout;
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
					Integer.parseInt(threads.getText()), 
					antiAliasing.isContentAreaFilled(),
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
					Integer.parseInt( px.getText() ), 
					Integer.parseInt( py.getText() ), 
					Integer.parseInt( pz.getText() ), 
					Integer.parseInt( radius.getText() )  ),
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
					Integer.parseInt( nx.getText() ), 
					Integer.parseInt( ny.getText() ), 
					Integer.parseInt( nz.getText() ), 
					Integer.parseInt( offset.getText() )  ),
					materials.get(  Integer.parseInt( material.getText() )-1  ));
				}
			catch(Exception e){}
		}
		return null;
	}

}
