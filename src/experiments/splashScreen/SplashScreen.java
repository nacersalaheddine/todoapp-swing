package experiments.splashScreen;

/**
 *
 * @author Nacer Salah Eddine
 */
import mainapp.*;
import consts.Resources;
import dao.CreateSqliteDb;
import dao.DAOFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import mainapp.TodoApp;
import static mainapp.TodoApp.todoDaoImpl;
import static mainapp.TodoApp.todosListsDaoImpl;
import util.Utility;

public class SplashScreen implements PropertyChangeListener {

    private final JWindow window;
    private final JProgressBar progressBar;
    static SplashScreen splash;
    private Task task;

    public SplashScreen() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        window = new JWindow();
        ImageIcon image = Utility.scaleImage(new ImageIcon("resources/splash.gif"), 500, 400);
        JLabel backgroundImg = new JLabel(image);
        window.add(backgroundImg);

        backgroundImg.setLayout(new BorderLayout());

        JPanel p = new JPanel();
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        JLabel label = new JLabel("<html>To-Do app V 1.0</html>");

        label.setFont(Utility.loadFont("resources/fonts/Oswald-VariableFont_wght.ttf").deriveFont(Font.PLAIN, 30));
        label.setForeground(new Color(0, 120, 215));
        label.setVerticalAlignment(JLabel.NORTH);
        label.setHorizontalAlignment(JLabel.CENTER);
        backgroundImg.add(label);

        window.add(progressBar, BorderLayout.SOUTH);

        p.setBorder(BorderFactory.createLineBorder(Color.black));

        window.setSize(450, 350);

    }

    public void show() {
        // set visibility of window 
        window.setVisible(true);
        // set location of window 
        window.setLocationRelativeTo(null);

        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress".equals(evt.getPropertyName())) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        }
    }

    public void hide() {
        window.setVisible(false);
        window.dispose();
    }

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {
            splash = new SplashScreen();
            splash.show();
        });
    }

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
