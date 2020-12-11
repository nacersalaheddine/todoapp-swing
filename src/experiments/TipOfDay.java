/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.


 */

package experiments;
import mainapp.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;


/**
 *
 * @author Nacer Salah Eddine
 */
public class TipOfDay {
  public static void main(String[] args) {
    JDialog d = new JDialog((Frame)null,"Tip of the Day");

    JPanel basic = new JPanel();
    basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
    d.add(basic);

    JPanel topPanel = new JPanel(new BorderLayout(0, 0));
    topPanel.setMaximumSize(new Dimension(450, 0));
    JLabel hint = new JLabel("This is the tip");
    hint.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 10));
    topPanel.add(hint);

    JSeparator separator = new JSeparator();
    separator.setForeground(Color.gray);

    topPanel.add(separator, BorderLayout.SOUTH);

    basic.add(topPanel);

    JPanel textPanel = new JPanel(new BorderLayout());
    textPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
    JTextPane pane = new JTextPane();

    pane.setContentType("text/html");
    String text = "<paragraph><b>Content</b></paragraph>";
    pane.setText(text);
    pane.setEditable(false);
    textPanel.add(new JScrollPane(pane));

    basic.add(textPanel);

    JPanel boxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));

    JCheckBox box = new JCheckBox("Show Tips at startup");
    box.setMnemonic(KeyEvent.VK_S);

    boxPanel.add(box);
    basic.add(boxPanel);

    JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    JButton ntip = new JButton("Next Tip");
    ntip.setMnemonic(KeyEvent.VK_N);
    JButton close = new JButton("Close");
    close.setMnemonic(KeyEvent.VK_C);

    bottom.add(ntip);
    bottom.add(close);
    basic.add(bottom);

    bottom.setMaximumSize(new Dimension(450, 0));

    d.setSize(new Dimension(450, 350));
    d.setResizable(false);
    d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    d.setVisible(true);
  }
}