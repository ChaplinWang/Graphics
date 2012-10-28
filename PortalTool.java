import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**  Portal tool for graphical editor 
     portal is defined by four control points
@author Tim Lambert
  */

public class PortalTool extends Tool{
    
    
    public PortalTool(FPEdit editor) {
	super(editor);
        extra[0] = 0.0;
        extra[1] = 0.0; 
    }

    public String getName() {
	return "Portal";
    }

    public String getMessage() {
	return "Click and drag twice to  create a portal";
    }

    public String extraName(int i){
	return new Portal().extraName(i);
    }

    protected Portal p; /* Portal being created */


    // create the Portal
    public void mousePressed(MouseEvent e) {
	if (p==null) {
	    p = new Portal();
	    editor.getProperties(p);
	    
	    canvas.getShapes().add(p);
	    canvas.select(p);
	} 
	p.addPoint(gridPoint(e));
	p.addPoint(gridPoint(e));   
	canvas.repaint();
    }


    public  void mouseDragged(MouseEvent e) {
	if (p != null){
	    p.setPoint(gridPoint(e));
	    canvas.repaint();
	}
    }

    public  void mouseReleased(MouseEvent e) {
	mouseDragged(e);
	if (p.pts2d.size() == 4) {
	    p = null;
	}
    }

}
