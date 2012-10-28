import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**  Polygon tool for graphical editor
@author Tim Lambert
  */

public class WallTool extends Tool{
    
    
    public WallTool(FPEdit editor) {
	super(editor);
        extra[0] = 0.0;
        extra[1] = 2.4; //default height
    }

    public String getName() {
	return "Wall";
    }

    public String getMessage() {
	return "Click and drag to create a wall";
    }

    public String extraName(int i){
	return newWall().extraName(i);
    }

    protected Wall p; /* Wall being created */


    public Wall newWall(){
	return new Wall();
    }

    // create the polygon
    public void mousePressed(MouseEvent e) {
	p = newWall();
	editor.getProperties(p);
	
	canvas.getShapes().add(p);
	canvas.select(p);
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
	p = null;
    }

}
