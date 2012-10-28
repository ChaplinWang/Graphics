/** This class represents a door */
import java.awt.*;
import java.util.*;
import javax.media.opengl.*;

import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;

// Door implementation similar to Wall
public class Door extends Wall {

    public float lineWidth() {
	return 2.0f;
    }
    // Door flags
    private double doorangle = Math.PI/2; // pi/2-open 0-pi/2 moving 0-closed
    private double doormove = Math.PI/100;
    public int direction = 1; 			  // 0 stay 1 opening -1 closing
    public boolean move_door = false;
    
    
    /** paint this door into g.*/
    public void paint(GL gl, GLDrawable glc){

	if (fill != null){
	    setColor(gl,fill);
	    gl.glDisable( GL.GL_TEXTURE_2D );
	}
	gl.glLineWidth(lineWidth());
	gl.glBegin( GL.GL_LINE_STRIP );

	Point2D centre = (Point2D)(pts2d.get(0));
	Point2D rad = (Point2D)(pts2d.get(1));
	double dx = rad.x - centre.x;
	double dy = rad.y - centre.y;
	double radius = Math.sqrt(dx*dx + dy*dy);
	double theta = Math.atan2(dy,dx);

	gl.glVertex2d(centre.x, centre.y);
	for (int i = 0; i< 10; i++){
	    double angle = theta + (i/10.0)*Math.PI/2.0;
	    gl.glVertex2d(centre.x+radius*Math.cos(angle),
			  centre.y+radius*Math.sin(angle));
	}
	gl.glEnd(); 
    }

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
            gl.glBegin( GL.GL_POLYGON );  //draw the door

            Point2D p0 =  pts2d.get(0);	  //getting control points
            Point2D p1 =  pts2d.get(1);
            double dx,dy;
            dx = p1.x - p0.x; 
            dy = p1.y - p0.y;
            double angle = Math.atan2(dy, dx);
            double dist = Math.sqrt((p0.x - p1.x)*(p0.x - p1.x) + (p0.y - p1.y)*(p0.y - p1.y));
            
            
            if(move_door){
            	if(direction == 1){
            		doorangle = doorangle - doormove;
            		angle = angle - doormove;
            		if(doorangle <=  0){
            			direction = -1;
            			move_door = !move_door;
            		}
            	}else if(direction == -1){
                	doorangle = doorangle + doormove;
                	angle = angle + doormove;
                	if(doorangle >=  Math.PI/2){
                		direction = 1;
                		move_door = !move_door;
                	}	
            	}
            	
            	p1.x = p0.x + dist * Math.cos(angle);
            	p1.y = p0.y + dist * Math.sin(angle);
            	
            }
            
            Vector3D t = new Vector3D(p1.x-p0.x,0,p1.y-p0.y);
            Vector3D n = new Vector3D(0,1,0);
            Vector3D cross = n.cross(t);
            gl.glNormal3d(cross.x, cross.y, cross.z);
            
            //draw 4 points of the door
            double length = scale*dist/100;
            double height = scale*Math.abs(extra[1] - extra[0])/100;
            
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

}
