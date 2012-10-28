import java.awt.*;
import java.util.*;
import java.io.*;
import javax.media.opengl.*;
import com.sun.opengl.util.*;


/**  This class represents a shape in the editor (such as a polygon)
     @author Tim Lambert
*/

public abstract class FPShape implements Cloneable,Serializable {
    /** fill colour to fill the shape with */
    Color fill = Color.white;

    /** Texture for this shape */
    MyTexture texture = null;

    /** Texture scale for this shape */
    double scale = 1.0;

    /** extra parameters - could be height or something else */
    double[] extra = {0.0,0.0};

    /** Set the fill colour.
	@param fill colour to fill the shape with
    */

    public void setFillColor(Color fill){
	this.fill= fill;
    }

    /** Get the fill colour.
	@returns fill colour to fill the shape with
    */

    public Color getFillColor(){
	return fill;
    }

    /** Set the texture
	@param texture for this shape
    */

    public void setTexture(MyTexture texture){
	this.texture = texture;
    }


    /** Get the texture
	@returns texture for this shape
    */

    public MyTexture getTexture(){
	return texture;
    }




    /** Set the texture scale
	@param texture for this shape
    */

    public void setScale(double scale){
	this.scale = scale;
    }

    /** get the texture scale
	@returns the scale
    */

    public double getScale(){
	return scale;
    }


    /** Set an extra parameter
	@param which paramter to set
	@param value to set
    */

    public void setExtra(int i, double val){
	extra[i] = val;
    }

    /** get an extra parameter
	@param which value to get
	@returns the extra parameter
    */

    public double getExtra(int i){
	return extra[i];
    }

    /** name of an extra parameter
	@param which value to get
	@returns name of extra parameter (eg "width")
    */

    public abstract String extraName(int i);

    /** Draw a point in XOR mode
	@param gl The GL pipeline to use
	@param x x coord
	@param y y coord
    */
    final static int SIZE = 2;
    public void drawPoint(GL gl, double x, double y){
	gl.glColor3f(1.0f, 1.0f, 1.0f);
	gl.glPushAttrib(GL.GL_COLOR_BUFFER_BIT); //save current Logic Op
	gl.glDisable( GL.GL_TEXTURE_2D );
	gl.glEnable(GL.GL_COLOR_LOGIC_OP);
	gl.glLogicOp(GL.GL_XOR);
	gl.glBegin(GL.GL_POLYGON);
	gl.glVertex2d(x+SIZE,y+SIZE);
	gl.glVertex2d(x-SIZE,y+SIZE);
	gl.glVertex2d(x-SIZE,y-SIZE);
	gl.glVertex2d(x+SIZE,y-SIZE);
	gl.glEnd();
	gl.glPopAttrib();
    }

    /** This sets the current GL drawing colour
	@param gl The GL pipeline to use
	@param c colour to set GL to use
    */
    public void setColor(GL gl, Color c){
	gl.glColor3ub((byte)c.getRed(),(byte)c.getGreen(),(byte)c.getBlue());
    }
    /** Creates a shape from a string describing it.
	@param s The string describing the shape. This must consist of a sequence of
	space separated fields.  The first field is the name of a class
	that is a child of FPShape.  The next three are the colour.
	The next is the name of the texture.  Further fields will be interpreted
	by the child class's fromTokens method.
	@return The clickable region described by the string.
	@exception MalformedShapeException If the string s cannot be parsed.
    */

    public static FPShape create(String s) throws MalformedShapeException{
	FPShape shape;
	String classname="";
	try {
	    StringTokenizer st = new StringTokenizer(s, " \t");
	    classname = st.nextToken();
	    Class aclass = Class.forName(classname);
	    shape = (FPShape) aclass.newInstance();
	    shape.fill = new Color(Integer.parseInt(st.nextToken()),
				   Integer.parseInt(st.nextToken()),
				   Integer.parseInt(st.nextToken()));
	    shape.texture = (MyTexture) TextureChoice.textures.get(st.nextToken());
	    shape.scale = Double.parseDouble(st.nextToken());
	    shape.extra[0] = Double.parseDouble(st.nextToken());
	    shape.extra[1] = Double.parseDouble(st.nextToken());
	    shape.fromTokens(st);
	    return shape;
	}
	catch (ClassNotFoundException e) {
	    System.err.println("Class not found: "+e.getMessage());
	    throw new MalformedShapeException(s);
	} catch (InstantiationException e) {
	    System.err.println("Couldn't Instance "+e.getMessage());
	    throw new MalformedShapeException(s);
	} catch (IllegalAccessException e) {
	    System.err.println("Illegal Access "+e.getMessage());
	    throw new MalformedShapeException(s);
	} catch (NoSuchElementException e) {
	    System.err.println("No such element "+e.getMessage());
	    throw new MalformedShapeException(s);
	} catch (ClassCastException e) {
	    System.err.println(classname + "is not a subclass of FPShape");
	    throw new MalformedShapeException(s);
	}
    }

    /** This must be implemented by child classes to parse the remaining fields
	of the string argument to create.
	@see FPShape#create
	@param context A URL.  Relative URLs will be interpreted relative to this.
	@param st A StringTokenizer providing the remainig fields of the String argument to create.
	@exception MalformedShapeException If the string s cannot be parsed.
    */

    protected abstract void fromTokens(StringTokenizer st) throws MalformedShapeException;

    /** This paints the shape on the screen.
	@param gl The GL pipeline to use
    */

    public abstract void paint(GL gl, GLDrawable glc);

    /** This paints a selected shape on the screen.
	If a shape is selected first paint is called, then paintSelect.
	@param gl The GL pipeline to use
    */

    public abstract void paintSelect(GL gl);


    /** This renders the shape on screen, but in 3D.
        @param gl The GL pipeline to use
        @param glc The GLComponent we're painting on
    */
    public abstract void render3D(GL gl, GLDrawable glc);
    


    /** This determines whether a query point is inside the shape.
	@param x The x coordinate of the query point.
	@param y The y coordinate of the query point.
	@returns true if the query point is inside the shape.
    */

    public abstract boolean contains(int x, int y);


    /** check for collision with the shape
	and return adjusted position after collision
	If no collision return null.
	@param from - Vector3D we are moving from
	@param to - Vector3D we are moving to
	@returns Vector3D giving corrected position
     */
    public Vector3D collide(Vector3D from, Vector3D to){
	return null;
    }


    /** This method translates the Shape by the specified amount.
	@param deltax The number of pixels to change the x position by.
	@param deltay The number of pixels to change the y position by.
    */

    public abstract void translate(int deltax, int deltay);

    /** This method rotates the Shape by the specified amount.
	@param fixed Centre of rotation
	@param angle Angle to rotate by
    */

    public abstract void rotate(Point2D fixed, double angle);

    /** This method rotates the Shape by the specified amount.
	@param fixed Centre of scaling
	@param xscale x scale factor
    */

    public abstract void scale(Point2D fixed, double xscale, double yscale);

    /** This method returns the centre of a shape
	@returns centre of rotation
    */

    public abstract Point2D centre();


    /** This method adds a control point to an Shape.
	What the control points mean depends on the shape.  For a polygon the 
	control points are the corners.  For a rectangle, they are two diagonally
	opposite corners and so on for other sorts of shapes.
	<p>This method can be used to interactively create an FPShape.
	@param p control point
    */

    public abstract void addPoint(Point p);


    /** This method selects the control point to be modified by the 
	setPoint method.  This would typically be the closest one to that specified
	in the parameters.
	<p>This method can be used in conjuction with setPoint to interactively 
	modify a FPShape.
	@see FPShape#setPoint
	@param p control point
    */

    public abstract Point selectPoint(Point p);


    /** This method gives the selected control point a new value.  The
	selected control point is the one specified by a previous selectPoint,
	or the one must recently added.
	@param p control point
    */

    public abstract void setPoint(Point p);

    /** This method removes the selected control point.  The
	selected control point is the one specified by a previous selectPoint,
	or the one must recently added.
	@param p control point
    */

    public abstract void removePoint();


    /** The string that this returns must be suitable to be used in create to
	recreate the instance.
	@returns The string description of this instance.
    */
    public String toString() {
	return getClass().getName() + " " +fill.getRed() + " " +
	    fill.getGreen() + " " + fill.getBlue() + " " + texture +
	    " " + scale + " " + extra[0] + " " + extra[1] + " ";
    }

    public Object clone(){

	try {
	    return create(toString());
	} catch (MalformedShapeException e){
	    System.err.println("Could not clone FPShape "+e.getMessage());
	    return null;
	}
    }

}
