
 package ogcgame;
 import jgame.*;
 import jgame.platform.*;
 import opengamecreator.lib.*;
   public class Objectball extends OGCObject {		public Objectball (OGCGame g, double x, double y) {
			// Initialise game object by calling an appropriate constructor
			// in the JGObject class.
			super(
				"myobject",// name by which the object is known
				true,//true means add a unique ID number after the object name.
				     //If we don't do this, this object will replace any object
				     //with the same name.
				x,  // X position
				y, // Y position
				1, // the object's collision ID (used to determine which classes
				   // of objects should collide with each other)
				"301a6321b141d0405ab190756a69d600", // name of sprite or animation to use (null is none)
                         g			);
			// Give the object an initial speed in a random direction.
//			xspeed = random(-2,2);
//			yspeed = random(-2,2);
                 setup();		}



        /** Draw the object. */
//        public void paint() {
//            // Draw a yellow ball
//            setColor(JGColor.yellow);
//            drawOval(x,y,16,16,true,true);
//        }
public void move() {
}public void setup() {
}	public void drawUserF0() {
		setColour((OGCGame.colourFromString("#FFFFFF")));
		drawOval(50, 50, 10, 10, true, false);
	}
public void paint() {
drawUserF0();}}