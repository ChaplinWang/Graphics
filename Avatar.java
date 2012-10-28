import java.awt.*;
import java.util.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.*;
 
public class Avatar {
    public static final double height = 80;
    final double moveStep = 2;
    final double turnStep = 0.03;
    double turnIncrement;
    double moveIncrement;
    int turnframes;
    int moveframes;
    int swapView;
    int viewcount = 0;
    Vector3D pos;
    Vector3D dir;
    
    float t = 0;
    public Avatar(Vector3D pos, Vector3D dir){
	this.pos = pos;
	this.dir = dir;
    }
    


    //move forward or back for given number of frames
    public void move(int noframes){
        if (noframes > 0) {
        	moveIncrement = moveStep;
        } else {
        	moveIncrement = -moveStep;
        }
        moveframes = Math.abs(noframes);
    }

    
    //turn left or right for given number of frames
    public void turn(int noframes){
        if (noframes > 0) {
        	turnIncrement = turnStep;
        } else {
        	turnIncrement = -turnStep;
        }
        turnframes = Math.abs(noframes);
    }



	public void changeView() {
		viewcount++;
		swapView = 1;	
	}
	
    //update position, checking for collisions
    public void advancePos(FPShapeList shapeList){
		Vector3D newpos;
	
	    if (moveframes > 0) {
	    	
	    	newpos = pos.add(dir.scale(moveIncrement));
	    	moveframes--;
	    } else {
	    	newpos = pos; //not moving
	    }
		
	    if(turnframes > 0){
	    	double angle =Math.atan2(dir.x, dir.z);
	    	angle += turnIncrement;
	    	
	    	dir.x = Math.sin(angle);
	    	dir.z = Math.cos(angle);
	    	dir.y = 0;

	    	turnframes--;   
	    }
	        
	      
	    if(swapView == 1 && shapeList.getViews().size() != 0){
	    	int next = viewcount%shapeList.getViews().size();
	    	ViewPoint nextView = shapeList.getViews().get(next);
	    	pos = nextView.getViewerPos();
	    	dir = nextView.getViewerDir();
	    	
	    	//pos.y = 80;
	    	newpos = pos;
	    	swapView = 0;
	    }
 
		pos = shapeList.collide(pos,newpos);
	        	        
    }

    public Vector3D currentPos(){
        return pos;
    }
     
    public Vector3D currentDir(){
        return dir;
    }


    public void setColor(GL gl, Color c){
	gl.glColor3ub((byte)c.getRed(),(byte)c.getGreen(),(byte)c.getBlue());
    }

    public void render3D(GL gl, GLDrawable glc){
        setColor(gl,Color.white);
        gl.glDisable( GL.GL_TEXTURE_2D );

        gl.glPushMatrix();
             
        gl.glTranslated(pos.x, pos.y-height/2, pos.z);

        Vector3D.lookAt(gl, new Vector3D(0, 0, 0), dir, new Vector3D(0,1,0));

        gl.glScaled(5,height,25);

        GLUT glut = new GLUT();
        glut.glutSolidCube(1);
        
        gl.glPopMatrix();
   }



}
