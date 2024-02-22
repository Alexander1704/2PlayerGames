package assetLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class OutlinedLabel extends JLabel { 
    private int outlineSize;
    private Color outlineColor;
    public OutlinedLabel(String pText){
        super(pText);
        this.outlineSize = 2;
        this.outlineColor = Color.BLACK;
    }
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

        g2.drawString(getText(), x - outlineSize, y);
        g2.drawString(getText(), x + outlineSize, y);
        g2.drawString(getText(), x, y - outlineSize);
        g2.drawString(getText(), x, y + outlineSize);

        g2.setColor(getForeground());
        g2.drawString(getText(), x, y);

        g2.dispose();
    }
    public void setOutlineSize(int pSize){
        this.outlineSize = pSize;
    }
    public void setOutlineColor(Color pColor){
        this.outlineColor = pColor;
    }
}
