package clientGame;

import serverGame.Positionable;
import assetLoader.ImageLoader;
import javax.swing.*;
import java.awt.*;

public class Player extends JLabel implements Positionable{ 
    private JPanel panel;
    protected String name;
    protected double xPos; 
    protected double yPos;
    protected double size;
    protected int health;
    protected String texture;
    protected boolean witched;
    protected boolean rightSided;
    protected int animation; 
    public Player(JPanel pPanel){
        panel = pPanel;
        xPos = 0;
        yPos = 0;
        this.size = 1 / 1700.0;
        health = 100;
        texture = "";
        witched = false;
        rightSided = true;
        name = "Player";
        animation = 10;
    }
    
    public void update(){
        double scaleImg = panel.getHeight() * this.size;
        if(scaleImg <= 0) {
            return;
        }
        try{
            this.setIcon(ImageLoader.getScaledIcon("player/" + this.name + "/animation" + this.animation + ".png", scaleImg, scaleImg));
        }catch (Exception e){
            // System.out.println("Error while loading PlayerImage, FileName: assets/player/" + this.name + "/animation" + this.animation + ".png");
            e.printStackTrace();
        }
        if(! this.rightSided) this.setIcon(ImageLoader.flipIcon(this.getIcon(), true, false));
        this.setSize(this.getPreferredSize());
    }

    public double getXPos(){
        return xPos;
    }

    public double getYPos(){
        return yPos;
    }

    public void setX(double pXPos){
        this.xPos = pXPos;
    }

    public void setY(double pYPos){
        this.yPos = pYPos;
    }
    
    public void setSize(double pSize){
        this.size = pSize;
    }

    public void setHealth(int a){
        health = a;
    }

    public int getHealth(){
        return health;
    }

    public int getAnimation(){
        return this.animation;
    }
    
    public void setAnimation(int pAnimationNum){
        this.animation = pAnimationNum;
    }

    public void setWitched(boolean b){
        witched = b;
    }

    public boolean isWitched(){
        return witched;
    }

    public int getScaleWidth(){
        return getWidth();
    }

    public boolean isRightSided(){
        return rightSided;
    }
    
    public void setRightSided(boolean pRightSided){
        this.rightSided = pRightSided;
        update();
    }
}