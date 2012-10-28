/** This class represents a wall */
import java.awt.*;
import java.util.*;
import javax.media.opengl.*;

import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;

public class Portal extends Wall {

    public final double portalHeight = 100.0;

    public String extraName(int i){
	return i==0 ? "Start Height" : "End Height";
    }

    public float lineWidth() {
	return 4.0f;
    }

    /** paint the portal using gl.*/
    public void paint(GL gl, GLDrawable glc){

	if (fill != null){
	    setColor(gl,fill);
	    gl.glDisable( GL.GL_TEXTURE_2D );
	}
	gl.glLineWidth(lineWidth());
	gl.glBegin( GL.GL_LINES );  //draw each wall
	for(int i = 0; i < pts2d.size(); i++) {
	    Point2D p = (Point2D)(pts2d.get(i));
	    gl.glVertex2d(p.x, p.y);
	}
	gl.glEnd(); 

        if (pts2d.size() == 4) {
	    /* Connect mid point of the two line segments with dotted line*/
	    gl.glLineStipple(1, (short) 0x0F0F);
	    gl.glEnable(GL.GL_LINE_STIPPLE);
	    gl.glLineWidth(1);
	    gl.glBegin( GL.GL_LINES ); 
	    Point2D mid =  pts2d.get(0).interp(pts2d.get(1),0.5);
	    gl.glVertex2d(mid.x, mid.y);
	    mid =  pts2d.get(2).interp(pts2d.get(3),0.5);
	    gl.glVertex2d(mid.x, mid.y);
	    gl.glEnd(); 
	    gl.glDisable(GL.GL_LINE_STIPPLE);
	}
    }

    public void render3D(GL gl, GLDrawable glc){
        if (pts2d.size() < 4) return;
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
            
            // get 4 control points of portal
            Point2D p0 =  pts2d.get(0);
            Point2D p1 =  pts2d.get(1);
            Point2D p2 =  pts2d.get(2);
            Point2D p3 =  pts2d.get(3); 
            
            // render 1st side of portal with height extra[0]
            renderOneside(p0, p1, gl, extra[0]);
            
         // render 1st side of portal with height extra[1]
            renderOneside(p2, p3, gl, extra[1]);
  
            gl.glPopMatrix();
       }
  }
   private void renderOneside(Point2D a, Point2D b, GL gl, double d){
	   gl.glClear(0);
	   Vector3D t = new Vector3D(b.x-a.x,0,b.y-a.y);
       Vector3D n = new Vector3D(0,1,0);
       Vector3D cross = n.cross(t);
       gl.glNormal3d(cross.x, cross.y, cross.z);
       
       
       // Texture adjustment vars
       double length = scale*Math.sqrt((a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y))/100 + 0.1;
       double height = scale;
       
       
       //draw the 4 points of it
       gl.glBegin( GL.GL_POLYGON );
       gl.glTexCoord2d(0,0); 
       gl.glVertex3d(a.x, d, a.y);
       gl.glTexCoord2d(0, height);
       gl.glVertex3d(a.x, d+ 100, a.y);
       gl.glTexCoord2d(length, height);
       gl.glVertex3d(b.x, d+ 100, b.y);
       gl.glTexCoord2d(length,0);
       gl.glVertex3d(b.x, d, b.y);
       gl.glEnd();
	   
   }

    
   public Vector3D collide(Vector3D from, Vector3D to){

   	if(intersect(from, to)){
   		
   		Point2D p0 = pts2d.get(2);
   		Point2D p1 = pts2d.get(3);
   		
   		// if collide with 1st portal just fly to second position
   		Vector3D v = new Vector3D((p0.x + p1.x)/2, extra[1] + Avatar.height, (p0.y + p1.y)/2);
   		return v;
   	}
   	else
	    return null;
   }
   
    /** add a control point */
    public void addPoint(Point p) {
	if (pts2d.size() < 4) {
	    super.addPoint(p);
	}
    }
  

    /** remove specified control point - not allowed for a portal*/
    public void removePoint() {
	return;
    }
    
    //utility for contains takes an offset so we can check the two parts of the portal
    public boolean contains(int offset, int x,int y){
	Edge2D e = new Edge2D(pts2d.get(offset), pts2d.get(offset+1));
	Point2D pe = e.toLineSpace(new Point(x,y));
	return (pe.getX() > 0 && pe.getX() < 1 && Math.abs(pe.getY()) < EPSILON);
    }

    public boolean contains(int x,int y){
	return contains(0,x,y) || contains(2,x,y) ;
    }


}
