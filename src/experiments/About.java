package experiments;

import about.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import factory.ButtonCreator;
import mainapp.TodoApp;
import util.Utility;

/**
 *
 * @author Nacer Salah Eddine
 */
public class About {
    //declare variable

    private JFrame mainFrame;

    /**
     * Launch the application.
     */
    public JFrame getMainFrame() {
        return this.mainFrame;
    }
    //main method
    public Font mainFont;
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static void main(String[] args) {
        /* It posts an event (Runnable)at the end of Swings event list and is
		processed after all other GUI events are processed.*/
        new About(null);

    }

    /**
     * Create the frame.
     *
     * @param jframe
     */
    public About(JFrame jframe) //constructor
    {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //try - catch block
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                    mainFont = Utility.loadFont("resources/fonts/Cairo-Regular.ttf");
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                        | UnsupportedLookAndFeelException ex) {
                }
                try {

                    //Create object of NewWindow
                    //set frame title
                    initUI();

                    mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    mainFrame.setSize(WIDTH, HEIGHT);
                    mainFrame.setLocationRelativeTo(null);
                    //Creating the MenuBar and adding components
                    GridLayout gl = new GridLayout(0, 2);
                    JPanel mainPanel = new JPanel(gl);

                    //Creating the panel at bottom and adding components
                    JPanel leftPanel = new JPanel(); // the panel is not visible in output
                    JPanel rightPanel = new JPanel();
                    leftPanel.setBackground(new Color(86, 56, 171));

                 
                    Border b = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(200, 200, 200));
                    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
                    leftPanel.setBorder(BorderFactory.createTitledBorder(b, "Application informations", TitledBorder.LEFT, TitledBorder.TOP, mainFont, new Color(255, 255, 255)));
                    JPanel topPen = new JPanel();

                    topPen.setLayout(new BoxLayout(topPen, BoxLayout.Y_AXIS));

                    JPanel headerPnl = new JPanel();
                    headerPnl.setLayout(new BoxLayout(headerPnl, BoxLayout.LINE_AXIS));

                    headerPnl.setBackground(new Color(0, 0, 0, 0));

                    JLabel logoLbl = new JLabel(Utility.scaleImage(new ImageIcon(TodoApp.class.getResource("/about/logo-shape.png")), 80, 80));
                    JLabel appNameLbl = new JLabel("TODO-APP");
                    JLabel appVerLbl = new JLabel("Ver: 1.0");
                    appVerLbl.setForeground(Color.white);
                    appNameLbl.setForeground(Color.white);

                    headerPnl.add(Box.createRigidArea(new Dimension(10, 0)));
                    headerPnl.add(logoLbl);
                    headerPnl.add(Box.createRigidArea(new Dimension(20, 0)));
                    headerPnl.add(appNameLbl);
                    headerPnl.add(Box.createRigidArea(new Dimension(30, 0)));

                    headerPnl.add(appVerLbl);
                    headerPnl.add(Box.createVerticalGlue());
                    headerPnl.add(Box.createHorizontalGlue());

                    topPen.add(headerPnl);
                    topPen.add(createListItem("", "About"));

                    topPen.add(createListItem("Developer(s)                   ", "Salah Eddine Nacer"));
                    topPen.add(createListItem("Short Description         ", "Simple application for keeping TO-DOs"));
                    topPen.add(createListItem("Creation Date                ", "Monday-2020"));
                    topPen.add(createListItem("License                            ", "MIT"));

                    topPen.add(createListItem("Source Code(Github)   ", "todoapp"));
                    topPen.add(createListItem("Java Version                   ", "1.8"));
                    topPen.setAlignmentX(Component.CENTER_ALIGNMENT);
                    topPen.setOpaque(false);
                    leftPanel.add(topPen, BorderLayout.CENTER);

                    rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

                    rightPanel.setBorder(BorderFactory.createTitledBorder(b, "ByteShfit Corp", TitledBorder.LEFT, TitledBorder.TOP, mainFont, new Color(24, 24, 24)));

                    rightPanel.setBackground(Color.white);
                    rightPanel.add(Box.createHorizontalGlue());
                    rightPanel.add(Box.createVerticalGlue());
                    JLabel logoMainLbl = new JLabel(Utility.scaleImage(new ImageIcon(TodoApp.class.getResource("/about/Asset 3@2x.png")), 180, 180));
                    JLabel devLbl = new JLabel("Salah Eddine Nacer");
                    logoMainLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

                    devLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                    rightPanel.add(logoMainLbl);
                    rightPanel.add(devLbl);

                    rightPanel.add(Box.createVerticalGlue());
                    JLabel label = new JLabel("nacersalaheddine05@gmail.com");

                    label.setForeground(Color.WHITE);
                    //JTextField tf = new JTextField(10); // accepts upto 10 characters
                    ButtonCreator send = new ButtonCreator("Contact Developer", "tip", null, Cursor.HAND_CURSOR);
                    send.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, new Color(187, 14, 127)));
                    send.setOpaque(false);
                    JPanel contactsBottomPnla = new JPanel();
                    contactsBottomPnla.setLayout(new BoxLayout(contactsBottomPnla, BoxLayout.LINE_AXIS));

                    contactsBottomPnla.add(Box.createRigidArea(new Dimension(10, 0)));
                    contactsBottomPnla.add(send);
                    send.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
                    contactsBottomPnla.add(Box.createVerticalGlue());

                    contactsBottomPnla.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(205, 202, 232)));
                    contactsBottomPnla.setBackground(new Color(0, 0, 0, 0));

                    JPanel contactsBottomPnl = new JPanel();
                    contactsBottomPnl.setLayout(new BoxLayout(contactsBottomPnl, BoxLayout.X_AXIS));
                    contactsBottomPnl.add(Box.createVerticalBox());

                    JLabel fbBtn = createBtn("tool tip", "/about/facebook.png");

                    JLabel behBtn = createBtn("tool tip", "/about/behance.png");

                    JLabel gitBtn = createBtn("tool tip", "/about/github-image.png");

                    JLabel linkedinBtn = createBtn("tool tip", "/about/linkedin.png");
                    contactsBottomPnl.add(Box.createRigidArea(new Dimension(10, 0)));
                    contactsBottomPnl.add(label);
                    contactsBottomPnl.add(Box.createHorizontalGlue());
                    contactsBottomPnl.add(fbBtn);
                    contactsBottomPnl.add(Box.createRigidArea(new Dimension(5, 0)));
                    contactsBottomPnl.add(behBtn);
                    contactsBottomPnl.add(Box.createRigidArea(new Dimension(5, 0)));
                    contactsBottomPnl.add(gitBtn);
                    contactsBottomPnl.add(Box.createRigidArea(new Dimension(5, 0)));
                    contactsBottomPnl.add(linkedinBtn);

                    contactsBottomPnl.add(Box.createVerticalGlue());
                    contactsBottomPnl.add(Box.createHorizontalGlue());

                    leftPanel.add(contactsBottomPnl);

                    contactsBottomPnl.setBackground(new Color(0, 0, 0, 0));

                    leftPanel.setLayout(new BorderLayout());

                    topPen.add(contactsBottomPnla);
                    topPen.add(contactsBottomPnl);

                    leftPanel.add(topPen);

                    mainPanel.add(leftPanel);
                    mainPanel.add(rightPanel);
                    mainPanel.setBackground(new Color(71, 53, 106));

                    //Adding Components to the frame.
                    mainFrame.getContentPane().add(mainPanel);

                    mainFrame.setVisible(true);
                    mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("resourses/404b48152020c5e813bc4f3046a99905.png"));

                    mainFrame.addWindowListener(new WindowAdapter() {

                        @Override
                        public void windowClosing(WindowEvent windowEvent) {
                            /*
                        if (JOptionPane.showConfirmDialog(frame, 
                            "Are you sure you want to close this window?", "Close Window?", 
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                            System.exit(0);
                        }
                             */
                            //mainFrame.setVisible(true);
                            System.out.println("closing detected");
                        }

                        @Override
                        public void windowClosed(WindowEvent e) {
                            System.out.println("windowClosed detected");
                        }

                    });

                    if (mainFont != null) {
                        Utility.setFont(mainFrame.getContentPane(), mainFont);
                        devLbl.setFont(mainFont.deriveFont(Font.BOLD, 15f));

                        appNameLbl.setFont(mainFont.deriveFont(Font.BOLD, 15f));
                    }
                    mainFrame.setUndecorated(true);
                    //set frame visible true
                    //jframe
                    mainFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public JPanel createListItem(String lblText, String lblInfoText) {
                JPanel listPane = new JPanel();
                listPane.setBackground(new Color(0, 0, 0, 0));

                listPane.setLayout(new BoxLayout(listPane, BoxLayout.LINE_AXIS));
                // listPane.setLayout(gl);
                listPane.add(Box.createVerticalGlue());
                JLabel lbl = new JLabel(lblText);
                JLabel infoLbl = new JLabel(lblInfoText);
                listPane.add(lbl);

                listPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                JPanel buttonPane = new JPanel();
                buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
                buttonPane.setBackground(new Color(0, 0, 0, 0));

                buttonPane.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(205, 202, 232)));

                lbl.setForeground(Color.white);
                infoLbl.setForeground(Color.white);
                buttonPane.add(Box.createRigidArea(new Dimension(5, 0)));
                buttonPane.add(infoLbl);
                buttonPane.add(Box.createVerticalGlue());
                listPane.add(buttonPane);
                buttonPane.add(Box.createHorizontalGlue());
                listPane.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {
                        //JTable table = (JTable) mouseEvent.getSource();
                        //Point point = mouseEvent.getPoint();
                        //int row = table.rowAtPoint(point);
                        if (mouseEvent.getClickCount() == 2) {
                            // your valueChanged overridden method 
                            System.out.println("opening image");
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

            private void initUI() {

                mainFrame = new JFrame();
                mainFrame.setTitle("About");
            }

            private JLabel createBtn(String tool_tip, String imgLoc) {

                JLabel lblImg = new JLabel(Utility.scaleImage(new ImageIcon(About.class.getResource(imgLoc)), 25, 25));
                lblImg.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                lblImg.setCursor(new Cursor(Cursor.HAND_CURSOR));
                return lblImg;
            }
        });

    }

}
