package help;

import consts.Resources;
import factory.ButtonCreator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import util.Utility;

/**
 *
 * @author Nacer Salah Eddine
 */
public class Help {

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final String APP_TITLE = "TODO-APP";
    ArrayList<Page> pages = new ArrayList<>();
    JTextPane pane;
    int pagesCounter = 1;

    public int currentPage = 1;
    private static final Logger LOG = Utility.getLogger(Help.class.getName());

    class Page {

        String title;
        String content;
        int number;

        public Page(String title, String content, int number) {
            this.title = title;
            this.content = content;
            this.number = number;
        }

    }

    public Help(Frame f) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                        | UnsupportedLookAndFeelException ex) {

                    String msg = "Msg:" + ex.getMessage() + " /-/" + ex.getCause();
                    LOG.log(Level.SEVERE, msg);
                }
                JDialog d = new JDialog(f, "Help Content");
                JPanel basic = new JPanel();
                basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
                d.add(basic);

                //read pages
                try (Stream<Path> paths = Files.walk(Paths.get(Resources.HELP_FILES_LOC))) {

                    paths.filter(Files::isRegularFile).forEach(((t) -> {
                        //System.out.println(t);

                        try {
                            Stream<String> lines = Files.lines(t);
                            String data = lines.collect(Collectors.joining("\n"));
                            //System.out.println(data);
                            pages.add(new Page("TITLE1", data, pagesCounter));
                            pagesCounter++;
                            lines.close();
                        } catch (IOException ex) {
                            Logger.getLogger(Help.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }));
                } catch (IOException ex) {
                    Logger.getLogger(Help.class.getName()).log(Level.SEVERE, null, ex);
                }

                //pages.add(new Page("TITLE1", INFO_TEXT1, 1));
                //pages.add(new Page("TITLE2", INFO_TEXT2, 2));
                JPanel topPanel = new JPanel(new BorderLayout(0, 0));
                //        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
                topPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 0));

                //topPanel.setMaximumSize(new Dimension(WINDOW_WIDTH, 0));
                JLabel hint = new JLabel("<html><h1 style=\"color:blue\">Help Content on " + APP_TITLE + "</h1> Application.</html>");
                hint.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 10));
                topPanel.add(hint);

                JSeparator separator = new JSeparator();
                separator.setForeground(Color.gray);

                topPanel.add(separator, BorderLayout.SOUTH);

                basic.add(topPanel);

                JPanel textPanel = new JPanel();
                textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
                textPanel.setBackground(Color.WHITE);
                //textPanel.setMaximumSize(new Dimension(WINDOW_WIDTH, 0));
                textPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
                pane = new JTextPane();

                pane.setContentType("text/html");
                pane.setText(pages.get(currentPage - 1).content);
                currentPage += 1;
                pane.setEditable(false);
                textPanel.add(new JScrollPane(pane));
                textPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
                pane.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));

                basic.add(textPanel);

                //String text = "<html><h2><b style=\"font-size:2.5em;\">1</b>&#47;2</h2></html>";
                JLabel lbl = new JLabel("1");
                JLabel infoLbl = new JLabel(pagesCounter - 1 + "");
                JPanel jp = createListItem(lbl, infoLbl, d);

                basic.add(jp);
                // basic.add(Box.createVerticalBox());
                d.setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
                d.setResizable(true);
                d.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                d.setLocationRelativeTo(null);
                d.setVisible(true);
            }

        });
    }

    public JPanel createListItem(JLabel lbl, JLabel infoLbl, JDialog d) {
        JPanel listPane = new JPanel();

        ButtonCreator closeBtn = new ButtonCreator("Close", "Close Help Content", null, Cursor.HAND_CURSOR);
        ButtonCreator nextBtn = new ButtonCreator("Next", "Next Page", null, Cursor.HAND_CURSOR);

        closeBtn.addActionListener((e) -> {
            d.dispose();
        });
        nextBtn.addActionListener((e) -> {
            if (currentPage > pagesCounter - 1) {
                currentPage = 1;
            }

            String cntnt = pages.get(currentPage - 1).content;
            lbl.setText(pages.get(currentPage - 1).number + "");
            pane.setText(cntnt);
            currentPage += 1;
        });

        listPane.setLayout(new BoxLayout(listPane, BoxLayout.X_AXIS));

        listPane.setBorder(new EmptyBorder(10, 25, 10, 25));
        lbl.setFont(new Font("impact", Font.BOLD, 40));
        infoLbl.setFont(new Font("impact", Font.PLAIN, 15));
        lbl.setForeground(Color.DARK_GRAY);
        infoLbl.setForeground(Color.DARK_GRAY);

        listPane.add(lbl);
        JLabel sep = new JLabel(" \\ ");
        sep.setFont(new Font("impact", Font.PLAIN, 15));
        listPane.add(sep);
        listPane.add(infoLbl);

        listPane.add(Box.createHorizontalGlue());
        listPane.add(nextBtn);
        listPane.add(closeBtn);
        listPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {

                    //System.out.println("opening image");
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e); //To change body of generated methods, choose Tools | Templates.
            }

        });

        return listPane;
    }

    public static void main(String[] args) throws IOException {
        new Help(null);

    }
}
