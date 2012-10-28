import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**  Polygon tool for graphical editor
@author Tim Lambert
  */

public class PolygonTool extends Tool{
    
    
    public PolygonTool(FPEdit editor) {
	super(editor);
	extra[0] = 0.0; //initial polygon height
    }

    public String getName() {
	return "Polygon";
    }

    public String extraName(int i){
	return new FPPolygon().extraName(i);
    }

    public String getMessage() {
	return "Click at each corner, Shift Click on last point";
    }

    protected FPPolygon p; /* Polygon being created */

    // create the polygon
    public void mouseClicked(MouseEvent e) {
	if (p==null){
	    p = new FPPolygon();
	    editor.getProperties(p);
	    
	    canvas.getShapes().add(p);
	    canvas.select(p);
	    p.addPoint(gridPoint(e));
	}
	if (e.isShiftDown()){
	    p = null;
	} else {
	    p.addPoint(gridPoint(e));
	}
	canvas.repaint();
    }

    public  void mouseMoved(MouseEvent e) {
	mouseDragged(e);
    }

    public  void mouseDragged(MouseEvent e) {
	if (p != null){
	    p.setPoint(gridPoint(e));
	    canvas.repaint();
	}
    }


}
