/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experiments.splashScreen;

/**
 *
 * @author Nacer Salah Eddine
 */
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;

//import com.sun.awt.AWTUtilities;

public class TranslucentFRM {

    private static class ShadowBorder extends AbstractBorder {

        private static final int RADIUS = 30;

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(RADIUS, RADIUS, RADIUS, RADIUS);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.top = RADIUS;
            insets.left = RADIUS;
            insets.bottom = RADIUS;
            insets.right = RADIUS;
            return insets;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(66, 0, 0));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_ATOP, 0.5f));
            g2d.fillRect(0, 0, width - RADIUS, RADIUS);
            g2d.fillRect(width - RADIUS, 0, RADIUS, height - RADIUS);
            g2d.fillRect(0, RADIUS, RADIUS, height - RADIUS);
            g2d.fillRect(RADIUS, height - RADIUS, width - RADIUS, RADIUS);
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        //AWTUtilities.setWindowOpaque(frame, false);
        JPanel panel = new JPanel(new BorderLayout());
        JButton button = new JButton("Hello");
        panel.add(button);
        panel.setBorder(new ShadowBorder());
        frame.setContentPane(panel);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }
}