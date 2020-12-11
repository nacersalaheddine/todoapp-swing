package splash_screen;

/**
 *
 * @author Nacer Salah Eddine
 */
import consts.Constants;
import consts.Resources;
import dao.CreateSqliteDb;
import dao.DAOFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public static final String WIND_TITLE = "<html>To-Do app V 2.0</html>";
    private static final Logger LOG = Utility.getLogger(SplashScreen.class.getName());
    public static Font mainFont = null;

    public SplashScreen() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {

            String msg = "Msg:" + ex.getMessage() + " /-/" + ex.getCause();
            LOG.log(Level.SEVERE, msg);
        }
        window = new JWindow();
        ImageIcon image = Utility.scaleImage(new ImageIcon(Resources.SPLASH_SCREEN_GIF_RES), 500, 400);
        JLabel backgroundImg = new JLabel(image);
        window.add(backgroundImg);

        backgroundImg.setLayout(new BorderLayout());

        JPanel p = new JPanel();
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        // progressBar.setStringPainted(true);

        JLabel label = new JLabel(WIND_TITLE);

        label.setFont(Utility.loadFont(Resources.SPLASH_SCREEN_FONT_RES).deriveFont(Font.PLAIN, 30));
        label.setForeground(new Color(0, 120, 215));
        label.setVerticalAlignment(JLabel.NORTH);
        label.setHorizontalAlignment(JLabel.CENTER);
        backgroundImg.add(label);

        window.add(progressBar, BorderLayout.SOUTH);

        p.setBorder(BorderFactory.createLineBorder(Color.black));

        window.setSize(450, 350);

    }

    public void show() {
        window.setVisible(true);
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

        private final Logger LOG = Logger.getLogger(Task.class.getName());

        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {

            //check my db
            Path dbFilePath = Paths.get(Resources.SQLITE_DB_FILE_LOCATION);
            try {
                if (Files.exists(dbFilePath)) {
                    //System.out.println("exists");
                    mainFont = Utility.loadFont(Constants.MAIN_FONT_RES);
                } else if (Files.notExists(dbFilePath)) {
                    //create file and folder of database
                    //resources/sqlite/todos_db.db
                    try {

                        Path path = Paths.get(Resources.DB_DIR_LOCATION);

                        //java.nio.file.Files;
                        Files.createDirectories(path);

                        //System.out.println("Directory is created!");

                    } catch (IOException e) {

                        String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
                        LOG.log(Level.SEVERE, msg);
                        System.err.println("Failed to create directory!" + e.getMessage());
                        throw new Exception("Failed to create directory!" + e.getMessage());
                    }

                    CreateSqliteDb.initSQLiteDB();

                } else {
                    String errMsg = "unknown error , the existence of the file cannot be verified. (maybe there is no access rights to this path)";
                    System.err.println(errMsg);
                    throw new Exception(errMsg);
                }
                readDbTodosList();
                readDbTodos();
            } catch (Exception e) {

                String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
                LOG.log(Level.SEVERE, msg);
                System.err.println("unknown error , " + e);

            }

            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            //Toolkit.getDefaultToolkit().beep();
            //setCursor(null); //turn off the wait cursor
            splash.hide();
            new TodoApp();
        }

        private void readDbTodosList() throws Exception {
            todosListsDaoImpl = DAOFactory.getInstance().getTodosListDao();
            todosListsDaoImpl.showAllActive();

        }

        private void readDbTodos() throws Exception {

            todoDaoImpl = DAOFactory.getInstance().getTodoDao();
            todoDaoImpl.showAllActive();

        }

    }

}
