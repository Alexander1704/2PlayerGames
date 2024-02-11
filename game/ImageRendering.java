package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
public class ImageRendering{

    public ImageRendering(){

    }

    public ImageIcon getScaledIcon(String path, double x_scale, double y_scale) throws IOException {
        ImageIcon imageIcon = new ImageIcon(ImageIO.read(new File("assets/" + path))); 
        Image image = imageIcon.getImage(); 
        Image newimg = image.getScaledInstance((int)(imageIcon.getIconWidth() * x_scale), (int) (imageIcon.getIconHeight()*y_scale),  java.awt.Image.SCALE_SMOOTH); 
        imageIcon = new ImageIcon(newimg);  
        return imageIcon;
    }

    public void warte(int time){
        try{
            Thread.sleep(time);
        }
        catch (InterruptedException ie){
            ie.printStackTrace();
        }
    }

    public ImageIcon flipIcon(Icon originalIcon, boolean horizontal, boolean vertical) {
        int width = originalIcon.getIconWidth();
        int height = originalIcon.getIconHeight();

        // Create a BufferedImage to draw the icon onto
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        // Draw the original icon onto the BufferedImage
        originalIcon.paintIcon(null, g2d, 0, 0);
        g2d.dispose();

        // Flip the BufferedImage
        BufferedImage flippedImage = flipBufferedImage(bufferedImage, true, false); // true for horizontal flip, false for vertical

        // Create a new ImageIcon from the flipped BufferedImage
        return new ImageIcon(flippedImage);
    }

    private BufferedImage flipBufferedImage(BufferedImage image, boolean horizontal, boolean vertical) {
        int width = image.getWidth();
        int height = image.getHeight();

        BufferedImage flippedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = flippedImage.createGraphics();

        // Define the AffineTransform for flipping
        AffineTransform transform = new AffineTransform();
        if (horizontal) {
            transform.translate(width, 0);
            transform.scale(-1, 1);
        }
        if (vertical) {
            transform.translate(0, height);
            transform.scale(1, -1);
        }

        g2d.transform(transform);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return flippedImage;
    }

    public Icon rotateIcon(ImageIcon originalIcon, double angle) {
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
}