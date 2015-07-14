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
import model.Blob;
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

        //WIDTH_PXL = 768;
        //HEIGHT_PXL = WIDTH_PXL*((int)model.getFieldSize().getHeight())/((int)model.getFieldSize().getWidth());
        WIDTH_PXL = (int) model.getFieldSize().getWidth();
        HEIGHT_PXL = (int) model.getFieldSize().getHeight();

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
                   //frame.setVisible(true);
                    //frame.setBackground(Color.BLACK);
                    JComponent background = new Background(Color.BLACK);
                    background.setOpaque(true);
                    background.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    background.setBounds(0, 0, WIDTH_PXL, HEIGHT_PXL);

                    msgLabel.setPreferredSize(new Dimension(WIDTH_PXL, MSG_HEIGHT));
                    msgLabel.setBounds(1, 10, WIDTH_PXL - 20, MSG_HEIGHT);
                    msgLabel.setForeground(Color.yellow);
                    msgLabel.setBackground(Color.BLACK);
                    msgLabel.setOpaque(false);
                    msgLabel.setHorizontalAlignment(SwingConstants.CENTER);

                    /*JComponent testB = new Background(Color.RED);
                    testB.setOpaque(true);
                    testB.setBounds(WIDTH_PXL / 2, HEIGHT_PXL / 2, 20, 20);*/

                    /*JComponent circletest = new DrawCircle();
                    circletest.setOpaque(true);
                    circletest.setBounds(WIDTH_PXL/2, HEIGHT_PXL/2, 10, 10);*/

                    frame.getContentPane().add(msgLabel);
                    frame.getContentPane().setPreferredSize(new Dimension(WIDTH_PXL, HEIGHT_PXL));

                    ArrayList<Blob> circles = model.circlesToDraw();
                    //System.out.println(circles);
                    for (int i = 0; circles.size() > i; i++) {
                        Ball newBall = new Ball(circles.get(i));
                        //System.out.println("Ball added");
                        //System.out.println(circles.get(i));
                        balls.add(newBall);
                        frame.getContentPane().add(newBall.getJComponent());
                    }
                    //frame.getContentPane().add(circletest);
                    //frame.getContentPane().add(testB);
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
                                balls.get(i).update(/*scalePoint*/(balls.get(i).myBlob.getCenter()));
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
    Blob myBlob;
    //private JComponent comp = new JLabel("");
    private JComponent comp = new DrawCircle();

    public Ball(Blob myBlob) {
        this.myBlob = myBlob;
        this.comp.setOpaque(true);
    }

    public void update(Point loc) {
        this.comp.setBounds((int)(loc.getX() - myBlob.getRadius()), (int)(loc.getY() - myBlob.getRadius()), (int)(myBlob.getRadius()*2), (int)(myBlob.getRadius()*2));
    }

    public JComponent getJComponent() {
        return this.comp;
    }
}

