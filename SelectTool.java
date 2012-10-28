import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**  Translate tool for image map editor
@author Tim Lambert
  */

public class SelectTool extends Tool{
    
  public SelectTool(FPEdit editor) {
    super(editor);
  }

  public String getName() {
    return "Select";
  }

  public String getMessage() {
      return "Click on object to select";
  }

  private FPShape r;
    private Point lastp;

  // On button down, highlight the region, and display a message
    public void mouseClicked(MouseEvent e) {
	r = canvas.getShapes().findShape(getPoint(e));
	canvas.select(r);

	canvas.repaint();
  }


    
  public Cursor getCursor() {
    return new Cursor(Cursor.HAND_CURSOR);
  }



}
