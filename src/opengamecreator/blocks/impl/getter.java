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
public class getter {

    public static String eval(Block b, Workspace w) {
        Block bb = BlockInfo.getBlockById(w, b.getSocketAt(0).getBlockID());
        return EvalUnit.eval(bb, w);
    }
}
