
package experiments;

/**
 *
 * @author Nacer Salah Eddine
 */
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
 
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
 
/**
 * This Java Swing program demonstrates how to create a hyperlink.
 *
 * @author www.codejava.net
 *
 */
public class HyperlinkDemo extends JFrame {
    private String text = "Visit CodeJava";
    private JLabel hyperlink = new JLabel(text);
 
    public HyperlinkDemo() throws HeadlessException {
        super();
        setTitle("Hyperlink Demo");
 
        hyperlink.setForeground(Color.BLUE.darker());
        hyperlink.setCursor(new Cursor(Cursor.HAND_CURSOR));
 
        hyperlink.addMouseListener(new MouseAdapter() {
 
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("http://www.codejava.net"));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
 
            @Override
            public void mouseExited(MouseEvent e) {
                hyperlink.setText(text);
            }
 
            @Override
            public void mouseEntered(MouseEvent e) {
                hyperlink.setText("<html><a href=''>" + text + "</a></html>");
            }
 
        });
 
        setLayout(new FlowLayout());
        getContentPane().add(hyperlink);
 
 
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
 
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
 
            @Override
            public void run() {
                new HyperlinkDemo().setVisible(true);;
            }
        });;
    }
}