package factory;

import consts.Constants;
import java.awt.Cursor;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import util.Utility;

/**
 *
 * @author Nacer Salah Eddine
 */
public class ButtonCreator extends JButton {

    private static final Logger LOG = Utility.getLogger(ButtonCreator.class.getName());

    // private JButton button;
    public ButtonCreator(String name, String toolTipText, String imgLoc, int cursor) {
        // this.button = new JButton(name);
        super.setText(name);
        super.setCursor(new Cursor(cursor));
        //this.tooltip
        super.setToolTipText(toolTipText);
        //this.setMargin(new Insets(10, 10, 10, 10));
        //this.setBorder(new EmptyBorder(0, 10, 0, 10));
        //this.setBackground(Color.WHITE);
        //this.setFont(TodoApp.mainFont);

        if (imgLoc != null) {
            ImageIcon imgIcon = Utility.scaleImage(new ImageIcon(getClass().getResource(imgLoc)), Constants.ICON_IMGS_WIDTH, Constants.ICON_IMGS_HEIGHT);
            super.setIcon(imgIcon);
        }

        /*/
            this.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println(" was clicked.");//this.getText() + 
                }
            });*/
    }

    public JButton getButton() {
        return this;
    }

    @Override
    public String toString() {
        return this.getText();
    }

    public void makeTransparent() {
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.setFocusPainted(true);
    }
}
