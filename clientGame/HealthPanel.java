package clientGame;

import serverGame.Positionable;
import assetLoader.*;
import javax.swing.*;
import java.awt.*;

public class HealthPanel extends JPanel implements Positionable{
    private JPanel panel;
    private JLabel healthLabel;
    private JPanel healthPanel;
    private JPanel healthLine;
    private double health;
    private double xPos;
    private double yPos;

    public HealthPanel(JPanel pPanel){
        setLayout(null);
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        this.panel = pPanel;

        healthLabel = new JLabel("100%");
        healthLabel.setLocation(0, 0);
        healthLabel.setSize(healthLabel.getPreferredSize());
        healthLabel.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        add(healthLabel);
        
        healthPanel = new JPanel();
        healthPanel.setLayout(null);
        healthPanel.setLocation(0, 0);
        healthPanel.setSize(100, 100);
        healthPanel.setBackground(Color.WHITE);
        healthPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        add(healthPanel);
        
        healthLine = new JPanel();
        healthLine.setLocation(0, 0);
        healthLine.setSize(healthPanel.getSize());
        healthLine.setBackground(Color.RED);
        healthLine.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        healthPanel.add(healthLine);

        health = 100;
        xPos = 0.5;
        yPos = 0.5;
    }

    public void setHealth(int pHealth){
        this.healthLabel.setText(pHealth + "%");
        this.health = pHealth;
        update();
    }

    public double getXPos(){
        return xPos;
    }

    public double getYPos(){
        return yPos;
    }
    
    public void setPosition(double pX, double pY){
        this.xPos = pX;
        this.yPos = pY;
    }

    public void update(){
        int size = (int) (panel.getHeight() * 0.2);
        setSize((int) (size), (int) (size/ 2));
        
        healthPanel.setSize(getWidth(), (getHeight())/ 2 + 1);
        healthPanel.setLocation(0, getHeight() / 2);
        
        healthLine.setSize((int) (healthPanel.getWidth() * (health / 100.0)), healthPanel.getHeight());
        
        healthLabel.setSize( (int) (getWidth() * 0.8), (int) (getHeight() / 2 * 0.8));
        FontLoader.scaleLabel(healthLabel);
        healthLabel.setSize(healthLabel.getPreferredSize());
        healthLabel.setLocation( (getWidth() - healthLabel.getWidth() ) / 2 , (getHeight() / 2 - healthLabel.getHeight() ) / 2);
    }
    
    public void componentResized(){
        
    }

    @Override 
    public int getScaleWidth(){
        return this.getWidth();
    }

    public void setColor(Color pColor){
        healthLine.setBackground(pColor);
    }
}
