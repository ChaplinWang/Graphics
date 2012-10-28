import java.awt.Color;

import javax.media.opengl.GL;
import javax.media.opengl.GLDrawable;

import com.sun.opengl.util.texture.Texture;

/** This class represents a window */

// Extending wall class, this is basically similar to wall 
public class Window extends Wall {

    public float lineWidth() {
    	return 2.0f;
    }

    
    // render the window associate 2 polygons
    // rendering them separately
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
            gl.glBegin( GL.GL_POLYGON );  //draw the polygon

            Point2D p0 =  pts2d.get(0);
            Point2D p1 =  pts2d.get(1);
        
            Vector3D t = new Vector3D(p1.x-p0.x,0,p1.y-p0.y);
            Vector3D n = new Vector3D(0,1,0);
            Vector3D cross = n.cross(t);
            gl.glNormal3d(cross.x, cross.y, cross.z);
            
            
            double length = scale*Math.sqrt((p0.x - p1.x)*(p0.x - p1.x) + (p0.y - p1.y)*(p0.y - p1.y))/100;
            double height = scale*Math.abs(extra[1] - extra[0])/100;
            
            //draw the 4 points of the windows
            
            // this this the bottom part of window
            gl.glTexCoord2d(0,0); 
            gl.glVertex3d(p0.x, extra[0], p0.y);
            gl.glTexCoord2d(0, height/3);
            gl.glVertex3d(p0.x, extra[0] + (extra[1]- extra[0])/3, p0.y);
            gl.glTexCoord2d(length, height/3);
            gl.glVertex3d(p1.x, extra[0] + (extra[1]- extra[0])/3, p1.y);
            gl.glTexCoord2d(length,0);
            gl.glVertex3d(p1.x, extra[0], p1.y);
            
            gl.glEnd();
            
            // There is a hole in middle of window
            
            // this this the top part of window
            gl.glBegin( GL.GL_POLYGON );
            gl.glTexCoord2d(0,height*2/3); 
            gl.glVertex3d(p0.x, extra[0] + (extra[1] - extra[0])*2/3, p0.y);
            gl.glTexCoord2d(0, height);
            gl.glVertex3d(p0.x, extra[1], p0.y);
            gl.glTexCoord2d(length, height);
            gl.glVertex3d(p1.x, extra[1], p1.y);
            gl.glTexCoord2d(length,height*2/3);
            gl.glVertex3d(p1.x, extra[0] + (extra[1] - extra[0])*2/3, p1.y);
            
            gl.glEnd();
            gl.glPopMatrix();
       }
  }
    

}
