package view;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import javafx.scene.shape.Circle;
import model.AgarModel;
import model.BaseBlob;
import sun.print.BackgroundServiceLookup;

public class AgarView {
    private final int WIDTH_PXL;
    private final int HEIGHT_PXL;

    private final AgarModel model;
    final JLabel msgLabel;
    ArrayList<Ball> balls;
    final JFrame frame;

    public AgarView(AgarModel model) {
        //initialize the View members:
        this.model = model;

        WIDTH_PXL = 768;
        HEIGHT_PXL = WIDTH_PXL*((int)model.getFieldSize().getHeight())/((int)model.getFieldSize().getWidth());

        this.balls = new ArrayList<Ball>();

        // initialize the graphics stuff:
        final JFrame frame = new JFrame("SwingAgar");
        final JLabel msgLabel = new JLabel("Messages");
        try {
        SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    final int MSG_HEIGHT = 30;
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    
                    frame.setLayout(null);
                    JLabel background = new JLabel("");
                    background.setBackground(Color.BLACK);
                    background.setOpaque(true);
                    background.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    background.setBounds(0, 0, WIDTH_PXL, HEIGHT_PXL);

                    msgLabel.setPreferredSize(new Dimension(WIDTH_PXL, MSG_HEIGHT));
                    msgLabel.setBounds(1, 10, WIDTH_PXL - 20, MSG_HEIGHT);
                    msgLabel.setForeground(Color.yellow);
                    msgLabel.setBackground(Color.BLACK);
                    msgLabel.setOpaque(false);
                    msgLabel.setHorizontalAlignment(SwingConstants.CENTER);

                    frame.getContentPane().add(msgLabel);
                    frame.getContentPane().setPreferredSize(new Dimension(WIDTH_PXL, HEIGHT_PXL));


                    for (BaseBlob blob : model.circlesToDraw()) {
                        Ball newBall = new Ball(blob);
                        balls.add(newBall);
                        frame.getContentPane().add(newBall.getJComponent());
                    }

                    frame.getContentPane().add(background);
                    
                    frame.pack();
                }
            });
        } catch (Exception e) {
            System.err.println("Couldn't initialize AgarView: " + e);
            System.exit(1);
        }
        this.frame = frame;
        this.msgLabel = msgLabel;
    }

    public void show() {
        this.frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    private Point scalePoint(Point modelPoint) {
        return new Point(scaleXPos((int)modelPoint.getX()), scaleYPos((int)modelPoint.getY()));
    }

    private int scaleXPos(int modelXPos) {
        return (int)(((modelXPos)*1.0/((int)model.getFieldSize().getWidth()))
                     * WIDTH_PXL);
    }

    private int scaleYPos(int modelYPos) {
        return (int)((modelYPos*1.0/((int)model.getFieldSize().getHeight())) * HEIGHT_PXL);
    }

    public void update() {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if (model.getMessage() == null) {
                        msgLabel.setVisible(false);
                    } else {
                        msgLabel.setVisible(true);
                        msgLabel.setText(model.getMessage());
                    }
                    for (int i = 0; balls.size() > i; i++) {
                        if (balls.get(i).myBlob != null) {
                            if (model.circlesToDraw().contains(balls.get(i).myBlob)) {
                                balls.get(i).update(scalePoint(balls.get(i).myBlob.getBlobCenterPoint()));
                            }else {
                                frame.remove(balls.get(i).getJComponent());
                                balls.remove(balls.get(i));
                                i--;
                            }
                        }
                    }
                    frame.repaint();
                    Toolkit.getDefaultToolkit().sync();
                }
            });
    }
}

/**
 * visualizing the blob
 */
class Ball {
    BaseBlob myBlob;
    private JComponent comp = new JLabel(""); //new MyCircle();

    public Ball(BaseBlob myBlob) {
        Point center = myBlob.getBlobCenterPoint();
        //this.comp = new MyCircle();
        //this.comp.setBackground(Color.WHITE);
        this.myBlob = myBlob;
        this.comp.setOpaque(true);
        this.comp.setBackground(Color.WHITE);
    }

    public void update(Point loc) {
        //this.comp.setSize((int) myBlob.getRadius()*2, (int) (myBlob.getRadius()*2));
        //this.comp.setLocation((int)loc.getX(), (int)loc.getY());
        this.comp.setBounds((int)loc.getX() - (int)myBlob.getRadius(), (int)loc.getY() - (int)myBlob.getRadius(), (int)myBlob.getRadius()*2, (int)myBlob.getRadius()*2);
    }

    public JComponent getJComponent() {
        return this.comp;
    }
}

