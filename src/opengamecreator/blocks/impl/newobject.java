/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.blocks.impl;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.workspace.Workspace;
import opengamecreator.blocks.EvalUnit;

/**
 *
 * @author Campbell Suter
 */
public class newobject {

    public static String codeFor(Block b, Workspace w) {
        String[] ss = EvalUnit.argsForAsArray(b, w);
        String s = ss[1] == null ? "" : "JGObject " + ss[1] + " = ";
        return s + "newobject(" + ss[0] + ", " + ss[2] + ", " + ss[3] + ");";
    }
}
