
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
				"daf280af792fd5b906511363ae2bc39d", // name of sprite or animation to use (null is none)
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
	public void onframe0() {
		fd((10/10));
if((y<x)) {
		down(20);

}
	}
public void move() {
onframe0();}	public void setup0() {
	}
public void setup() {
setup0();}public void paint() {
}}