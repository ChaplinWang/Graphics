/** This class implents 2D affine transformation matrices
 */
import java.awt.*;

public class Matrix2D {
    // the last row is always 0 0 1, so just need to specify six numbers
    // giving the first two rows.
  
    private double xx,xy,xw;
    private double yx,yy,yw;
    // last row is  0, 0, 1
    public Matrix2D(double xx, double xy, double xw,
		    double yx, double yy, double yw) {
	this.xx = xx;
	this.xy = xy;
	this.xw = xw;
	this.yx = yx;
	this.yy = yy;
	this.yw = yw;
    }

    public Matrix2D() {
	this(0,0,0,0,0,0);
    }

    // composition of two transformations - the resulting of first doing this
    // one and then the one supplied as a parameter.

    public Matrix2D compose(Matrix2D m) {
	Matrix2D result = new Matrix2D();
	result.xx = m.xx*xx + m.xy*yx;
	result.xy = m.xx*xy + m.xy*yy;
	result.xw = m.xx*xw + m.xy*yw + m.xw;
	result.yx = m.yx*xx + m.yy*yx;
	result.yy = m.yx*xy + m.yy*yy;
	result.yw = m.yx*xw + m.yy*yw + m.yw;
	return result;
    }

    // apply a transformation to a point

    public Point apply(Point p) {
	return new Point((int)Math.round(xx*p.x + xy*p.y + xw),
			 (int)Math.round(yx*p.x + yy*p.y + yw));
    }

    public Point2D apply(Point2D p) {
	return new Point2D(xx*p.x + xy*p.y + xw,
			   yx*p.x + yy*p.y + yw);
    }


    //return rotation about point
    public static Matrix2D rotateAbout(Point2D c,double angle){
	return (new Matrix2D(1,0,-c.x,0,1,-c.y)).
	    compose(new Matrix2D(Math.cos(angle),-Math.sin(angle),0,
			       Math.sin(angle),Math.cos(angle),0)).
	    compose(new Matrix2D(1,0,c.x,0,1,c.y));
    }

    //return scale about point
    public static Matrix2D scaleAbout(Point2D c,double sx,double sy){
	return (new Matrix2D(1,0,-c.x,0,1,-c.y)).
	    compose(new Matrix2D(sx,0,0,
			       0,sy,0)).
	    compose(new Matrix2D(1,0,c.x,0,1,c.y));
    }
		  

    // useful for debugging

    public String toString() {
	return xx + " " + xy + " " + xw + " | " +
	    yx + " " + yy + " " + yw;
    }

}
