import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.*;

/**  This application implements a Floor Plan map editor
@author Tim Lambert
  */

public class FPEdit extends JFrame {

    /** this frame */
    final JFrame frame;


  /** name of FP file being edited */
  String filename;

    /**The content pane*/
    Container content;

  /** The canvas used to display the picture */
  FPCanvas canvas;

  /** list of all the edit windows */
  static ArrayList<FPEdit> windows = new ArrayList<FPEdit>();

    /** file chooser */
    static JFileChooser chooser;

  /** status is used for displaying messages */
  JTextField status;

  /** active Tool */
  Tool currentTool;

    /** properties panel */
    PropertiesPanel properties;

  /** grid size input text field */
  JTextField gridSize;

    /** save menu item */
    JMenuItem save;

  /** The clipboard */
  static FPShape clipboard;

  /** An application needs a main method */
  public static void main(String[] args) {
      if (args.length > 0) {
	  for (int i = 0; i < args.length; i++) {
	      windows.add(new FPEdit(args[i]));
	  }
      } else {
	  windows.add(new FPEdit("untitled.fpe"));
      }
  }
    

  /* add a menu item and associate an ActionListener with it */
  private JMenuItem addMenuItem(JMenu m, String name, int key, int modifiers, ActionListener action) {
    JMenuItem mi = new JMenuItem(name);
    mi.setAccelerator(KeyStroke.getKeyStroke(key, modifiers));
    mi.addActionListener(action);
    m.add(mi);
    return mi;
  }

  /* add a menu item without a shortcut and associate an ActionLister with it */
  private JMenuItem addMenuItem(JMenu m, String name, ActionListener action) {
    JMenuItem mi = new JMenuItem(name);
    mi.addActionListener(action);
    m.add(mi);
    return mi;
  }

  /* add a button to a panel and associate an ActionLister with it */
  private void addButton(JPanel p, String name, ActionListener action) {
    JButton b = new JButton(name);
    b.addActionListener(action);
    p.add(b);
  }

  /** Create the JFrame editing the specified file
      @param title - name of FP file to be editted
  */

  public FPEdit(String title) {
    super(title); 
    filename = title;

    frame = (JFrame) this;
    content = getContentPane();
    
    // dispose this object if the window closes
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
	dispose();
      }
    });

    JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    
    // Add a menubar, with a File menu
    JMenuBar menubar = new JMenuBar();
    JMenu file = new JMenu("File");
    menubar.add(file);
    addMenuItem(file, "New", KeyEvent.VK_N, 0,
		new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	windows.add(new FPEdit("untitled.fpe"));
      }
    });
    addMenuItem(file, "Open", KeyEvent.VK_O, 0,
		new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	open();
      }
    });
    save =  addMenuItem(file, "Save", KeyEvent.VK_S, 0,
		new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	canvas.write(filename);
      }
    });
    save.setEnabled(! title.equals("untitled.fpe"));
    
    addMenuItem(file, "Save As", KeyEvent.VK_S, InputEvent.SHIFT_MASK,
		new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	saveAs();
      }
    });
    addMenuItem(file, "Close", KeyEvent.VK_W, 0,
		new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	dispose(); 
      }
    });
    addMenuItem(file, "Quit", KeyEvent.VK_Q, 0,
		new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	int answer = JOptionPane.showConfirmDialog(frame,
				      "Really quit?",
				      "Really quit?",
				      JOptionPane.YES_NO_OPTION);
	if (answer == JOptionPane.YES_OPTION) System.exit(0);
      }
    });

    JMenu edit = new JMenu("Edit");
    menubar.add(edit);
    addMenuItem(edit, "Cut", KeyEvent.VK_X, 0,
		new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	cut();
      }
    });
    addMenuItem(edit, "Copy", KeyEvent.VK_C, 0,
		new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	copy();
      }
    });
    addMenuItem(edit, "Paste", KeyEvent.VK_V, 0,
		new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	paste();
      }
    });
    addMenuItem(edit, "To front",
		new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	  tofront();
      }
    });
    addMenuItem(edit, "To back",
		new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	  toback();
      }
    });
    addMenuItem(edit, "Properties",
		new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	  editProperties();
      }
    });

    // create the file chooser
    if (chooser == null) {
	chooser = new JFileChooser("maps");
	// Note: source for ExampleFileFilter can be found in FileChooserDemo,
	// under the demo/jfc directory in the Java 2 SDK, Standard Edition.
	ExampleFileFilter filter = new ExampleFileFilter();
	filter.addExtension("fpe");
	filter.setDescription("Floor plans");
	chooser.setFileFilter(filter);
    }

    // add the status text field
    status = new JTextField();
    status.setEditable(false);


    // create the main drawing area
    canvas = new FPCanvas(title, this);
    canvas.setSize(500, 300);
    content.add("Center", canvas);

    //Panels to hold all the choices
    JPanel choices = new JPanel();
    choices.setLayout( new BorderLayout() );
    choices.add("South",properties = new PropertiesPanel(frame));
    JPanel row1 = new JPanel();

    // The tool choice
    ToolChoice toolchoice = new ToolChoice(this);
    row1.add(toolchoice);

    // Grid scale
    row1.add(new JLabel("Grid Size"));
    row1.add(gridSize = new JTextField("8",3));



    choices.add("Center",row1);


    // Create Help menu; add an item; add to menubar
    JMenu help = new JMenu("Help");
    addMenuItem(help, "About", KeyEvent.VK_A, 0,
		new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	JOptionPane.showMessageDialog(frame,
				      "This editor was written by Tim Lambert",
				      "About FP Editor",
				      JOptionPane.INFORMATION_MESSAGE);
      }
    });

    menubar.add(help);

    content.add("North",choices);
    // makes menubar the menubar of this frame.
    setJMenuBar(menubar);
    content.add("South",status);

    setSize(750,600);
    setVisible(true);
  }

  // override dispose so that if last window is closed we exit.
  public synchronized void dispose() {
    windows.remove(this);
    if (windows.isEmpty()) {
      super.dispose();
      System.exit(0);
    } else {
      super.dispose();
    }
  }



  /** open a FP file */
  void open() {
    int returnVal = chooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
	windows.add(new FPEdit(chooser.getSelectedFile().getPath()));
    }
  }

  /** save a FP file under different name*/
  void saveAs() {
    int returnVal = chooser.showSaveDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
	filename = chooser.getSelectedFile().getPath();
	canvas.write(filename);
	this.setTitle(filename);
    }
  }

    /* bring slected shape to front */
    void tofront() {
	FPShape selection = canvas.getSelection();
        if (selection != null) {
	    canvas.getShapes().remove(selection);
	    canvas.getShapes().add(selection);
	    canvas.repaint();
	}
  }

    /* push selected shape to back */
    void toback() {
	FPShape selection = canvas.getSelection();
        if (selection != null) {
	    canvas.getShapes().remove(selection);
	    canvas.getShapes().add(0,selection);
	    canvas.repaint();
	}
  }

    /* remove selected shape */
    void cut() {
      copy();
      canvas.getShapes().remove(canvas.getSelection());
      canvas.select(null);
      canvas.repaint();
  }

    void copy() {
	clipboard =  (FPShape) canvas.getSelection().clone();
    }

  void paste() {
    if (clipboard != null){
	FPShape clipcopy = (FPShape)clipboard.clone();
	canvas.getShapes().add(clipcopy);
	canvas.select(clipcopy);
	canvas.repaint();
    }
  }

    void editProperties(){
	if (canvas.getSelection() != null) {
	    Object[] options = {"Set",
				"Cancel"};
	    PropertiesPanel panel = new PropertiesPanel(frame);
	    panel.setProperties(canvas.getSelection());
	    int n = JOptionPane.showOptionDialog
		(frame,
		 panel,
		 "Change properties?",
		 JOptionPane.YES_NO_OPTION,
		 JOptionPane.QUESTION_MESSAGE,
		 null,
		 options,
		 options[0]);

	    if (n==0) { //User clicked on "Set"
		panel.getProperties(canvas.getSelection());
	    }		
	    canvas.repaint();
	}
    }
    
  /** Displays the argument string in the status window.
      @param msg - the string to display
  */

  public void showStatus(String msg) {
    status.setText(msg);
  }

  /** Return the canvas used to display the FP file being edited
      @returns canvas used to display the FP file being edited
  */

  public FPCanvas getCanvas() {
    return canvas;
  }

  /** Set the currently active Tool
      @param tool - tool to be notified about mouse events
  */

  public void setTool(Tool tool) {
    if (currentTool != null){
      canvas.removeMouseListener(currentTool);
      canvas.removeMouseMotionListener(currentTool);
      for (int i=0; i <= 1; i++){
	  currentTool.setExtra(i,properties.getExtra(i));
      }
    }
    currentTool = tool;
    canvas.addMouseListener(currentTool);
    canvas.addMouseMotionListener(currentTool);
    setCursor(tool.getCursor());
    showStatus(tool.getMessage());
    for (int i=0; i <= 1; i++){
	properties.setExtraLabel(i, tool.extraName(i));
	properties.setExtra(i, tool.getExtra(i));
    }
  }

    
    /** Get the properties from properties panel */
    public void getProperties (FPShape s){
	properties.getProperties(s);
    }

  /** Return the gridSize from the textfield
      @returns the gridSize from the textfield
  */

  public int getGridSize() {
    return Integer.parseInt(gridSize.getText());
  }

}
