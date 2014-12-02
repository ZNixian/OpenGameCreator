/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.compiler;

/**
 *
 * @author Campbell Suter
 */
public class Constants {

    private Constants() {
    }
    public static final String imports = ""
            + "\n package ogcgame;"
            + "\n import jgame.*;"
            + "\n import jgame.platform.*;"
            + "\n import opengamecreator.lib.*;"
            + "\n";
    public static final String mainHead =
            ""
            + "\npublic class Main extends OGCGame {"
            + "\n    public static void main(String [] args) {"
            + "\n        new Main(new JGPoint(640,480));"
            + "\n    }"
            + "\n"
            + "\n    public Main() {"
            + "\n    // This inits the engine as an applet."
            + "\n        initEngineApplet(); "
            + "\n    }"
            + "\n"
            + "\n    public Main(JGPoint size) {"
            + "\n        // This inits the engine as an application."
            + "\n        initEngine(size.x,size.y); "
            + "\n    }"
            + "\n"
            + "\n"
            + "\n    public void doFrame() {"
            + "\n        // Move all objects."
            + "\n        moveObjects("
            + "\n            null,// object name prefix of objects to move (null means any)"
            + "\n            0    // object collision ID of objects to move (0 means any)"
            + "\n        );"
            + "\n    }"
            + "\n"
            + "\n"
            + "\n"
            + "\n    @Override"
            + "\n    public void initGame() {"
            + "\n        setFrameRate(35, 2);"
            + "\n        defineMedia(\"media.tbl\");"
            + "\n        // create some game objects"
            + "\n        // (see below for the class definition)"
            + "\n//        for (int i = 0; i < 20; i++) {"
            + "\n//            new ObjectTurtles(this);"
            + "\n//        }"
            + "\n        usersetup();"
            + "\n    }";
    public static final String objectClassHead = ""
            + "   public class %%%classname%%% extends OGCObject {"
            + "		public %%%classname%%% (OGCGame g, double x, double y) {\n"
            + "			// Initialise game object by calling an appropriate constructor\n"
            + "			// in the JGObject class.\n"
            + "			super(\n"
            + "				\"myobject\",// name by which the object is known\n"
            + "				true,//true means add a unique ID number after the object name.\n"
            + "				     //If we don't do this, this object will replace any object\n"
            + "				     //with the same name.\n"
            + "				x,  // X position\n"
            + "				y, // Y position\n"
            + "				1, // the object's collision ID (used to determine which classes\n"
            + "				   // of objects should collide with each other)\n"
            + "				%%%spritename%%%, // name of sprite or animation to use (null is none)\n"
            + "                         g"
            + "			);\n"
            + "			// Give the object an initial speed in a random direction.\n"
            + "//			xspeed = random(-2,2);\n"
            + "//			yspeed = random(-2,2);\n"
            + "                 setup();"
            + "		}\n"
            + "\n"
            + "\n"
            + "\n        /** Draw the object. */"
            + "\n//        public void paint() {"
            + "\n//            // Draw a yellow ball"
            + "\n//            setColor(JGColor.yellow);"
            + "\n//            drawOval(x,y,16,16,true,true);"
            + "\n//        }"
            + "\n";
}
