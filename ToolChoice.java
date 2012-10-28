import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/**  This class provides the choices between the various tools.
@author Tim Lambert
  */

public class ToolChoice extends JComboBox implements ItemListener{
    Map<String,Tool> tools;
    FPEdit editor;
    
    public ToolChoice(FPEdit editor) {
	
	this.editor = editor;
        addItemListener(this);
	// Create tool menu and hashtable that maps from menu to tools
	Tool[] toolarray = { new PolygonTool(editor),
			     new WallTool(editor),
			     new WindowTool(editor),
			     new DoorTool(editor),
			     new PortalTool(editor),
			     new LightTool(editor),
			     new ViewPointTool(editor),
			     new TranslateTool(editor),
			     new RotateTool(editor),
			     new ScaleTool(editor),
			     new ReshapeTool(editor),
			     new SelectTool(editor)};
	tools = new HashMap<String,Tool>();
	for (int i = 0; i < toolarray.length; i++) {
	    tools.put(toolarray[i].getName(),toolarray[i]); 
	    addItem(toolarray[i].getName());
	} 
	editor.setTool((Tool)tools.get(getSelectedItem()));
    }
    
    //called when the user changes tools
    public void itemStateChanged(ItemEvent e) {
	Tool tool = (Tool)tools.get(getSelectedItem());
	if (tool != null) {
	    editor.setTool(tool);
	}
    }

}
