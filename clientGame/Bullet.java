package clientGame;

import serverGame.Positionable;
import assetLoader.ImageLoader;
import javax.swing.*;
import java.awt.*;

public class Bullet extends JLabel implements Positionable{
    private JPanel panel;
    final int ID;
    final boolean rightSided;
    String texture;
    double x; 
    double y;
    public Bullet(JPanel pPanel, int id, String texture, double x, double y, boolean rightSided){
        this.panel = pPanel;
        this.ID = id;
        this.x = x;
        this.y = y;
        this.texture = texture;
        double scaleImg = (panel.getHeight() * 0.125) / 170.0;
        this.rightSided = rightSided;
    }

    public double getXPos(){
        return x;
    }

    public void setXPos(double a){
        this.x = a;
    }

    public double getYPos(){
        return y;
    }

    public void setYPos(double a){
        this.y = a;
    }

    public int getID(){
        return ID;
    }

    public void setTexture(String str){
        texture = str;
    }

    public String getTexture(){
        return texture;
    }

    void turnImage(){
        ImageLoader il = new ImageLoader();
        ImageIcon flippedIcon = il.flipIcon(this.getIcon(), true, false);
        this.setIcon(flippedIcon);
    }

    @Override
    public void setIcon(Icon ic){
        super.setIcon(ic);
    }

    public void imageTurning(){
        if(getIcon() != null && !rightSided) turnImage();
    }

    @Override 
    public boolean equals(Object o){
        if(o instanceof Bullet){
            Bullet b = (Bullet) o;
            if(b.getID() == getID()) return true;
            return false;
        }
        if(o instanceof Integer){
            Integer i = (Integer) o;
            if(getID() == i) return true;
            return false;
        }
        return false;
    }

    public int getScaleWidth(){
        return getWidth();
    }
}
