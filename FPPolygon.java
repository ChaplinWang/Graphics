/** This class represents a polygon */
import java.awt.Point;
import java.awt.Color;
import java.awt.Polygon;
import java.util.*;
import javax.media.opengl.*;
import com.sun.opengl.util.texture.*;


public class FPPolygon extends FPShape {

    public String extraName(int i){
	return i==0 ? "Height" : null;
    }

    protected ArrayList<Point2D> pts2d;
    protected int selection = -1;
    
    public FPPolygon() {
	pts2d = new ArrayList<Point2D>();
    }

    public String extraName(){
	return "Height";
    }

    /** paint this curve into g.*/
    public void paint(GL gl, GLDrawable glc){

	if (fill != null || texture != null){
	    if (texture == null) {
		setColor(gl,fill);
		gl.glDisable( GL.GL_TEXTURE_2D );
	    } else {
		Texture gltexture = texture.getTexture(glc);
		gltexture.enable();
		gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT );
		gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT );
		gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR );
		gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR );
		gl.glTexEnvf( GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE,
			      GL.GL_REPLACE );
		gltexture.bind();
	    }
	    gl.glBegin( GL.GL_POLYGON );  //draw the polygon 
	    for(Point2D p : pts2d) {
		gl.glTexCoord2d(scale*p.x/100, -scale*p.y/100); //dodgy texture scaling
		gl.glVertex2d(p.x, p.y);
	    }
	    gl.glEnd(); 

	}
    }

    public void paintSelect(GL gl){
	for(Point2D p : pts2d) {
	    drawPoint(gl,p.x,p.y);
	}
    }


   public void render3D(GL gl, GLDrawable glc){
       if (pts2d.size() < 3) return;
        if (fill != null || texture != null){
            if (texture == null) {
                setColor(gl,fill);
                gl.glDisable( GL.GL_TEXTURE_2D );
            } else {
                setColor(gl, Color.white);
		Texture gltexture = texture.getTexture(glc);
		gltexture.enable();
		gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT );
		gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT );
                gl.glTexEnvf( GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE,
                              GL.GL_MODULATE);
                gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER ,
                                  GL.GL_NEAREST);
                gl.glTexParameteri( GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER ,
                                  GL.GL_NEAREST);
		gltexture.bind();
            }
            gl.glPushMatrix();
            gl.glBegin( GL.GL_POLYGON );  //draw the polygon

            // First we have to work out the normal correctly; make sure it po ints
            // in the correct direction.
            //
            // this calculation uses the cross product of the first two edges.  It
            // It is easy to show that the cross product has a y value of:
            // (y1-y0).(x2-x1)-(x1-x0).(y2-y1). If this is positive, the norma l is (0,1,0),
            // if this is negative then it is (0, -1, 0). This works for conca ve polygons.
            // This should actually be stored in a field somewhere, so it is n ot continually
            // recalculated but ...
            Point2D p0 =  pts2d.get(0);
            Point2D p1 =  pts2d.get(1);
            Point2D p2 =  pts2d.get(2);
            double ycross = (p1.y-p0.y)*(p2.x-p1.x)-(p1.x-p0.x)*(p2.y-p1.y);
            // System.out.println("ycross = " + ycross);
            if(ycross >= 0){
                gl.glNormal3d(0,1,0);
            }
            else {
                gl.glNormal3d(0,-1,0);
            }
          for(Point2D p : pts2d) {
                gl.glTexCoord2d(scale*p.x/100, scale*p.y/100); //dodgy texture  scaling
                gl.glVertex3d(p.x, extra[0], p.y);
               // System.out.println(p);
            }
            gl.glEnd();
            gl.glPopMatrix();
       }
    }



    static final int EPSILON = 6;  /* distance for picking */

    /** return index of control point near to (x,y) or -1 if nothing near */
    public Point selectPoint(Point p) {
	double mind = Double.MAX_VALUE;
	selection = -1;
	for (int i = 0; i < pts2d.size(); i++){
	    Point2D p2 = pts2d.get(i);
	    double d = sqr(p2.x-p.x) + sqr(p2.y-p.y);
	    if (d < mind && d < EPSILON*EPSILON) {
		mind = d;
		selection = i;
	    }
	}

	if (selection == -1) {
	    return null;
	} else {
	    return ((pts2d.get(selection))).toPoint();
	}
    }

    // square of a double
    static double sqr(double x) {
	return x*x;
    }
  
    /** add a control point */
    public void addPoint(Point p) {
	pts2d.add(new Point2D(p));
	selection = pts2d.size() - 1;
    }
  
    /** find the closest edge of a polygon to p and select end point 
     and return position on that edge as number between 0 and 1*/
    protected double closestEdge(Polygon pl, Point p) {
	Point2D closest = null;
	int closesti;
	for (int i = 0; i < pl.npoints; i++){
	    int iplus = (i+1) % pl.npoints;
	    Edge2D e = new Edge2D(pl.xpoints[i],pl.ypoints[i],pl.xpoints[iplus],pl.ypoints[iplus]);
	    Point2D pe = e.toLineSpace(p);
	    if (pe.getX() > 0 && pe.getX() < 1 && Math.abs(pe.getY()) < EPSILON) {
		if (closest == null ||
		    Math.abs(pe.getY()) < Math.abs(closest.getY()) ) {
		    closest = pe;
		    selection = iplus;
		}
	    }

	}
	return (closest == null) ?  -1.0 : closest.getX();
    }

    /** set selected control point */
    public void setPoint(Point p) {
	if (selection >= 0) {
	    pts2d.set(selection, new Point2D(p));
	}
    }

    /** remove specified control point */
    public void removePoint() {
	if (selection >= 0) {
	    pts2d.remove(selection);

	}
	selection = -1;  //otherwise next control point becomes selected
    }

    /** Convert a List of Point2D to Polygon */
    public Polygon toPolygon(List<Point2D> ps){
	Polygon pts = new Polygon();
	for (Point2D p2 : ps){
	    Point p = p2.toPoint();
	    pts.addPoint(p.x,p.y);
	}
	return pts;
    }

    public boolean contains(int x,int y){
	return toPolygon(pts2d).contains(x,y);
    }

    public Vector3D collide(Vector3D from, Vector3D to){
	final double increment = 5; //how far to move avatar towards polygon if not on it
	if (contains((int) Math.round(to.x),(int) Math.round(to.z)) && to.y > extra[0]) {
	    //collision! move avatar towards correct height - Avatar.height above polygon's height
            if (to.y > extra[0] + Avatar.height + increment) {
		return new Vector3D(to.x,to.y-increment,to.z);
	    } else if (to.y < extra[0] + Avatar.height - increment) {
		return new Vector3D(to.x,to.y+increment,to.z);
	    } else {	    
		return new Vector3D(to.x,extra[0] + Avatar.height,to.z);
	    }
	} else {
	    return null;
	}
    }

    public void rotate(Point2D fixed, double angle){
	apply(Matrix2D.rotateAbout(fixed, angle));
    }

    public void scale(Point2D fixed, double xscale, double yscale){
	apply(Matrix2D.scaleAbout(fixed, xscale, yscale));
    }

    public void translate(int deltax, int deltay){
	apply(new Matrix2D(1,0,deltax,0,1,deltay));
    }

    public Point2D centre(){
	Point2D p0 = (pts2d.get(0));
	double minx, miny, maxx, maxy;
	minx = maxx = p0.x;
	miny = maxy = p0.y;
	for (Point2D p : pts2d){
	    if (p.x < minx){
		minx = p.x;
	    }
	    if (p.x > maxx){
		maxx = p.x;
	    }
	    if (p.y < miny){
		miny = p.y;
	    }
	    if (p.y > maxy){
		maxy = p.y;
	    }
	}
	return new Point2D((minx+maxx)/2,(miny+maxy)/2);
    }

    public void apply(Matrix2D t){
	for (int i = 0; i < pts2d.size(); i++){
	    Point2D p = t.apply(pts2d.get(i));
	    pts2d.set(i,p);
	}
    }


    public String toString() {
	StringBuffer result = new StringBuffer(super.toString());
	result.append(" " + pts2d.size());
	for (Point2D p : pts2d) {
	    result.append(" " + p);
	}
	return result.toString();
    }

 // Parse the coordinates
  public void fromTokens(StringTokenizer st) throws MalformedShapeException {
    try {
	selection = -1;
	int n = Integer.parseInt(st.nextToken());
	for (int i = 0; i < n; i++) {
	    pts2d.add(new Point2D(Double.parseDouble(st.nextToken()),
				  Double.parseDouble(st.nextToken())));
	}

    } 
    catch (NoSuchElementException e) {
      throw new MalformedShapeException(e.getMessage());
    } catch (NumberFormatException e) {
      throw new MalformedShapeException(e.getMessage());
    }
  }

}
