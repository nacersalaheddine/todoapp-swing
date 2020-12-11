package experiments.splashScreen;

/**
 *
 * @author Nacer Salah Eddine
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Area;

import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

//import com.sun.awt.AWTUtilities;
import javax.swing.JButton;

public class FlickerWindow extends JWindow implements MouseListener, MouseMotionListener{

    JPanel controlPanel;
    JPanel outlinePanel;
    int mouseX, mouseY;
    Rectangle windowRect;
    Rectangle cutoutRect;
    Area windowArea;

    public static void main(String[] args) {
        FlickerWindow fw = new FlickerWindow();
    }

    public FlickerWindow() {
        super();
        setLayout(new BorderLayout());
        setBounds(500, 500, 200, 200);
        setBackground(Color.GREEN);

        controlPanel = new JPanel();
        controlPanel.setBackground(Color.GRAY);
        controlPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        controlPanel.addMouseListener(this);
        controlPanel.addMouseMotionListener(this);

        outlinePanel = new JPanel();
        outlinePanel.setBackground(Color.BLUE);
        outlinePanel.setBorder(new CompoundBorder(new EmptyBorder(2,2,2,2), new LineBorder(Color.RED, 1)));

        add(outlinePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.NORTH);
        add(new JButton("Dummy button"), BorderLayout.SOUTH);
        setVisible(true);
        setShape();

        addComponentListener(new ComponentAdapter() {           
            @Override
            public void componentResized(ComponentEvent e) {
                setShape();
            }});
    }


    public void paint(Graphics g) {
        // un-comment or breakpoint here to see window updates more clearly
        //try {Thread.sleep(10);} catch (Exception e) {}
        super.paint(g);
    }

    public void setShape() {
        Rectangle bounds = getBounds();
        Rectangle outlineBounds = outlinePanel.getBounds();
        Area newShape = new Area (new Rectangle(0, 0, bounds.width, bounds.height));
        newShape.subtract(new Area(new Rectangle(3, outlineBounds.y + 3, outlineBounds.width - 6, outlineBounds.height - 6)));
        setSize(bounds.width, bounds.height);
       // AWTUtilities.setWindowShape(this, newShape);
    }

    public void mouseDragged(MouseEvent e) {
        int dx = e.getXOnScreen() - mouseX;
        int dy = e.getYOnScreen() - mouseY;

        Rectangle newBounds = getBounds();
        newBounds.translate(dx, dy);
        newBounds.width -= dx;
        newBounds.height -= dy;

        mouseX = e.getXOnScreen();
        mouseY = e.getYOnScreen();

        setBounds(newBounds);
    }

    public void mousePressed(MouseEvent e) {
        mouseX = e.getXOnScreen();
        mouseY = e.getYOnScreen();
    }

    public void mouseMoved(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}