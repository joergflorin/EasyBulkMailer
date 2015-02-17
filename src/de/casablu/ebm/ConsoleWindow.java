/*
 * Created on 16.02.2015 by joerg
 */
package de.casablu.ebm;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Redirecting output to a window to see System.outs on gui.
 * 
 * @author Joerg Florin (git@casa-blu.de)
 */
class ConsoleWindow extends JFrame {

    ConsoleWindow() {
        setTitle(String.format("Easy Bulk Mailer %s Console",
                Version.getVersionString()));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        final JTextArea consoleArea = new JTextArea();
        consoleArea.setEditable(false);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(consoleArea), BorderLayout.CENTER);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        final PipedInputStream pis = new PipedInputStream();
        final BufferedReader br = new BufferedReader(new InputStreamReader(pis));

        try {
            PipedOutputStream pos = new PipedOutputStream(pis);
            PrintStream ps = new PrintStream(pos);
            System.setOut(ps);
            System.setErr(ps);

            Thread reader = new Thread() {
                public void run() {
                    while (true) {
                        try {
                            // refresh console every 10 ms.
                            sleep(10);
                        } catch (InterruptedException e) {
                            // ignore
                        }
                        try {
                            final String newLine = br.readLine();
                            SwingUtilities.invokeLater(new Runnable() {

                                @Override
                                public void run() {
                                    consoleArea.append(newLine);
                                    consoleArea.append("\n");
                                }

                            });
                        } catch (IOException e) {
                            // ignore
                        }
                    }
                }
            };
            reader.setDaemon(true);
            reader.start();
        } catch (IOException e) {
            // TODO better Error-Handling.
            throw new RuntimeException(e);
        }
    }
}
