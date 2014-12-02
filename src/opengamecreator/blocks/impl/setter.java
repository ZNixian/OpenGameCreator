/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.blocks.impl;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.workspace.Workspace;
import opengamecreator.BlockInfo;
import opengamecreator.blocks.EvalUnit;

/**
 *
 * @author Campbell Suter
 */
public class setter {

    public static String eval(Block b, Workspace w) {
        Block b1 = BlockInfo.getBlockById(w, b.getSocketAt(0).getBlockID());
        Block b2 = BlockInfo.getBlockById(w, b.getSocketAt(0).getBlockID());
        return EvalUnit.eval(b1, w) + "=" + EvalUnit.eval(b2, w) + ";";
    }
}
