package view;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import model.AgarModel;
import model.BaseBlob;
import sun.print.BackgroundServiceLookup;

public class AgarView {
    private final int WIDTH_PXL;
    private final int HEIGHT_PXL;

    private final AgarModel model;

    //final Ball ball;
    ArrayList<Ball> balls;
    final JFrame frame;
    
    /*
    private JLabel makeScoreLabel(BarKey k) {
        JLabel ret = new JLabel("0");
        switch (k) {
        case LEFT:
            ret.setBounds(20,20, 50,50);
            ret.setHorizontalAlignment(SwingConstants.LEFT);
            break;
        case RIGHT:
            ret.setBounds(WIDTH_PXL-20-50,20, 50,50);
            ret.setHorizontalAlignment(SwingConstants.RIGHT);
            break;
        }
        ret.setForeground(Color.WHITE);
        ret.setBackground(Color.BLACK);
        ret.setFont(new Font("Mono", Font.PLAIN, 40));
        ret.setOpaque(true);
        return ret;
    } 
    */

    public AgarView(AgarModel model) {
        //initialize the View members:
        this.model = model;

        WIDTH_PXL = 600;
        HEIGHT_PXL = WIDTH_PXL*((int)model.getFieldSize().getHeight())/((int)model.getFieldSize().getWidth());

        //this.ball = new Ball();
        this.balls = new ArrayList<Ball>();

        // initialize the graphics stuff:
        final JFrame frame = new JFrame("SwingAgar");
        try {
        SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    
                    frame.setLayout(null);
                    JLabel background = new JLabel("");
                    background.setBackground(Color.BLACK);
                    background.setOpaque(true);
                    background.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    background.setBounds(0, 0, WIDTH_PXL, HEIGHT_PXL);

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
                    //bars.get(BarKey.LEFT).update((model.getBarPos(BarKey.LEFT)), scaleYPos(model.getBarHeight(BarKey.LEFT)));
                    //bars.get(BarKey.RIGHT).update(scaleYPos(model.getBarPos(BarKey.RIGHT)), scaleYPos(model.getBarHeight(BarKey.RIGHT)));
                    for (Ball currBall : balls) {
                        if (currBall.myBlob != null) {
                            if (model.circlesToDraw().contains(currBall.myBlob)) {
                                currBall.update(scalePoint(currBall.myBlob.getBlobCenterPoint()));
                            }else {
                                balls.remove(currBall);
                            }
                        }
                    }
                    //ball.update(scalePoint(model.getBallPos()));
                    /*if (model.getMessage() == null) {
                        msglabel.setVisible(false);
                    } else {
                        msglabel.setVisible(true);
                        msglabel.setText(model.getMessage());
                    }*/
                    /*for (BarKey k : BarKey.values()) {
                        scorelabels.get(k).setText(model.getScore(k));
                    }*/
                    Toolkit.getDefaultToolkit().sync(); //TODO: Can we fix this better?
                }
            });
    }
}

/**
 * visualizing the bar of a player
 */
class Bar {
    public static final int WIDTH_PXL = 10;

    private final int XPOS;

    private JComponent comp = new JLabel("");

    // p in [0..10]
    public void update(int pxl, int bar_height_pxl) {
        this.comp.setBounds(this.XPOS - WIDTH_PXL/2, pxl-bar_height_pxl/2, WIDTH_PXL, bar_height_pxl);
    }

    public Bar(int xpos) {
        this.XPOS = xpos;
        this.comp.setBackground(Color.WHITE);
        this.comp.setOpaque(true);
        //final Dimension size = new Dimension(width, height);
        //        this.comp.setPreferredSize(size);
        //        this.comp.setMinimumSize(size);
        //        this.comp.setMaximumSize(size);
    }

    public JComponent getJComponent() {
        return this.comp;
    }
}

/**
 * visualizing the ball
 */
class Ball {
    BaseBlob myBlob;
    private final JComponent comp = new JLabel("");

    public Ball(BaseBlob myBlob) {
        this.comp.setBackground(Color.WHITE);
        this.comp.setOpaque(true);
        this.myBlob = myBlob;
    }

    public void update(Point loc) {
        this.comp.setBounds((int)loc.getX() - (int)myBlob.getRadius(), (int)loc.getY() - (int)myBlob.getRadius(), (int)myBlob.getRadius()*2, (int)myBlob.getRadius()*2);
    }

    public JComponent getJComponent() {
        return this.comp;
    }
}