import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**  Polygon tool for graphical editor
@author Tim Lambert
  */

public class WindowTool extends WallTool{
    
    
    public WindowTool(FPEdit editor) {
	super(editor);
    }

    public String getName() {
	return "Window";
    }

    public String getMessage() {
	return "Click and drag to create a window";
    }


    public Wall newWall(){
	return new Window();
    }

}
