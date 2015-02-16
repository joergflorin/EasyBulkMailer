/*
 * Created on 16.02.2015 by joerg
 */
package de.casablu.ebm;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Redirecting output to a window to see System.outs on gui.
 * 
 * @author Joerg Florin (git@casa-blu.de)
 */
class ConsoleWindow extends JFrame {

    ConsoleWindow() {
        setTitle("Easy Bulk Mailer Console");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextArea consoleArea = new JTextArea();

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(consoleArea), BorderLayout.CENTER);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        System.setOut(new PrintStream(new CustomOutputStream(consoleArea)));
        System.setErr(new PrintStream(new CustomOutputStream(consoleArea)));
    }

    /**
     * This class extends from OutputStream to redirect output to a JTextArrea
     * 
     * @author www.codejava.net
     *
     */
    public class CustomOutputStream extends OutputStream {
        private JTextArea textArea;

        public CustomOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) throws IOException {
            // redirects data to the text area
            textArea.append(String.valueOf((char) b));
            // scrolls the text area to the end of data
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }
}
