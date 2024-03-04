package assetLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**Ein OutlindedLabel ist ein JLabel mit einer Umrandung.
 */
public class OutlinedLabel extends JLabel { 
    private int outlineSize;
    private Color outlineColor;
    
    /**Konstruktor des Outlined Label, der ein neues Objekt der Klasse OutlinedLabel
     * erstellt und initialisiert.
     * 
     * @param pText Text, der auf dem JLabel dargestellt werden soll. Wenn kein Text dargestellt
     *      werden soll, kann das Label auf "" gesetzt werden.
     */
    public OutlinedLabel(String pText){
        super(pText);
        this.outlineSize = 2;
        this.outlineColor = Color.BLACK;
    }
    
    /**Das Outlined Label überschreibt die paintComponent-Methode, sodass es eine Umrandung um
     * das gesamt Text Objekt gibt. 
     * 
     * @param g Graphic-Objekt, auf dem der Text gemalt werden soll
     */
    //mit StackOverflow erstellt
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(outlineColor);
        g2.setFont(getFont());

        FontMetrics metrics = g2.getFontMetrics();
        Rectangle2D bounds = metrics.getStringBounds(getText(), g2);

        int x = (getWidth() - (int) bounds.getWidth()) / 2;
        int y = ((getHeight() - (int) bounds.getHeight()) / 2) + metrics.getAscent();

        //Umrandung des Textes
        g2.drawString(getText(), x - outlineSize, y);
        g2.drawString(getText(), x + outlineSize, y);
        g2.drawString(getText(), x, y - outlineSize);
        g2.drawString(getText(), x, y + outlineSize);

        g2.setColor(getForeground());
        g2.drawString(getText(), x, y);

        g2.dispose();
    }
    
    /**Setze die Umrandungsgröße des OutlinedLabels
     * 
     * @param pSize Größe der Umrandung
     */
    public void setOutlineSize(int pSize){
        this.outlineSize = pSize;
    }
    
    /**Setze die Farbe der Umrandung
     * 
     * @param pColor Farbe der Umrandung
     */
    public void setOutlineColor(Color pColor){
        this.outlineColor = pColor;
    }
}
