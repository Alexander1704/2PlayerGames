package serverGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import java.awt.image.*;
import java.awt.geom.AffineTransform;

public class RotatingImageLabel extends JFrame {

    private JLabel rotatingLabel;
    private Timer timer;
    private ImageIcon icon;

    public RotatingImageLabel() {
        setTitle("Rotating Image Label");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setLocationRelativeTo(null);

        // Load an image icon (replace "path_to_your_image.png" with your image path)
        icon = new ImageIcon("assets/bullets/boomerang.png");

        rotatingLabel = new JLabel(icon);
        rotatingLabel.setLocation(0, 0);
        add(rotatingLabel);

        timer = new Timer(50, new RotateLabel());
        timer.start();
    }

    private class RotateLabel implements ActionListener {
        private double angle = 0;

        @Override
        public void actionPerformed(ActionEvent e) {
            angle += 0.1;
            rotatingLabel.setIcon(rotateIcon(new ImageIcon("assets/bullets/boomerang.png"), angle));
        }

        private Icon rotateIcon(ImageIcon originalIcon, double angle) {
            Image image = originalIcon.getImage();
            double sin = Math.abs(Math.sin(angle));
            double cos = Math.abs(Math.cos(angle));
            int newWidth = (int) Math.floor(image.getWidth(null) * cos + image.getHeight(null) * sin) + 100;
            int newHeight = (int) Math.floor(image.getHeight(null) * cos + image.getWidth(null) * sin) + 100;

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

    }

    public static void main() {
        SwingUtilities.invokeLater(() -> {
                    RotatingImageLabel rotatingLabelFrame = new RotatingImageLabel();
                    rotatingLabelFrame.setVisible(true);
            });
    }
}
