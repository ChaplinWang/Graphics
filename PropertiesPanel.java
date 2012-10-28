import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;

/**  This class is a panel to display/set the properties of a FPShape
     @author Tim Lambert
*/

public class PropertiesPanel extends JPanel {

    /** fill colour Choice */
    JButton colorB;
    Color color;
    
    /** texture Choice */
    TextureChoice texture;
    
    /** scale input text field */
    JTextField scale;

    /* parent Frame */
    JFrame parent ;

    /** extra text fields */
    JTextField[] extra = new JTextField[2];

    /** label extra text fields */
    JLabel[] extralabel = new JLabel[2];


    public PropertiesPanel(JFrame frame) {
        parent = frame;
	// Fill colour choice
	add(new JLabel("Colour:"));
	colorB = new JButton();
	color = Color.WHITE;
	colorB.setBackground(color);
	colorB.addActionListener
	    (
	     new ActionListener () {
		 public void actionPerformed(ActionEvent e) { 
		     color = JColorChooser.showDialog
			 ( 
			  parent, 
			  "Choose colour", 
			  color 
			  );
		     colorB.setBackground(color);
		 }});
	add(colorB);
	
	// Texture choice
	add(new JLabel("Texture:"));
	texture = new TextureChoice();
	add(texture);
	
	// Texture scale
	add(scale= new JTextField("1",4));

	// extra parameters
        for (int i = 0; i <=1; i++){
	    add(extralabel[i] = new JLabel("Extra:"));
	    add(extra[i]= new JTextField("10",4));
	}
    }

    /* get the properties for a shape from the PropertiesPanel */
    public void getProperties (FPShape s){
	s.setFillColor(getFillColor());
	s.setTexture(getTexture());
	s.setScale(getScale());
        for (int i = 0; i <=1; i++){
	    s.setExtra(i,getExtra(i));
	}
    }

    /* set the properties in PropertiesPanel  from a shape*/
    public void setProperties (FPShape s){
	color = s.getFillColor();
	colorB.setBackground(color);
	if (s.getTexture() != null) {
	    texture.setSelectedItem(s.getTexture().toString());
	}
	scale.setText(Double.toString(s.getScale()));
        for (int i = 0; i <=1; i++){
	    setExtraLabel(i,s.extraName(i));
	    extra[i].setText(Double.toString(s.getExtra(i)));
	}
    }


  /** Return the fill colour selected in the Fill colour choice
      @returns the fill colour selected in the Fill colour choice
  */


    public Color getFillColor() {
	return color;
    }
    
    /** Return the texture selected in the texture choice
	@returns the texture selected in the texture choice
    */
    
    public MyTexture getTexture() {
	return texture.getTexture();
    }
    
    /** Return the scale from the textfield
      @returns the scale from the textfield
    */
    
    public double getScale() {
	return Double.parseDouble(scale.getText());
    }

    /** set an extra in the textfield
      @param double  which extra to set
      @param double  value to set
    */
    
    public void setExtra(int i, double val) {
	extra[i].setText(Double.toString(val));
    }

    /** Return the extra from the textfield
      @param double  which extra to get
      @returns the extra from the textfield
    */
    
    public double getExtra(int i) {
	return Double.parseDouble(extra[i].getText());
    }

    /** set the label on the extra field
      @returns the extra from the textfield
    */
    
    public void setExtraLabel(int i, String name) {
        if (name == null) {
	    extralabel[i].setVisible(false);
	    extra[i].setVisible(false);
	} else {
	    extralabel[i].setVisible(true);
	    extra[i].setVisible(true);
	    extralabel[i].setText(name);
	}
    }
    

}
