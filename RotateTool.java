import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**  Rotate tool for floor plan editor
@author Tim Lambert
  */

public class RotateTool extends Tool{
    
  public RotateTool(FPEdit editor) {
    super(editor);
  }

  public String getName() {
    return "Rotate";
  }

  public String getMessage() {
    return "Press mouse button inside an object and drag it to rotate it";
  }

  private FPShape r;
    private Point lastp;
    private double lasta;
    private Point2D c;

  // On button down, highlight the region, and display a message
    public void mousePressed(MouseEvent e) {
	r = canvas.getShapes().findShape(getPoint(e));
	if (r == null) return ;
	canvas.select(r);
	canvas.repaint();
	lastp = getPoint(e);
	c = r.centre();
        lasta = Math.atan2(lastp.y-c.y,lastp.x-c.x);
    }

    public  void mouseDragged(MouseEvent e) {
	if (r != null) {
	    lastp = getPoint(e);
	    double a = Math.atan2(lastp.y-c.y,lastp.x-c.x);
	    r.rotate(c, a-lasta);
	    lasta = a;
	    canvas.repaint();
	}
    }
    
  public Cursor getCursor() {
    return new Cursor(Cursor.MOVE_CURSOR);
  }



}
