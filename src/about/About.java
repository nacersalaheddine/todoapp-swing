package about;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import factory.ButtonCreator;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainapp.TodoApp;
import util.Utility;

/**
 *
 * @author Nacer Salah Eddine
 */
public class About extends JDialog {

    /**
     * Launch the application.
     */
    public Font mainFont;
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final String FB_IMG_RES_LOC = "/about/res/facebook.png";
    public static final String BEH_IMG_RES_LOC = "/about/res/behance.png";
    public static final String GH_IMG_RES_LOC = "/about/res/github-image.png";
    public static final String LNKDN_IMG_RES_LOC = "/about/res/linkedin.png";
    public static final String SHAPE_LOGO_RES_LOC = "/about/res/logo-shape.png";
    public static final String MAIN_LOGO_RES_LOC = "/about/res/Asset 3@2x.png";

    public static final String FONT_RES_LOC = "resources/fonts/Cairo-Regular.ttf";

    public static final String APP_TITLE = "TODO-APP";
    public static final String APP_VERSTION = "Ver: 2.0";
    public static final String EMAIL_LBL_TOOLTIP = "Click To Copy To Your Clipboard";

    public static final String DEVELOPER_S_LBL = "Developer(s)";
    public static final String APP_DESC_LBL = "Short Description";
    public static final String APP_CREATION_DATE_LBL = "Creation Date";
    public static final String APP_LICENSE_LBL = "License";
    public static final String APP_SOURCE_CODE_LINK_LBL = "Source Code(Github)";
    public static final String APP_JAVA_VERSION_LBL = "Java Version";

    public static final String CORP_NAME_LBL = "ByteShfit Corp";
    public static final String COPY_INFO_BTN_TOOLTIP = "Copy Info";
    public static final String COPY_INFO_BTN = "Copy Informations to clipboard";

    public static final String CONTACT_DEV_BTN_TOOLTIP = "Contact Developer";
    public static final String CONTACT_DEV_BTN = "Contact Developer";

    public static final String FB_LINK = "www.fb.com/byte.shift.tech";
    public static final String BEHANCE_LINK = "";
    public static final String GITHUB_LINK = "www.github.com/nacersalaheddine";
    public static final String LINKED_IN = "";

    public static final String GH_BTN_TOOLTIP = "Visit GitHub";
    public static final String BH_BTN_TOOLTIP = "Visit Behance";
    public static final String FB_BTN_TOOLTIP = "Visit Facebook Page";
    public static final String LI_BTN_TOOLTIP = "Visit LinkedIn";

    public static final String APP_INFO_BORDER_LBL = "Application informations";

    public static final String APP_CREATION_DATE = "Sep-23-2020";
    public static final String APP_JAVA_VERSION = "1.8 or higher";
    public static final String APP_LICENSE = "Freeware";
    public static final String APP_SOURCE_CODE_LINK = "nacersalaheddine/todoapp-swing";
    public static final String APP_DESC = "Simple application for keeping TO-DOs";
    public static final String DEVELOPER_EMAIL = "nacersalaheddine05@gmail.com";
    public static final String DEVELOPER_S = "Salah eddine Nacer";

    public static final String WINDOW_TITLE = "About";

    public static final Color MAIN_BG_COLOR = new Color(71, 53, 106);

    public static final String CORP_ASCII_TEXT_NAME
            = " /$$$$$$$  /$$     /$$ /$$$$$$$$ /$$$$$$$$        /$$$$$$  /$$   /$$ /$$$$$$ /$$$$$$$$ /$$$$$$$$\n"
            + "| $$__  $$|  $$   /$$/|__  $$__/| $$_____/       /$$__  $$| $$  | $$|_  $$_/| $$_____/|__  $$__/\n"
            + "| $$  \\ $$ \\  $$ /$$/    | $$   | $$            | $$  \\__/| $$  | $$  | $$  | $$         | $$   \n"
            + "| $$$$$$$   \\  $$$$/     | $$   | $$$$$         |  $$$$$$ | $$$$$$$$  | $$  | $$$$$      | $$   \n"
            + "| $$__  $$   \\  $$/      | $$   | $$__/          \\____  $$| $$__  $$  | $$  | $$__/      | $$   \n"
            + "| $$  \\ $$    | $$       | $$   | $$             /$$  \\ $$| $$  | $$  | $$  | $$         | $$   \n"
            + "| $$$$$$$/    | $$       | $$   | $$$$$$$$      |  $$$$$$/| $$  | $$ /$$$$$$| $$         | $$   \n"
            + "|_______/     |__/       |__/   |________/       \\______/ |__/  |__/|______/|__/         |__/   \n"
            + "                                                                                                \n";

    public static final String SOCIAL_LINKS = " [FB]fb.com//byte.shift.tech  [GH]@nacersalaheddine";
    public static final String ALL_INFO
            =//= DEVELOPER_EMAIL + "\n" + "Salah eddine nacer";
            "APP NAME            : " + APP_TITLE + "\n"
            + "APP VERSION         : " + APP_VERSTION + "\n"
            + DEVELOPER_S_LBL + "        : " + DEVELOPER_S + "\n"
            + APP_DESC_LBL + "   : " + APP_DESC + "\n"
            + APP_CREATION_DATE_LBL + "       : " + APP_CREATION_DATE + "\n"
            + APP_LICENSE_LBL + "             : " + APP_LICENSE + "\n"
            + APP_SOURCE_CODE_LINK_LBL + " : " + APP_SOURCE_CODE_LINK + "\n"
            + APP_JAVA_VERSION_LBL + "        : " + APP_JAVA_VERSION + "\n"
            + "Developer Email     : " + DEVELOPER_EMAIL + "\n"
            + "Social links        :" + SOCIAL_LINKS + "\n"
            + "\n\n\nCorp :\n\n\n"
            + CORP_ASCII_TEXT_NAME;
    private static final Logger LOG = Utility.getLogger(About.class.getName());

    public static void main(String[] args) {
        /* It posts an event (Runnable)at the end of Swings event list and is
		processed after all other GUI events are processed.*/
        new About(null);

    }

    public About(JFrame jframe) {

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                    mainFont = Utility.loadFont(FONT_RES_LOC);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                        | UnsupportedLookAndFeelException ex) {
                    String msg = "Msg:" + ex.getMessage() + " /-/" + ex.getCause();
                    LOG.log(Level.SEVERE, msg);
                }
                try {

                    initUI();

                    //Creating the MenuBar and adding components
                    GridLayout gl = new GridLayout(0, 2);
                    JPanel mainPanel = new JPanel(gl);

                    JPanel leftPanel = new JPanel(); // the panel is not visible in output
                    JPanel rightPanel = new JPanel();
                    initLeftPanel(leftPanel);
                    initRightPanel(rightPanel);

                    mainPanel.add(leftPanel);
                    mainPanel.add(rightPanel);
                    mainPanel.setBackground(MAIN_BG_COLOR);

                    getContentPane().add(mainPanel);
                    //setIconImage(Toolkit.getDefaultToolkit().getImage(FB_IMG_RES_LOC));
                    addWindowListener(new WindowAdapter() {

                        @Override
                        public void windowClosing(WindowEvent windowEvent) {
                            /*
                            if (JOptionPane.showConfirmDialog(jframe,
                                    "Are you sure you want to close this window?", "Close Window?",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
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
                    setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    setVisible(true);
                } catch (Exception e) {
                    String msg = "Msg:" + e.getMessage() + " /-/" + e.getCause();
                    LOG.log(Level.SEVERE, msg);
                }
            }

            public JPanel createListItem(String lblText, String lblInfoText) {
                JPanel listPane = new JPanel();
                listPane.setBackground(new Color(0, 0, 0, 0));

                listPane.setLayout(new BoxLayout(listPane, BoxLayout.LINE_AXIS));

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
                        if (mouseEvent.getClickCount() == 2) {

                           // System.out.println("double click");
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
                setTitle(WINDOW_TITLE);
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
                setLocationRelativeTo(null);

                if (mainFont != null) {
                    Utility.setFont(getContentPane(), mainFont);

                }
            }

            private JLabel createContactBtnLbls(String tool_tip, String imgLoc, String linkUrl) {

                JLabel lblImbBtn = new JLabel(Utility.scaleImage(new ImageIcon(About.class.getResource(imgLoc)), 25, 25));
                lblImbBtn.setToolTipText(tool_tip);
                lblImbBtn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                lblImbBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                lblImbBtn.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            Desktop.getDesktop().browse(new URI(linkUrl));
                        } catch (IOException | URISyntaxException e1) {
                            String msg = "Msg:" + e1.getMessage() + " /-/" + e1.getCause();
                            LOG.log(Level.SEVERE, msg);
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                });
                return lblImbBtn;
            }

            private void initLeftPanel(JPanel leftPanel) {
                leftPanel.setBackground(new Color(86, 56, 171));

                Border b = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(200, 200, 200));
                leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
                leftPanel.setBorder(BorderFactory.createTitledBorder(b, APP_INFO_BORDER_LBL, TitledBorder.LEFT, TitledBorder.TOP, mainFont, Color.WHITE));
                JPanel topPen = new JPanel();

                topPen.setLayout(new BoxLayout(topPen, BoxLayout.Y_AXIS));

                JPanel headerPnl = new JPanel();
                headerPnl.setLayout(new BoxLayout(headerPnl, BoxLayout.LINE_AXIS));

                headerPnl.setBackground(new Color(0, 0, 0, 0));

                JLabel logoLbl = new JLabel(Utility.scaleImage(new ImageIcon(TodoApp.class.getResource(SHAPE_LOGO_RES_LOC)), 80, 80));

                JLabel appNameLbl = new JLabel(APP_TITLE);
                JLabel appVerLbl = new JLabel(APP_VERSTION);
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

                JPanel jp = createListItem("", "INFO");
                ButtonCreator copyInfoBtn = new ButtonCreator(COPY_INFO_BTN, COPY_INFO_BTN_TOOLTIP, null, Cursor.HAND_CURSOR);
                copyInfoBtn.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE));
                copyInfoBtn.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
                jp.add(Box.createRigidArea(new Dimension(10, 0)));
                jp.add(copyInfoBtn);
                copyInfoBtn.addActionListener((e) -> {
                    StringSelection selection = new StringSelection(ALL_INFO);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection, selection);

                });
                topPen.add(jp);

                topPen.add(createListItem(DEVELOPER_S_LBL + "                   ", DEVELOPER_S));
                topPen.add(createListItem(APP_DESC_LBL + "            ", APP_DESC));
                topPen.add(createListItem(APP_CREATION_DATE_LBL + "                 ", APP_CREATION_DATE));
                topPen.add(createListItem(APP_LICENSE_LBL + "                            ", APP_LICENSE));

                JPanel p = createListItem(APP_SOURCE_CODE_LINK_LBL + "      ", "<html><u>" + APP_SOURCE_CODE_LINK + "</u></html>");

                JLabel l = (JLabel) ((JPanel) p.getComponent(2)).getComponent(1);
                l.setCursor(new Cursor(Cursor.HAND_CURSOR));
                l.setBackground(Color.WHITE);
                l.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            Desktop.getDesktop().browse(new URI("www.github.com/" + APP_SOURCE_CODE_LINK));

                        } catch (IOException | URISyntaxException ex) {
                            String msg = "Msg:" + ex.getMessage() + " /-/" + ex.getCause();
                            LOG.log(Level.SEVERE, msg);

                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                        // l.setForeground(Color.WHITE);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        //  l.setForeground(Color.WHITE.darker());
                    }

                });

                topPen.add(p);
                topPen.add(createListItem(APP_JAVA_VERSION_LBL + "                   ", APP_JAVA_VERSION));
                topPen.setAlignmentX(Component.CENTER_ALIGNMENT);
                topPen.setOpaque(false);
                leftPanel.add(topPen, BorderLayout.CENTER);

                JLabel emailLbl = new JLabel(DEVELOPER_EMAIL);
                emailLbl.setForeground(Color.WHITE);
                emailLbl.setToolTipText(EMAIL_LBL_TOOLTIP);
                emailLbl.setCursor(new Cursor(Cursor.HAND_CURSOR));

                emailLbl.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        StringSelection selection = new StringSelection(DEVELOPER_EMAIL);
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(selection, selection);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                        emailLbl.setForeground(Color.WHITE);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        emailLbl.setForeground(Color.WHITE.darker());
                    }

                });

                ButtonCreator contactDevBtn = new ButtonCreator(CONTACT_DEV_BTN, CONTACT_DEV_BTN_TOOLTIP, null, Cursor.HAND_CURSOR);
                contactDevBtn.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.WHITE));
                contactDevBtn.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));

                contactDevBtn.setFocusable(false);
                JPanel contactsBottomPnla = new JPanel();
                contactsBottomPnla.setLayout(new BoxLayout(contactsBottomPnla, BoxLayout.LINE_AXIS));

                contactsBottomPnla.add(Box.createRigidArea(new Dimension(10, 0)));
                contactsBottomPnla.add(contactDevBtn);

                contactsBottomPnla.add(Box.createVerticalGlue());

                contactsBottomPnla.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, new Color(205, 202, 232)));
                contactsBottomPnla.setBackground(new Color(0, 0, 0, 0));

                JPanel contactsBottomPnl = new JPanel();
                contactsBottomPnl.setLayout(new BoxLayout(contactsBottomPnl, BoxLayout.X_AXIS));
                contactsBottomPnl.add(Box.createVerticalBox());

                JLabel fbBtn = createContactBtnLbls(FB_BTN_TOOLTIP, FB_IMG_RES_LOC, FB_LINK);

                JLabel behBtn = createContactBtnLbls(BH_BTN_TOOLTIP, BEH_IMG_RES_LOC, BEHANCE_LINK);
                behBtn.setEnabled(false);
                JLabel gitBtn = createContactBtnLbls(GH_BTN_TOOLTIP, GH_IMG_RES_LOC, GITHUB_LINK);

                JLabel linkedinBtn = createContactBtnLbls(LI_BTN_TOOLTIP, LNKDN_IMG_RES_LOC, LINKED_IN);
                linkedinBtn.setEnabled(false);
                contactsBottomPnl.add(Box.createRigidArea(new Dimension(10, 0)));
                contactsBottomPnl.add(emailLbl);
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

                contactsBottomPnl.setBackground(MAIN_BG_COLOR);

                leftPanel.setLayout(new BorderLayout());

                topPen.add(contactsBottomPnla);
                topPen.add(contactsBottomPnl);

                if (mainFont != null) {

                    appNameLbl.setFont(mainFont.deriveFont(Font.BOLD, 15f));
                }
                leftPanel.add(topPen);
            }

            private void initRightPanel(JPanel rightPanel) {

                Border b = BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(200, 200, 200));

                rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

                rightPanel.setBorder(BorderFactory.createTitledBorder(b, CORP_NAME_LBL, TitledBorder.LEFT, TitledBorder.TOP, mainFont, new Color(24, 24, 24)));

                rightPanel.setBackground(Color.white);
                rightPanel.add(Box.createHorizontalGlue());
                rightPanel.add(Box.createVerticalGlue());
                JLabel logoMainLbl = new JLabel(Utility.scaleImage(new ImageIcon(TodoApp.class.getResource(MAIN_LOGO_RES_LOC)), 180, 180));
                JLabel devLbl = new JLabel(DEVELOPER_S);
                logoMainLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

                devLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                rightPanel.add(logoMainLbl);
                rightPanel.add(devLbl);

                rightPanel.add(Box.createVerticalGlue());

                if (mainFont != null) {

                    devLbl.setFont(mainFont.deriveFont(Font.BOLD, 15f));

                }
            }
        });

    }

}
