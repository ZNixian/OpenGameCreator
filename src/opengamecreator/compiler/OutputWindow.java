/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package opengamecreator.compiler;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 *
 * @author Campbell Suter
 */
public class OutputWindow {

    private JFrame window;
    private Component parent;
    private JTextArea textArea;

    public OutputWindow(Component parent) {
        this.parent = parent;
        window = new JFrame("Compile Result");
        Rectangle r = new Rectangle(parent.getLocationOnScreen(), new Dimension(500, 500));
        textArea = new JTextArea();
        textArea.setEditable(true);
        window.setContentPane(textArea);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.setBounds(r);
        window.setVisible(true);
    }

    public void remove() {
        if (textArea.getText().isEmpty()) {
            window.setVisible(false);
            window.dispose();
        } else {
            window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }

    public void add(Object s) {
        textArea.append(s.toString());
        textArea.append("\n");
    }
}
