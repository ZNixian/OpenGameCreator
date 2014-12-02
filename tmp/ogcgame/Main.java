
 package ogcgame;
 import jgame.*;
 import jgame.platform.*;
 import opengamecreator.lib.*;

public class Main extends OGCGame {
    public static void main(String [] args) {
        new Main(new JGPoint(640,480));
    }

    public Main() {
    // This inits the engine as an applet.
        initEngineApplet(); 
    }

    public Main(JGPoint size) {
        // This inits the engine as an application.
        initEngine(size.x,size.y); 
    }


    public void doFrame() {
        // Move all objects.
        moveObjects(
            null,// object name prefix of objects to move (null means any)
            0    // object collision ID of objects to move (0 means any)
        );
    }



    @Override
    public void initGame() {
        setFrameRate(35, 2);
        defineMedia("media.tbl");
        // create some game objects
        // (see below for the class definition)
//        for (int i = 0; i < 20; i++) {
//            new ObjectTurtles(this);
//        }
        usersetup();
    }	public void usersetupF0() {
newobject("Objectball", 50, 50);	}
public void usersetup() {
usersetupF0();}}