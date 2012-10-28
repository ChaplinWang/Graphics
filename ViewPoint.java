/** This class represents a viewpiont */
import java.awt.*;
import java.util.*;
import javax.media.opengl.*;


public class ViewPoint extends Wall {

    public ViewPoint(){
    }

    /* construct a viewpoint given position and direction */
    public ViewPoint(Vector3D position, Vector3D direction){
	
	pts2d.add(new Point2D(position.x,position.z));
	extra[0] = position.y;

	Vector3D look = position.add(direction);
	pts2d.add(new Point2D(look.x,look.z));
	extra[1] = look.y;
    }


    public float lineWidth() {
	return 1.0f;
    }

    public Vector3D getViewerPos(){
        Point2D p0 = pts2d.get(0);
        return new Vector3D(p0.x,extra[0],p0.y);
    }

    public Vector3D getViewerDir(){
        Point2D p1 = pts2d.get(1);
	Vector3D look = new Vector3D(p1.x,extra[1],p1.y);
        Vector3D viewdir = look.subtract(getViewerPos());
        return viewdir.normalize();
    }

    public void render3D(GL gl, GLDrawable glc){
    }

    /** paint the viewpoint as a line with an arrow*/
    public void paint(GL gl, GLDrawable glc){
	double length = 10; //length of arrow head
        double angle = Math.PI/4; //angle between arrow head and body
	if (fill != null){
	    setColor(gl,fill);
	    gl.glDisable( GL.GL_TEXTURE_2D );
	}
	gl.glLineWidth(lineWidth());
        Point2D start = pts2d.get(0);
        Point2D end = pts2d.get(1);
        double linelength =  end.subtract(start).length();
        if (linelength == 0.0) {
	    return; //nothing to draw
	}
        // length units back along line from end
        Point2D tip = end.interp(start,length/linelength);
        Point2D leftTip = Matrix2D.rotateAbout(end,angle).apply(tip);
        Point2D rightTip = Matrix2D.rotateAbout(end,-angle).apply(tip);
        
	gl.glBegin( GL.GL_LINE_STRIP);  //draw the arrow
	    gl.glVertex2d(start.x, start.y);
	    gl.glVertex2d(end.x, end.y);
	    gl.glVertex2d(leftTip.x, leftTip.y);
	    gl.glVertex2d(rightTip.x, rightTip.y);
	    gl.glVertex2d(end.x, end.y);
	gl.glEnd(); 
    }

  
}
