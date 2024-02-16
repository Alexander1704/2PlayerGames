package game;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;

public class Healthbar extends JPanel implements Positionable{
    private JPanel gamePanel;
    private  JPanel healthPanel;
    private Healthy player;
    public Healthbar(JPanel gamePanel, Healthy pl){
        super();
        this.gamePanel = gamePanel;
        player = pl;
        setBackground(Color.WHITE);
        Border blackBorder = BorderFactory.createLineBorder(Color.BLACK, 1); // Creating a black 1-pixel border
        this.setBorder(blackBorder);
    }

    @Override 
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(this.getForeground());
        g.fillRect(0, 0, getWidth() * player.getHealth() / 100, getHeight());
    }

    public double getXPos(){
        if(player == null) return 0;
        return player.getXPos() - (1.0 * getWidth() / gamePanel.getWidth()/ 4);
    }

    public double getYPos(){
        if(player == null) return 0;
        return player.getYPos() - (player.getHeight() * 1.0/ gamePanel.getHeight()) * player.getYPos() + (player.getHeight() * 1.0/ gamePanel.getHeight()) + 0.02;
    }

    public void scale(){
        int size = 100;
        setSize((int) (size), (int) (size/ 10));
    }
    
    @Override 
    public int getScaleWidth(){
        return player.getWidth();
    }
}
