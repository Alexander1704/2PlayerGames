package assetLoader;

import java.awt.*; 
import java.io.*; 
import javax.swing.*;

/**In dieser Klasse werden typische Funktionen, die in Verbindung mit dem Laden 
 * einer Schriftart stehen, zur Verfügung gestellt.
 */
public class FontLoader{
    
    /**Erstellt ein neues Objekt der Klasse FontLoader. Ein FontLoader-Objekt wird
     * meist allerdings nicht benötigt, da alle Methoden statisch sind.
     */
    public FontLoader(){
        
    }
    
    /**Lädt FontArt mithilfe einer FontDatei, die sich in einem abgespeicherten Pfad befindet
     * und einer Größe
     * 
     * @param pFontPath Pfad der Font , welche im Asset-Ordner startet
     * @param pSize Größe der Font
     */
    //mithilfe StackOverflow erstellt
    public static Font loadFont(String pFontPath, int pSize) {
        Font customFont = null;
        try {
            File fontFile = new File("assets/" + pFontPath);
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.PLAIN, pSize);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        return customFont;
    }
    
    /**Setzt die Größe der Font eines JLabels, sodass diese an die Größe des Labels angepasst ist
     *
     *@param label JLabel bei dem die Fontgröße angepasst werden soll 
     */
    //mithilfe von chatgpt erstellt
     public static void fitFont(JLabel label) {
        Font labelFont = label.getFont();
        String labelText = label.getText();

        //damit Division durch 0 nicht zu einer zu kleinen Fontgröße führt
        if (labelFont.getSize() == 0) { 
            labelFont = labelFont.deriveFont(10f);
            label.setFont(labelFont);
        }

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
    
    /**Setzt die Größe der Font eines JButtons, sodass diese an die Größe des Buttons angepasst ist
     *
     *@param label JButton bei dem die Fontgröße angepasst werden soll  
     */
    //mithilfe von chatgpt erstellt
    public static void fitFont(JButton label) {
        Font labelFont = label.getFont();
        String labelText = label.getText();

        // Berechne die Breite des Textes im JLabel
        int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);

        // Erhalte die aktuelle Breite und Höhe des JLabels
        int componentWidth = label.getWidth() - 25;
        int componentHeight = label.getHeight() - 25;

        // Berechne die Skalierungsfaktoren für Breite und Höhe
        double widthRatio = (double) componentWidth / (double) stringWidth;
        int newFontSize = (int) (labelFont.getSize() * widthRatio);

        // Setze die neue Schriftgröße, sodass der Text passt
        int fontSizeToUse = Math.min(newFontSize, componentHeight); // verhindere zu große Schrift
        label.setFont(new Font(labelFont.getName(), labelFont.getStyle(), fontSizeToUse));
    }
}