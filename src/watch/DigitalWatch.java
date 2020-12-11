package watch;

import consts.Constants;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import util.Utility;

/**
 *
 * @author Nacer Salah Eddine
 */
public class DigitalWatch extends JLabel implements Runnable {

    Thread t = null;
    int hours = 0, minutes = 0, seconds = 0;
    String timeString = "";

    public DigitalWatch() {
        Font digitalFont = Utility.loadFont(Constants.DIGITAL_FONT_RES).deriveFont(35f);
        super.setFont(digitalFont);
        super.setBorder(new EmptyBorder(5, 10, 10, 10));
        //super.setMaximumSize(new Dimension(200,1));
        t = new Thread(this);
        t.start();

    }

    public void run() {
        try {
            while (true) {

                Calendar cal = Calendar.getInstance();
                hours = cal.get(Calendar.HOUR_OF_DAY);
                if (hours > 12) {
                    hours -= 12;
                }
                minutes = cal.get(Calendar.MINUTE);
                seconds = cal.get(Calendar.SECOND);

                SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
                Date date = cal.getTime();
                timeString = formatter.format(date);

                printTime();

                t.sleep(1000);  // interval given in milliseconds  
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printTime() {
        super.setText(timeString);
    }
}
