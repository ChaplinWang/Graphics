import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**  Translate tool for image map editor
@author Tim Lambert
  */

public class TranslateTool extends Tool{
    
  public TranslateTool(FPEdit editor) {
    super(editor);
  }

  public String getName() {
    return "Translate";
  }

  public String getMessage() {
    return "Press mouse button inside an object and drag it to a new position";
  }

  private FPShape r;
    private Point lastp;

  // On button down, highlight the region, and display a message
    public void mousePressed(MouseEvent e) {
	r = canvas.getShapes().findShape(getPoint(e));
	if (r == null) return ;
	canvas.select(r);
	canvas.repaint();
	lastp = gridPoint(e);
  }

    public  void mouseDragged(MouseEvent e) {
	if (r != null) {
	    Point newp = gridPoint(e);
	    r.translate(newp.x-lastp.x, newp.y-lastp.y);
	    lastp = newp;
	    canvas.repaint();
	}
    }
    
  public Cursor getCursor() {
    return new Cursor(Cursor.MOVE_CURSOR);
  }



}
