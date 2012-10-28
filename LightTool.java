import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**  Light tool for graphical editor
@author Tim Lambert
  */

public class LightTool extends Tool{
    
    
    public LightTool(FPEdit editor) {
	super(editor);
    }

    public String extraName(int i){
	return new Light().extraName(i);
    }

    public String getName() {
	return "Light";
    }

    public String getMessage() {
	return "Click to create a light";
    }

    // create the light
    public void mousePressed(MouseEvent e) {
	Light p = new Light();
	editor.getProperties(p);
	
	canvas.getShapes().add(p);
	canvas.select(p);
	p.addPoint(gridPoint(e));
	canvas.repaint();
    }


}
