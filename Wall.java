/** This class represents a wall */
import java.awt.*;
import java.util.*;
import javax.media.opengl.*;

import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;

public class Wall extends FPPolygon {


    public String extraName(int i){
	return i==0 ? "Base" : "Top";
    }

    public float lineWidth() {
	return 4.0f;
    }

    /** paint the wall using gl.*/
    public void paint(GL gl, GLDrawable glc){

	if (fill != null){
	    setColor(gl,fill);
	    gl.glDisable( GL.GL_TEXTURE_2D );
	}
	gl.glLineWidth(lineWidth());
	gl.glBegin( GL.GL_LINES );  //draw the wall
	for(int i = 0; i < 2; i++) {
	    Point2D p = (Point2D)(pts2d.get(i));
	    gl.glVertex2d(p.x, p.y);
	}
	gl.glEnd(); 
    }

    // 4 point poligon redering is the wall
      public void render3D(GL gl, GLDrawable glc){
          if (pts2d.size() < 2) return;
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
              gl.glBegin( GL.GL_POLYGON );  
              //draw the polygon

              Point2D p0 =  pts2d.get(0);
              Point2D p1 =  pts2d.get(1);
          
              Vector3D t = new Vector3D(p1.x-p0.x,0,p1.y-p0.y);
              Vector3D n = new Vector3D(0,1,0);
              Vector3D cross = n.cross(t);
              gl.glNormal3d(cross.x, cross.y, cross.z);
              
              // Texture adjust vars
              double length = scale*Math.sqrt((p0.x - p1.x)*(p0.x - p1.x) + (p0.y - p1.y)*(p0.y - p1.y))/100;
              double height = scale*Math.abs(extra[1] - extra[0])/100;
              
              //draw the 4 points of the wall
              gl.glTexCoord2d(0,0); 
              gl.glVertex3d(p0.x, extra[0], p0.y);
              gl.glTexCoord2d(0, height);
              gl.glVertex3d(p0.x, extra[1], p0.y);
              gl.glTexCoord2d(length, height);
              gl.glVertex3d(p1.x, extra[1], p1.y);
              gl.glTexCoord2d(length,0);
              gl.glVertex3d(p1.x, extra[0], p1.y);
              
              gl.glEnd();
              gl.glPopMatrix();
         }
    }

    /** add a control point */
    public void addPoint(Point p) {
	if (pts2d.size() < 2) {
	    super.addPoint(p);
	}
    }
  

    /** remove specified control point */
    public void removePoint() {
    	return;
    }

    public boolean contains(int x,int y){
	Edge2D e = new Edge2D((Point2D)pts2d.get(0),
			      (Point2D)pts2d.get(1));
	Point2D pe = e.toLineSpace(new Point(x,y));
	return (pe.getX() > 0 && pe.getX() < 1 && Math.abs(pe.getY()) < EPSILON);
    }

    
    public Vector3D collide(Vector3D from, Vector3D to){
	
    	if(intersect(from, to)){
    		return from;
    	}
    	else
	    return null;
    }
    
    
    // calculate if the walking path go though the wall
    public boolean intersect (Vector3D from, Vector3D to){
    	
    	Point2D p0 = pts2d.get(0);
    	Point2D p1 = pts2d.get(1);
    	
    	Point2D p2 = new Point2D(from.x, from.z);
    	Point2D p3 = new Point2D(to.x, to.z);
    	   			
    	
    	double a1 = p1.y - p0.y;
    	double b1 = p0.x - p1.x;
    	double c1 = p0.x * p1.y - p1.x * p0.y;
    	
    	double a2 = p3.y - p2.y;
    	double b2 = p2.x - p3.x;
    	double c2 = p2.x * p3.y - p3.x * p2.y;
    	
    	// the lines are parallel no possible blocking
    	if((a2*b1 - a1*b2) == 0){
    		return false;
    	}
    	
    	double x = (b2*c1 - b1*c2)/(a1*b2 - a2*b1);
    	double y = (a1*c2 - a2*c1)/(a1*b2 - a2*b1);
    	// the robot's head is lower than the wall so it can go through
    	if(to.y < extra[0]){
    		return false;
    	}
    	// the robot's foot is higher than the wall so it can go through
    	if(to.y - Avatar.height > extra[1]){
    		return false;
    	}
    	
    	// the robot cannot go through it
    	if(between(p0, p1, x, y) && between(p2, p3, x, y)){
    		return true;
    	}else
  
        // all other conditions it doesn't block
		return false;
    	
    	
    }
    
    
    // test if a point is in between 
    public boolean between(Point2D p0, Point2D p1, double x, double y){

    	if(x > p0.x  && x < p1.x  ){
    		return true;
    	}
    	else if(x > p1.x  && x < p0.x){
    		return true;
    	}else if(p0.x == p1.x && x == p0.x){
    		
        	if(y > p0.y  && y < p1.y  ){
        		return true;
        	}
        	else if(y > p1.y  && y < p0.y){
        		return true;
        	}
        	return false;
    	}
    	
    	else
		return false;	
    }
 
}
