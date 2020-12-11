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
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*from w  ww .  j  a  v a2 s  .com*/
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

public class JWindowNoTitleBar extends JFrame {
  JWindow window = new JWindow(this);

  public JWindowNoTitleBar() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.getContentPane().add(new JLabel("About"), BorderLayout.NORTH);
    window.getContentPane().add(new JLabel("Label", SwingConstants.CENTER),
        BorderLayout.CENTER);
    JButton b = new JButton("Close");
    window.getContentPane().add(b, BorderLayout.SOUTH);
    b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        window.setVisible(false);
      }
    });
    window.pack();
    window.setBounds(50, 50, 200, 200);

    b = new JButton("About...");
    b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        window.setVisible(true);
      }
    });
    getContentPane().add(b);
    pack();
  }

  public static void main(String[] args) {
    new JWindowNoTitleBar().setVisible(true);
  }
}