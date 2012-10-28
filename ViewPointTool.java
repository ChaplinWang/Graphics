import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**  ViewPoint tool for graphical editor
@author Tim Lambert
  */

public class ViewPointTool extends WallTool{
    
    
    public ViewPointTool(FPEdit editor) {
	super(editor);
    }

    public String getName() {
	return "ViewPoint";
    }

    public String getMessage() {
	return "Click and drag to create a view point";
    }


    public Wall newWall(){
	return new ViewPoint();
    }

}
