package RayTracing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {
	
private ActionListener listener;
	
	public MenuBar(ActionListener al){
		super();
		listener = al;
		JMenu file = new JMenu("File");
		JMenu run = new JMenu("Run");
		JMenu options = new JMenu("Options");
		JMenu scene = new JMenu("Scene");
		add(file);
		add(run);
		add(options);
		add(scene);
		addMenuItem(file, "Open", KeyEvent.VK_O);
		addMenuItem(file, "Save", KeyEvent.VK_S);
		addMenuItem(run, "Render", 0);
		addMenuItem(options, "Settings", 0);
		addMenuItem(scene, "Add Sphere", 0);
		addMenuItem(scene, "Add Plane", 0);
		addMenuItem(scene, "Set Camera", 0);
		addMenuItem(scene, "Set Background Color", 0);
	}
	
	private void addMenuItem(JMenu parent, String text, int key){
		JMenuItem item = new JMenuItem(text);
		item.setActionCommand(text);
	    item.addActionListener(listener);
	    if (key !=0)
	    	item.setAccelerator(KeyStroke.getKeyStroke(key, ActionEvent.CTRL_MASK));
		parent.add(item);
	}	

}
