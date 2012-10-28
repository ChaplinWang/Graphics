/** This class represents a 2D point.  Like Point of java.awt, but
  uses floats instead of ints.
  */
import java.awt.Point;
import java.io.*;

public class Point2D implements Serializable{
    double x,y;
    
    public Point2D(double x, double y) {
	this.x = x;
	this.y = y;
    }
    
    public Point2D(Point p) {
	this(p.x,p.y);
    }
    
    public double getX(){
	return x;
    }
    
    public double getY(){
	return y;
    }
    
    //interpolate this + t*(q-this)
    public Point2D interp(Point2D q,double t){
	return new Point2D(x + t*(q.x-x), y + t*(q.y-y));
    }

    //subtract q from this point
    public Point2D subtract(Point2D q){
	return new Point2D(x-q.x, y-q.y);
    }

    //add q from this point
    public Point2D add(Point2D q){
	return new Point2D(x+q.x, y+q.y);
    }

    //multiply by a scalar
    public Point2D scale(double s){
	return new Point2D(s*x, s*y);
    }

    //length of vector
    public double length(){
	return Math.sqrt(x*x+y*y);
    }

    public String toString(){
	return x+" "+y;
    }
    
    
    public Point toPoint(){
	return new Point((int)Math.round(x),(int)Math.round(y));
    }
}
