import java.awt.Point;
import java.util.*;
import java.io.*;

import javax.media.j3d.RenderingAttributes;
import javax.media.opengl.*;

/** This class represents a sequence of FPShape objects
@author Tim Lambert
*/

public class FPShapeList extends ArrayList<FPShape>{

    
    /** find a shape containing a point.
	If no such shape exists return null.
	@param p - point inside shape
	@returns shape containing p
     */
    public FPShape findShape(Point p) {
	FPShape r;
	for(int i = size()-1; i>= 0; i--)  { //search backwards so frontmost shape is found
	    r = get(i);
	    if (r.contains(p.x, p.y)) return r;
	}
	return null;
    }
    
    /** check for collision with any shape
	and return result of that shape's collide method
	If no collision return Vector3D to (because nothing stopped us from moving there).
	@param from - Vector3D we are moving from
	@param to - Vector3D we are moving to
	@returns Vector3D giving corrected position
     */
    public Vector3D collide(Vector3D from, Vector3D to) {
	Vector3D p;
	for(int i = size()-1; i>= 0; i--)  { //search backwards so frontmost shape is found
	    p = get(i).collide(from,to);
	    if (p != null) return p;
	}
	return to;
    }
    
    /** Paint all the shapes in the list
	@param g - Graphics context to use
     */
    public void paint(GL gl, GLDrawable glc) {
	for (FPShape o : this) {
	    o.paint(gl, glc);
	}
    }

    public void paintSelectEach(GL gl) {
	for (FPShape o : this) {
	    o.paintSelect(gl);
	}
    }


    public void render3D(GL gl, GLDrawable glc){
      for (FPShape o : this) {
          o.render3D(gl, glc);
      }
    }
    
    /**
     * Show all the lights depending on their switch condition
     */
    public void renderLights(GL gl, GLDrawable glc){
    	ArrayList<Light> l = this.getLights();
    	int i = 0, j = GL.GL_LIGHT1;
    	
    	for(i = 0; i < 7; i++,j++){
    		if(i > l.size()-1){break;}
    		
	    	if(l.get(i).isOn){
	    		float[] lightmat = l.get(i).fill.getColorComponents(null);;
	    		float[] lightpos = l.get(i).getPosition();
	        	gl.glLightfv(j, GL.GL_POSITION, lightpos, 0);
	        	gl.glLightfv(j, GL.GL_DIFFUSE, lightmat, 0);
	        	
	        	gl.glEnable(GL.GL_LIGHTING);
	        	gl.glEnable(j);
	    	}else{
	    		gl.glDisable(j);		
	    	}
	    	
    	}
    	
    }    

    
    

    // Function to get a list of lights from list.
    public ArrayList<Light> getLights(){
        ArrayList<Light> lightlist = new ArrayList<Light>();
      for (FPShape o : this) {
            if(o instanceof Light){
                lightlist.add((Light)o);
            }
        }
        return lightlist;
    }

   // Function to get a list of views from list
    public ArrayList<ViewPoint> getViews(){
        ArrayList<ViewPoint> viewlist = new ArrayList<ViewPoint>();
      for (FPShape o : this) {
            if(o instanceof ViewPoint){
                viewlist.add((ViewPoint)o);
            }
        }
        return viewlist;
    }

   // Function to get a list of doors from list
    public ArrayList<Door> getDoors(){
        ArrayList<Door> doorlist = new ArrayList<Door>();
      for (FPShape o : this) {
            if(o instanceof Door){
                doorlist.add((Door)o);
            }
        }
        return doorlist;
    }



  /** read FPShapes stored in a file into this ShapeList
@param sourcename - the name of the file
@throws IOException - if the file is not readable
@throws MalformedRegionException - if the file contains invalid data
   */

  public void read(String sourcename) throws IOException, MalformedShapeException{
    InputStream ins;
    String s="";

    /* Each line of the file contains the description of a region */
    ins = new FileInputStream(sourcename);
    BufferedReader in = new BufferedReader(new InputStreamReader(ins));
    while((s = in.readLine()) != null) {
      add(FPShape.create(s));
    }
  }

  /** this method writes the ShapeList to the specified file
@param filename - the name of the file to write it to
*/

  public void write(String filename) {
    try {
      File outputFile = new File(filename);
      
      PrintStream out = new PrintStream(new FileOutputStream(outputFile));
      for(int i = 0; i < size(); i++)  {
	out.println(get(i));
      }
      
      out.close();
    } catch (FileNotFoundException e) {
      System.err.println("File not found: " + e);
    } catch (IOException e) {
      System.err.println("IO exception: " + e);
    }
  }

} 
