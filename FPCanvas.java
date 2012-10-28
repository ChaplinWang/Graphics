import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.media.opengl.*;
import javax.swing.*;
import javax.swing.event.*;

/**  This class represents the drawing area in the driving simulator editor
     @author Tim Lambert
*/

public class FPCanvas extends JPanel
    implements GLEventListener{


    /** list of the shapes displayed in this canvas */
    protected FPShapeList shapes;
    /** the editor window that contains this canvas */
    protected FPEdit editor;
    /** currently selected shapes */
    protected FPShape selection = null;
    

    /** OpenGL drawing canvas */ 
    GLCanvas glc = null; 

    JScrollBar vertical;
    JScrollBar horizontal;

    //width and height on screen
    protected int width=600, height=400;

    //maximum width and height
    protected int maxWidth = 1000, maxHeight = 1000;

    /** The constructor is given the name of the file containing the
	floor plan to be displayed in this canvas and the parent frame.
	@param name - the name of the file containing the image map
	@param editor - the parent window
	@param useJPanel - if true use a GLJPanel rather than GLCanvas 
    */
    public FPCanvas(String name, FPEdit editor) {
	this.editor = editor;
	shapes = new FPShapeList();
	selection = null;
	if (name.equals("untitled.fpe")) {// new file
	} else {
	    try {
		shapes.read(name);
	    } catch (IOException e) {
		JOptionPane.showMessageDialog(editor,
				      "Sorry, couldn't open file\n"+e,
				      "Open error",
				      JOptionPane.ERROR_MESSAGE);
	    } catch (MalformedShapeException e) {
		JOptionPane.showMessageDialog
		    (editor,
		     "Are you sure that file contains a floor plan?\n"+e,
		     "Read error",
		     JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	/** 
	 * Set the layout manager for the Frame. This is BorderLayout 
	 * which will cause the children ( the GLDrawable ) to 
	 * be resized to fill the Frame upon resizing the Frame. 
	 */ 
	setLayout( new BorderLayout() );

	/** 
	 * Create a new GLDrawable from the factory 
	 */ 

	glc = new GLCanvas();

	/** Add the GLDrawable to the Panel and display it */
	add( "Center", glc ); 
	setSize(width,height);

	/** 
	 * Add the GLEventListener to handle GL events 
	 * and initialize! 
	 */ 
	glc.addGLEventListener( this ); 

        /* vertical scroll bar */
        vertical = new JScrollBar();
        vertical.setMaximum(maxHeight);
        add("East", vertical);
        vertical.getModel().addChangeListener(new ChangeListener() {
		public void stateChanged(ChangeEvent e){
		    glc.repaint();
		}
	    }
				   );

        /* horizontall scroll bar */
        horizontal = new JScrollBar(JScrollBar.HORIZONTAL);
        horizontal.setMaximum(maxWidth);
        add("South", horizontal);
        horizontal.getModel().addChangeListener(new ChangeListener() {
		public void stateChanged(ChangeEvent e){
		    glc.repaint();
		}
	    }
				   );
    }     
    
    public int getXoffset() {
	return horizontal.getValue();
    }
    
    public int getYoffset() {
	return vertical.getValue();
    }


    /** pass add/remove*Listener calls onto the GLDrawable */
    public void addMouseMotionListener(MouseMotionListener l) {
	glc.addMouseMotionListener( l );
    }
    public void addMouseListener(MouseListener l) {
	glc.addMouseListener( l );
    }
    public void addKeyListener(KeyListener l) {
	glc.addKeyListener( l );
    }
    public void removeMouseMotionListener(MouseMotionListener l) {
	glc.removeMouseMotionListener( l );
    }
    public void removeMouseListener(MouseListener l) {
	glc.removeMouseListener( l );
    }
    public void removeKeyListener(KeyListener l) {
	glc.removeKeyListener( l );
    }
    /** pass repaint to the GLDrawable */
    
    public void repaint() {
	if (glc != null) glc.repaint();
    }
    
    public void paintComponent(Graphics g) {  
	super.paintComponent(g);
	glc.repaint();
    }
  
    /** This method writes the content of the canvas to a file
	@param filename - the name of the file
    */
    
    public void write(String filename) {
	shapes.write(filename);
    }
    
    /** Return the shapes on this canvas.
	@returns list of shapes on this canvas
    */
    public FPShapeList getShapes(){
	return shapes;
    }

    /** Select a shape (as needed by the Select tool 
	@param r - shape to select
    */
    public void select(FPShape r){
	selection = r;
    }


    /** Return the selected shape
	@returns selected shapes
    */
    public FPShape getSelection(){
	return selection;
    }


    /** 
     * Executed exactly once to initialize the 
     * associated GLDrawable 
     */ 

    public void init( GLAutoDrawable drawable ) { 
	/** 
	 * Set the background colour when the GLDrawable 
	 * is cleared 
	 */ 
	GL gl = drawable.getGL();
	gl.glClearColor( 0.0f, 0.0f, 0.0f, 1.0f ); //black

    } 
    
    /** Handles resizing of the GLDrawable */ 

    public void reshape( GLAutoDrawable drawable, int x, int y,
			 int width, int height ) {
	GL gl = drawable.getGL();
	gl.glViewport( x, y, width, height );
	this.width = width;
        horizontal.setVisibleAmount(width);
	this.height = height;
        vertical.setVisibleAmount(height);
    }
    
    /** This method handles the painting of the GLDrawable */
    
    public void display( GLAutoDrawable drawable ) {
	
	GL gl = drawable.getGL();
	gl.glMatrixMode( GL.GL_PROJECTION );
	gl.glLoadIdentity();
	int xoffset = horizontal.getValue();
	int yoffset = vertical.getValue();
	gl.glOrtho( xoffset, xoffset+width, yoffset+height, yoffset,-1,1);
	/** Clear the colour buffer */
	gl.glClear( GL.GL_COLOR_BUFFER_BIT );
	shapes.paint(gl, drawable);
	if (selection != null){
	    selection.paintSelect(gl);
	}
    } 
    /** This method handles things if display depth changes */
    public void displayChanged(GLAutoDrawable drawable,
			       boolean modeChanged,
			       boolean deviceChanged){
    }

}

