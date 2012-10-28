import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**  Scale tool for driving simulator editor
@author Tim Lambert
  */

public class ScaleTool extends Tool{
    
  public ScaleTool(FPEdit editor) {
    super(editor);
  }

  public String getName() {
    return "Scale";
  }

  public String getMessage() {
    return "Press mouse button inside an object and drag it to scale it";
  }

  private FPShape r;
    private Point lastp;
    private double lastdx, lastdy;
    private Point2D c;

  // On button down, highlight the region, and display a message
    public void mousePressed(MouseEvent e) {
	r = canvas.getShapes().findShape(getPoint(e));
	if (r == null) return ;
	canvas.select(r);
	canvas.repaint();
	Point p = getPoint(e);
	c = r.centre();
        lastdx = p.x - c.x;
        if (lastdx == 0.0) lastdx = 1.0;
        lastdy = p.y - c.y;
        if (lastdy == 0.0) lastdy = 1.0;
    }

    public  void mouseDragged(MouseEvent e) {
	if (r != null) {
	    Point p = getPoint(e);
	    double dx = p.x - c.x;
	    if (dx == 0.0) dx = 1.0;
	    double dy = p.y - c.y;
	    if (dy == 0.0) dy = 1.0;
	    r.scale(c, dx/lastdx, dy/lastdy);
	    lastdx = dx;
	    lastdy = dy;
	    canvas.repaint();
	}
    }
    
  public Cursor getCursor() {
    return new Cursor(Cursor.MOVE_CURSOR);
  }



}
