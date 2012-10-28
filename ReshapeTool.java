import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**  Reshape tool for graphical editor
@author Tim Lambert
  */

public class ReshapeTool extends Tool{
    
  public ReshapeTool(FPEdit editor) {
    super(editor);
  }

  public String getName() {
    return "Reshape";
  }

  public String getMessage() {
    return "Shift click selects. Drag points or click to add, right click to delete";
  }

  public void mousePressed(MouseEvent e) {
    FPShape r;

    if (e.isShiftDown()){ /*shift click, select shape */
	r = canvas.getShapes().findShape(getPoint(e));
	canvas.select(r);
    } else {
      r = canvas.getSelection();
      if (r == null) return;
      if (r.selectPoint(getPoint(e)) == null) { /*no point selected add new one*/
	r.addPoint(gridPoint(e));
      } else if (e.isMetaDown()) { /*right click, delete control point */
	r.removePoint();
      }
    }
    canvas.repaint();
  }

  public  void mouseDragged(MouseEvent e) {
    FPShape r = canvas.getSelection();
    if(r != null ) {
      r.setPoint(gridPoint(e));
	canvas.repaint();
    }
  }
  
  public Cursor getCursor() {
    return new Cursor(Cursor.HAND_CURSOR);
  }
  

}
