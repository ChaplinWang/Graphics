import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;

/**  This class provides the choices between the various textures.
@author Tim Lambert
  */

public class TextureChoice extends JComboBox{
    static Map<String,MyTexture> textures; //associates file names with textures
    static String dirname = "textures";
    static String[] names;
    
    static {
	// we look for png files in the 
	// directory.  
	File dir = new File(dirname);
	names = dir.list(new FilenameFilter() {
		public boolean accept(File dir, String name) {
		    return name.toLowerCase().endsWith(".png");
	}
	    });
	textures = new HashMap<String,MyTexture>();
	if (names != null) {
	    for (int i = 0; i<names.length; i++) {
		textures.put(names[i], new MyTexture(names[i])); 
	    }
	}
    }

    public TextureChoice() {
	addItem("None");
	if (names != null) {
	    for (int i = 0; i<names.length; i++) {
		addItem(names[i]);
	    }
	}
    }
    
    public MyTexture getTexture() {
	return (MyTexture)textures.get(getSelectedItem());
    }
    
    public void setTexture(String s) {
	setSelectedItem(s);
    }
    
}
    
