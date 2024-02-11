package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class TestRotate {
    private JFrame frame;
    private JLabel rotatingLabel;
    private double angle;
    private Timer timer;

    public TestRotate() {
        frame = new JFrame();
        frame.setTitle("Rotating Image Label");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        rotatingLabel = new JLabel();
        rotatingLabel.setIcon(new ImageIcon("assets/bullets/boomerang.png"));
        frame.add(rotatingLabel);

        angle = 0;

        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateRotation();
            }
        });

        timer.start();

        frame.setVisible(true);
        updateLabelSize();
    }

    private void updateRotation() {
        angle += 0.1;
        rotatingLabel.setIcon(rotateIcon(new ImageIcon("assets/bullets/boomerang.png"), angle));
        updateLabelSize();
    }

    private Icon rotateIcon(ImageIcon originalIcon, double angle) {
        Image image = originalIcon.getImage();
        double sin = Math.abs(Math.sin(angle));
        double cos = Math.abs(Math.cos(angle));
        int newWidth = (int) Math.floor(image.getWidth(null) * cos + image.getHeight(null) * sin);
        int newHeight = (int) Math.floor(image.getHeight(null) * cos + image.getWidth(null) * sin);

        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotatedImage.createGraphics();
        AffineTransform transform = new AffineTransform();
        transform.translate((newWidth - image.getWidth(null)) / 2, (newHeight - image.getHeight(null)) / 2);
        transform.rotate(angle, image.getWidth(null) / 2, image.getHeight(null) / 2);
        g2d.setTransform(transform);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return new ImageIcon(rotatedImage);
    }

    private void updateLabelSize() {
        rotatingLabel.setSize(rotatingLabel.getIcon().getIconWidth(), rotatingLabel.getIcon().getIconHeight());
        rotatingLabel.setLocation(0, 0); // Set the location to (0, 0)
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TestRotate();
            }
        });
    }
}
