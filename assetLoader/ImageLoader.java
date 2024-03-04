package assetLoader;

import javax.swing.*;  
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.geom.AffineTransform;

/**In dieser Klasse werden typische Funktionen, die in Verbindung mit dem Laden 
 * eines Bildes stehten, zur Verfügung gestellt.
 */
public class ImageLoader{

    /**Erstellt ein neues Objekt der Klasse ImageLoader. Ein ImageLoader-Objekt wird
     * meist allerdings nicht benötigt, da alle Methoden statisch sind.
     */
    public ImageLoader(){

    }

    /**Setze das ImageIcon eines JLabels mithilfe eines Pfades eines Bildes so, dass 
     * es die größtmögliche Größe erreicht ohne die Relationen des Bildes zu ändern.
     * Wenn das Bild nicht gefunden werden kann, wird ein Fehler ausgegeben.
     * 
     * @param pLabel JLabel, bei dem das ImageIcon gesetzt werden soll
     * @param pImagePath Pfad des zu setzenden Bildes (Pfad startet im assets-Ordner)
     */
    public static void fitImage(JLabel pLabel, String pImagePath) throws IOException{
        Dimension imgSize = getImageSize(pImagePath);
        if(imgSize == null) {
            System.err.println("Bild mit dem Pfad: assets/" + pImagePath + " konnte nicht gefunden werden :(");
            return;
        }
        Dimension labelSize = pLabel.getSize();

        double xRatio = 1.0 * labelSize.getWidth()  / imgSize.getWidth();
        double yRatio = 1.0 * labelSize.getHeight() / imgSize.getHeight();

        double ratio = Math.min(xRatio, yRatio);
        pLabel.setIcon(getScaledIcon(pImagePath, ratio, ratio));
    }

    /**Erstelle ein ImageIcon, mit einer absoluten festgelegten Größe und gib
     * es zurück. Das ImageIcon kann bei einem anderen Verhältnis als das Ausgangsbild
     * auch verzerrt werden.
     * 
     * @param pPath Pfad in dem das Bild liegt (Pfad startet im assets-Ordner)
     * @param pXScale absolute Breite des ImageIcons
     * @param pYScale absolute Höhe des ImageIcons
     */
    public static ImageIcon getResizedIcon(String pPath, int pXScale, int pYScale) throws IOException {
        Image img = ImageIO.read(new File("assets/" + pPath));
        Image scaledImg = img.getScaledInstance(pXScale, pYScale, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }

    /**Erstelle ein ImageIcon, das relativ zu der Größe des Bildes skaliert wird, und gib
     * es zurück.
     * 
     * @param pPath Pfad in dem das Bild liegt (Pfad startet im assets-Ordner)
     * @param pXScale relative Breite des ImageIcons im Verhältnis zur Originalbreite des Bildes
     * @param pYScale relative Höhe des ImageIcons im Verhältnis zur Originalhöhe des Bildes
     */
    public static ImageIcon getScaledIcon(String pPath, double pXScale, double pYScale) throws IOException {
        ImageIcon imageIcon = new ImageIcon(ImageIO.read(new File("assets/" + pPath))); 
        Image image = imageIcon.getImage(); 
        Image newimg = image.getScaledInstance((int)(imageIcon.getIconWidth() * pXScale), (int) (imageIcon.getIconHeight()*pYScale),  java.awt.Image.SCALE_SMOOTH); 
        imageIcon = new ImageIcon(newimg);  
        return imageIcon;
    }

    /**Gib die Größe eines Bildes als Dimension zurück. Wenn das Bild nicht gefunden werden
     * kann bzw. das Bild nicht geladen werden kann, wird null zurückgegeben.
     * 
     * @param pPath Pfad des Bildes, im assets-Ordner startend
     * @return Dimension des Bildes
     */
    //mithilfe von StackOverflow erstellt
    public static Dimension getImageSize(String path){
        try {
            File file = new File("assets/" + path); 
            Image image = ImageIO.read(file);

            if (image != null) {
                int width = image.getWidth(null); 
                int height = image.getHeight(null);
                return new Dimension(width, height);
            } else {
                System.out.println("Das Bild konnte nicht geladen werden.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**Dreht ein ImageIcon horizontal und vertical und gibt es zurück.
     * 
     *@param originalIcon Icon, das  gedreht werden soll.
     *@param horizontal soll Icon horizontal gedreht werden? 
     *@param vertical soll Icon vertikal gedreht werden?
     */
    //mithilfe von chatgpt erstellt
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

    /**Hilfsmethode für die flipIcon-Methode
     */ 
    //mithilfe von chatgpt erstellt
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
}
