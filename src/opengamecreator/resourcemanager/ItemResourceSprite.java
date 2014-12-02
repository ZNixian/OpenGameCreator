/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.resourcemanager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import opengamecreator.OpenGameCreator;
import opengamecreator.resourcemanager.commandLine.ExternalFileThread;

/**
 *
 * @author Campbell Suter
 */
public class ItemResourceSprite extends ItemResourceNode {

    private static boolean isOpen;

    public ItemResourceSprite(File location, OpenGameCreator ogc) {
        super(location);
    }
    private static final int SPLASH_DELAY = 3000;
    private static Map<File, Thread> openFiles = new HashMap<>();

    @Override
    public void open() {
        if (openFiles.containsKey(getLocation())) {
            JOptionPane.showMessageDialog(null,
                    "This file is already open!\nWaiting for this process to terminate.", "File open!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String line = "\"C:\\Program Files\\paint.net\\PaintDotNet.exe\" \"" + getLocation().getAbsolutePath() + "\"";
        Thread t = new Thread(new ExternalFileThread(line, getLocation()));
        add(getLocation(), t);
        t.start();
    }

    private synchronized static void add(File fi, Thread t) {
        openFiles.put(fi, t);
    }

    public synchronized static void done(File fi) {
        openFiles.remove(fi);
    }
}
