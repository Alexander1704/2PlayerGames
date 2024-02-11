package testClient;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.Map;
import java.awt.event.*;

public abstract class Page extends JPanel{
    public abstract String getDescription();
    public abstract void positionElements();
    public abstract void resizeElements();
    public abstract void reloadData();
    public abstract void componentResized();
    
    @Override
    public void paintComponent(Graphics g){
        updateElements();
        super.paintComponent(g);
    }
    public void updateElements(){
        resizeElements();
        positionElements();
    }
    public ImageIcon getResizedIcon(String path, int x_scale, int y_scale) throws IOException {
            // Lade das Bild von der Datei
            Image img = ImageIO.read(new File("assets/" + path));
            
            // Skaliere das Bild auf die Größe des JLabels
            Image scaledImg = img.getScaledInstance(x_scale, y_scale, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
    }
    public BufferedImage scaleImage(BufferedImage originalImage, int newWidth, int newHeight) {
        // Create a new buffered image with the desired width and height
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        
        // Get the Graphics2D object of the resized image
        Graphics2D g2d = resizedImage.createGraphics();
        
        // Scale and draw the original image onto the resized image
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        
        // Dispose the Graphics2D object
        g2d.dispose();
        
        return resizedImage;
    }
    public ImageIcon getScaledIcon(String path, double x_scale, double y_scale) throws IOException {
        ImageIcon imageIcon = new ImageIcon(ImageIO.read(new File("assets/" + path))); 
        Image image = imageIcon.getImage(); 
        Image newimg = image.getScaledInstance((int)(imageIcon.getIconWidth() * x_scale), (int) (imageIcon.getIconHeight()*y_scale),  java.awt.Image.SCALE_SMOOTH); 
        imageIcon = new ImageIcon(newimg);  
        return imageIcon;
    }
    public Font loadFont(String fontPath, int size) {
        Font customFont = null;
        try {
            File fontFile = new File(fontPath);
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.PLAIN, 24);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return customFont;
    }
    
    public void warte(int time){
        try{
            Thread.sleep(time);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void warteSolange(boolean b){
        while(b){
            warte(10);
        }
    }
}
