package assetLoader;

import java.awt.*; 
import java.io.*; 
import javax.swing.JLabel;


public class FontLoader{
    
    public FontLoader(){
        
    }
    
    public static Font loadFont(String fontPath, int size) {
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
    
    public static void scaleLabel(JLabel label) {
        Font labelFont = label.getFont();
        String labelText = label.getText();

        // Berechne die Breite des Textes im JLabel
        int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);

        // Erhalte die aktuelle Breite und Höhe des JLabels
        int componentWidth = label.getWidth();
        int componentHeight = label.getHeight();

        // Berechne die Skalierungsfaktoren für Breite und Höhe
        double widthRatio = (double) componentWidth / (double) stringWidth;
        int newFontSize = (int) (labelFont.getSize() * widthRatio);

        // Setze die neue Schriftgröße, sodass der Text passt
        int fontSizeToUse = Math.min(newFontSize, componentHeight); // verhindere zu große Schrift
        label.setFont(new Font(labelFont.getName(), labelFont.getStyle(), fontSizeToUse));
    }
}