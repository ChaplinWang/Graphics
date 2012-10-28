import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**  Polygon tool for graphical editor
@author Tim Lambert
  */

public class DoorTool extends WallTool{
    
    
    public DoorTool(FPEdit editor) {
	super(editor);
    }

    public String getName() {
	return "Door";
    }

    public String getMessage() {
	return "Click and drag to create a door";
    }


    public Wall newWall(){
	return new Door();
    }

}
