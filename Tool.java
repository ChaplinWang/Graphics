import java.awt.*;
import java.awt.event.*;

/**  This class represents a tool such as select, translate etc in 
     the FP editor.
@author Tim Lambert
  */

public abstract class Tool implements MouseMotionListener, MouseListener{


    /** The  canvas that the tool operates on*/
    protected FPCanvas canvas;
    /** The frame that contains the the canvas */
    protected FPEdit editor;

    /** Extra values (like height) set for this tool */
    protected double[] extra = {0.0,0.0};

    /** The constructor for a Tool
	@param editor - the frame that the tool is operating in.
    */

    public Tool(FPEdit editor) {
	this.editor = editor;
	canvas = editor.getCanvas();
    }

    /** Return the name of the tool to be displayed in a menu
	@returns the name of the tool
    */
    public abstract String getName();


    /** Return cursor to use when this tool is active
	@returns the cursor to use when this tool is active
    */

    public Cursor getCursor() {
	return new Cursor(Cursor.DEFAULT_CURSOR);
    }

    /** Returns the message to be displayed in the status area when the tool
	is activated.  The message will typically contain brief instructions.
	@returns the message to be displayed
    */

    public String getMessage() {
	return "";
    }

    /** name of the extra parameter
	@returns name of extra parameter (eg "width")
    */

    public String extraName(int i){
	return null;
    }

    /** value of the extra parameter
	@param i -  which parameter
	@returns value of extra parameter
    */

    public double getExtra(int i){
	return extra[i];
    }

    /** set value of an extra parameter
	@param i -  which parameter
	@param val -  value of extra parameter
    */

    public void setExtra(int i,double val){
	extra[i] = val;
    }

    /* mouse position, rounded to grid point */
    Point gridPoint(MouseEvent e) {
	int gridsize = editor.getGridSize();

	return (new Point(((e.getX()+canvas.getXoffset())/gridsize)*gridsize,
			  ((e.getY()+canvas.getYoffset())/gridsize)*gridsize));
    }

    /* mouse position 
       We have to adjust the (x,y) coordinates returned in the mouse event
       too allow for the postion of the scrollbars */
    Point getPoint(MouseEvent e) {
	return (new Point(e.getX()+canvas.getXoffset(),
			  e.getY()+canvas.getYoffset()));
    }
    

    public void mousePressed(MouseEvent e) {
    }
    
    public  void mouseClicked(MouseEvent e) {
    }
    
    public  void mouseReleased(MouseEvent e) {
    }
    
    public  void mouseEntered(MouseEvent e) {
    }
    
    public  void mouseExited(MouseEvent e) {
    }
    
    public  void mouseDragged(MouseEvent e) {
    }
    
    public  void mouseMoved(MouseEvent e) {
    }


}
