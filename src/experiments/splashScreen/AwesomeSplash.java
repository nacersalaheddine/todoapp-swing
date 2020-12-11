/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experiments.splashScreen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Nacer Salah Eddine
 */
/**
 *
 * @author Nacer Salah Eddine
 */
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;
import util.Utility;

public class AwesomeSplash implements
        PropertyChangeListener {

    private final JWindow window;
    JLabel label;

    public AwesomeSplash() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {
        }
        window = new JWindow();
        ImageIcon image
                = //new ImageIcon("resources/splash.gif");
                Utility.scaleImage(new ImageIcon("resources/ee0d01a2796059e8298032a7442810b9-abstract-square-background-by-vexels.png"), 500, 400);
        JLabel backgroundImg = new JLabel(image);
        //  window.add(backgroundImg);
        /*
        taskOutput = new JTextArea(5, 20);
        taskOutput.setMargin(new Insets(5, 5, 5, 5));
        taskOutput.setEditable(false);
       taskOutput.setText("sfsdfsdfsffffffffffffffffffffffffffffff");
      
         */
        backgroundImg.setLayout(new BorderLayout());

        // set panel 
        JPanel p = new JPanel();
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        //  progressBar.setLayout(mgr);
        // create a label 
        label = new JLabel("Todo app V 1.0");

        //label.setFont(label.getFont().deriveFont(Font.BOLD, 48));
        label.setFont(Utility.loadFont("resources/fonts/Oswald-VariableFont_wght.ttf").deriveFont(Font.BOLD, 15));
        label.setForeground(Color.WHITE);
        /*
    label.setBackground(Color.blue);
label.setSize(50, 20);
        label.setOpaque(true);
         */
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        backgroundImg.add(label);
        // p.add(new JLabel(image), BorderLayout.CENTER);
        p.add(backgroundImg, BorderLayout.CENTER);
        // window.add(progressBar,BorderLayout.SOUTH);

        window.pack();

        // set border 
        //  p.setBorder(BorderFactory.createLineBorder(Color.black));
        //   p.add(progressBar, BorderLayout.SOUTH);
        // p.add(label, BorderLayout.NORTH);
        p.setOpaque(false);
        // p.setComponentZOrder(progressBar, window.getComponentCount()-1); 

        //   p.add(taskOutput);
        //  window.add(p);
        // AWTUtilities.setWindowOpaque(window, false);
        p.setBackground(new Color(255, 255, 255, 100));
        window.add(p);
        //AWTUtilities.setWindowOpacity ( window, 0.5f );
        // set background 

        // setsize of window 
        window.setSize(450, 350);

    }
    private final JProgressBar progressBar;
    // private JTextArea taskOutput;

    public void show() {

        // set visibility of window 
        window.setVisible(true);
        // set location of window 
        window.setLocationRelativeTo(null);

        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();

    }

    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
            label.setText(String.format(
                    "<html> Completed %d%% of task<br>Second line.</html>", task.getProgress()));

            /*
            taskOutput.append(String.format(
                    "Completed %d%% of task.\n", task.getProgress()));*/
        }
    }

    public void hide() {

        window.setVisible(false);
        window.dispose();
    }
    static AwesomeSplash splash;

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                splash = new AwesomeSplash();
                splash.show();
            }
        });
    }

    private Task task;

    class Task extends SwingWorker<Void, Void> {

        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
            Random random = new Random();
            int progress = 0;
            //Initialize progress property.
            setProgress(0);
            while (progress < 100) {
                //Sleep for up to one second.
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException ignore) {
                }
                //Make random progress.
                progress += random.nextInt(10);
                setProgress(Math.min(progress, 100));
            }
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            Toolkit.getDefaultToolkit().beep();
            //setCursor(null); //turn off the wait cursor
            splash.hide();
        }
    }

}
