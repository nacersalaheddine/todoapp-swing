package util;

import consts.Resources;
import experiments.JSONTEST;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import logger.LoggerFilter;
import logger.LoggerFormatter;
import logger.LoggerHandler;
import org.json.JSONException;
import watch.DigitalWatch;

/**
 *
 * @author Nacer Salah Eddine
 */
public class Utility {

    private static final Logger LOG = getLogger(Utility.class.getName());

    public static void writeFile(File f, String data, String ext) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File(f.toString() + "." + ext));
            pw.print(data);
        } catch (JSONException ex) {
            Logger.getLogger(JSONTEST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(JSONTEST.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (pw != null) {
                try {
                    pw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static void copyToClipboardText(String s) {

        StringSelection selection = new StringSelection(s);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);

    }

    public static String copyTextFromClipboard() {
        try {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (HeadlessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedFlavorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";

    }

    public static ImageIcon scaleImage(ImageIcon icon, int w, int h) {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if (icon.getIconWidth() > w) {
            nw = w;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if (nh > h) {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }

        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }

    public static String fixedLengthString(String string, int length) {
        return String.format("%1$" + length + "s", string);
    }

    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        logger.addHandler(new LoggerHandler());
        try {

            //create a folder called logs
            //FileHandler file name with max size and number of log files limit
            Handler logsFileHandler = new FileHandler(Resources.LOGS_FILES_PATH, 2000, 5);
            logsFileHandler.setFormatter(new LoggerFormatter());
            //setting custom filter for FileHandler
            logsFileHandler.setFilter(new LoggerFilter());
            logger.addHandler(logsFileHandler);

        } catch (IOException | SecurityException e) {
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);
        }
        return logger;
    }

    public static Font loadFont(String loc) {
        Font font = null;

        try {

            //create the font to use. Specify the size!
            font = Font.createFont(Font.TRUETYPE_FONT, new File(loc)).deriveFont(14f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(font);

        } catch (IOException | FontFormatException e) {
            String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
            LOG.log(Level.SEVERE, msg);
        }
        return font;

    }

    public static String getFullDateTime() {
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();
        String weekDay = localDate.getDayOfWeek().toString();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);
        return weekDay + "-" + month + "-" + day + "-" + year + " / " + hours + ":" + minutes + ":" + seconds;
    }

    public static void setFont(Component component, Font font) {
        if (font != null) {
            component.setFont(font);
            if (component instanceof Container) {
                for (Component child : ((Container) component).getComponents()) {
                    if (!(child instanceof DigitalWatch)) {
                        setFont(child, font);
                    }
                }
            }
        }

    }
}
