/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.resourcemanager.commandLine;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import opengamecreator.resourcemanager.ItemResourceSprite;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;

/**
 *
 * @author Campbell Suter
 */
public class ExternalFileThread implements Runnable {

    private String command;
    private File file;

    public ExternalFileThread(String command, File file) {
        this.command = command;
        this.file = file;
    }

    @Override
    public void run() {
        try {
            CommandLine cmdLine = CommandLine.parse(command);
            DefaultExecutor executor = new DefaultExecutor();
            int exitValue = executor.execute(cmdLine);
        } catch (ExecuteException ex) {
            Logger.getLogger(ExternalFileThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExternalFileThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        ItemResourceSprite.done(file);
    }
}
