package view;

import javax.swing.*;
import java.awt.*;

public class DrawCircle extends JComponent {

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Point loc = this.getLocation();
        int diameter = (int)this.getSize().getHeight();
        //System.out.println(loc);
        g.setColor(Color.GREEN);
        g.fillOval(0, 0, diameter, diameter);
        //g.fillOval((int)loc.getX(), (int)loc.getY(), diameter, diameter);
    }
}
