import java.awt.Point;
/** This class represents a 2D edge made of two Point2Ds
  */

public class Edge2D {
    private Point2D start,end;
    private Matrix2D toLineSpaceT;
    public Edge2D(Point2D start, Point2D end) {
	this.start = start;
	this.end = end;
	double dx = end.x - start.x;
	double dy = end.y - start.y;
	double length = Math.sqrt(dx*dx + dy*dy);
        toLineSpaceT = (new Matrix2D(1,0,-start.x,0,1,-start.y)).
	    compose(new Matrix2D(dx,dy,0,-dy,dx,0)).
	    compose(new Matrix2D(1/(length*length),0,0,0,1/length,0));

    }
    public Edge2D(int x1, int y1, int x2, int y2) {
	this(new Point2D(x1,y1),new Point2D(x2,y2));
    }

    public String toString(){
	return start+"-"+end;
    }
    
    /* transform point to line space (t,s)
       where s is distance from line and t tells you how far you
       are along line
    */
    public Point2D toLineSpace(Point p){
	return toLineSpaceT.apply(new Point2D (p));
    }
}
    
