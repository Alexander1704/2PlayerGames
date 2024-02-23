package assetLoader;

import javax.swing.*;  
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.geom.AffineTransform;
public class ImageLoader{

    public ImageLoader(){

    }
    
    public static void fitImage(JLabel pLabel, String pImagePath) throws IOException {
        Dimension imgSize = getImageSize(pImagePath);
        Dimension labelSize = pLabel.getSize();
        
        double xRatio = 1.0 *  labelSize.getWidth() / imgSize.getWidth();
        double yRatio = 1.0 * labelSize.getHeight() / imgSize.getHeight();
        
        double ratio = Math.min(xRatio, yRatio);
        pLabel.setIcon(getScaledIcon(pImagePath, ratio, ratio));
    }
    public static ImageIcon getResizedIcon(String path, int x_scale, int y_scale) throws IOException {
            // Lade das Bild von der Datei
            Image img = ImageIO.read(new File("assets/" + path));
            
            // Skaliere das Bild auf die Größe des JLabels
            Image scaledImg = img.getScaledInstance(x_scale, y_scale, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
    }
    public static ImageIcon getScaledIcon(String path, double x_scale, double y_scale) throws IOException {
        ImageIcon imageIcon = new ImageIcon(ImageIO.read(new File("assets/" + path))); 
        Image image = imageIcon.getImage(); 
        Image newimg = image.getScaledInstance((int)(imageIcon.getIconWidth() * x_scale), (int) (imageIcon.getIconHeight()*y_scale),  java.awt.Image.SCALE_SMOOTH); 
        imageIcon = new ImageIcon(newimg);  
        return imageIcon;
    }
    
    public static Dimension getImageSize(String path){
        try {
            File file = new File("assets/" + path); 
            Image image = ImageIO.read(file);

            if (image != null) {
                int width = image.getWidth(null); // Breite des Bildes
                int height = image.getHeight(null); // Höhe des Bildes
                return new Dimension(width, height);
            } else {
                System.out.println("Das Bild konnte nicht geladen werden.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ImageIcon flipIcon(Icon originalIcon, boolean horizontal, boolean vertical) {
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

    private static BufferedImage flipBufferedImage(BufferedImage image, boolean horizontal, boolean vertical) {
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

    public static Icon rotateIcon(ImageIcon originalIcon, double angle) {
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

    public static ImageIcon cropIcon(ImageIcon originalIcon, int leftCrop, int rightCrop, int topCrop, int bottomCrop){
        // Zuschneiden des Bildes
        BufferedImage originalImage = new BufferedImage(originalIcon.getIconWidth(), originalIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = originalImage.createGraphics();
        g2d.drawImage(originalIcon.getImage(), 0, 0, null);
        BufferedImage croppedImage = originalImage.getSubimage(leftCrop, topCrop, originalImage.getWidth() - leftCrop - rightCrop, originalImage.getHeight() - topCrop - bottomCrop);
        return new ImageIcon(croppedImage);
        
    }
    

}
