/** This class represents a texture.
    It tries to hide all the gory details needed to initialize
    things so they work with JOGL.
    Much code copied from Kevin Duling (jattier@hotmail.com) implementation
    of NeHe lesson 6
*/
import java.awt.*;
import java.io.*;
import java.util.*;
import java.net.*;

import javax.media.opengl.*;
import com.sun.opengl.util.*;
import com.sun.opengl.util.texture.*;

public class MyTexture{


    String filename; //file texture is in

    /** texture for each GLDrawable */
    /** Ought to use shared contexts but they are not guarenteed to work
        so we load the texture for each drawable */
    HashMap<GLDrawable,Texture> textures = new HashMap<GLDrawable,Texture>();
    Texture texture; //actual texture


    public MyTexture(String filename) {
	this.filename = filename;
    }
  

    /** Retrieve a URL resource from the jar.  If the resource is not found, then
     * the local disk is also checked.
     * @param filename Complete filename, including parent path
     * @return a URL object if resource is found, otherwise null.
     */  
	
    private URL getResource(String filename) throws IOException {
	// Try to load resource from jar
	URL url = ClassLoader.getSystemResource(filename);
	// If not found in jar, then load from disk
	if (url == null) {
	    return new URL("file", "localhost", filename);
	} else {
	    return url;
	}
    }
	
    /** Load a texture from a file */

    Texture loadTexture(String filename) {
	String fileType = null;
	Texture texture = null;
	URL url = null;
		
	try {
	    url = getResource(filename);
	} catch (IOException e) {
	    e.printStackTrace();
	}
		
	if (filename.toLowerCase().endsWith(".png")) fileType = TextureIO.PNG;
	else if (filename.toLowerCase().endsWith(".gif")) fileType = TextureIO.GIF;
	else if (filename.toLowerCase().endsWith(".jpg")) fileType = TextureIO.JPG;
	else if (filename.toLowerCase().endsWith(".tiff")) fileType = TextureIO.TIFF;
	else if (filename.toLowerCase().endsWith(".tif")) fileType = TextureIO.TIFF;
	else if (filename.toLowerCase().endsWith(".tga")) fileType = TextureIO.TGA;
	else if (filename.toLowerCase().endsWith(".rgb")) fileType = TextureIO.SGI;
		
	if (fileType == null) {
	    System.err.println("son't know how to load images of file type: " + filename);
	    System.exit(1);
	}
	try {
	    texture = TextureIO.newTexture(url, true, fileType);
	} catch (GLException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return(texture);
    }

    /** get a Texture from a MyTexture, loading if necessary */ 

    public Texture getTexture(GLDrawable glc) {
	Texture texture = textures.get(glc);
	if (texture == null) {
	    String pathname = TextureChoice.dirname+File.separator+filename;
	    texture = loadTexture(pathname);
	    textures.put(glc,texture);
	}
	//set up texture tranform so that (0,0) is bottom left corner and
        // (1,1) is top right corner of texture.
	GL gl = ((GLAutoDrawable)glc).getGL();
        gl.glMatrixMode(GL.GL_TEXTURE);
        gl.glLoadIdentity();
	TextureCoords tc = texture.getImageTexCoords();
	gl.glTranslated(tc.left(),tc.bottom(),0);
	gl.glScaled(tc.right()-tc.left(),tc.top()-tc.bottom(),1);
        gl.glMatrixMode(GL.GL_MODELVIEW);

	return texture;
    }

    public String toString(){
	return filename;
    }

}
