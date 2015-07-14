package view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Adam on 2015-07-14.
 */
public class Background extends JComponent {

    Color color;

    public Background(Color color) {
        super();
        this.color = color;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
}
