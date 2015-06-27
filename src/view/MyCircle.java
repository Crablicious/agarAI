package view;

import javax.swing.*;
import java.awt.*;

public class MyCircle extends JComponent {

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        Point loc = this.getLocation();
        int diameter = (int)this.getSize().getHeight();
        System.out.println(loc);
        g.fillOval((int)loc.getX(), (int)loc.getY(), diameter, diameter);
    }
}
