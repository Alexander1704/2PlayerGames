package clientGame;

import serverGame.Positionable;
import assetLoader.ImageLoader;
import javax.swing.*;
import java.awt.*;

public class Player extends JLabel implements Positionable{ 
    private JPanel panel;
    private ImageLoader ir; 
    private String name;
    private double x; 
    private double y;
    private double size;
    private int health;
    private String texture;
    private boolean witched;
    private boolean rightSided;
    private int animation;
    public Player(JPanel pPanel){
        panel = pPanel;
        ir = new ImageLoader();
        x = 0;
        y = 0;
        this.size = 1 / 1700.0;
        health = 100;
        texture = "";
        witched = false;
        rightSided = true;
        name = "Player";
        animation = 1;
    }
    
    public void update(){
        ImageLoader imageLoader = new ImageLoader();
        double scaleImg = panel.getHeight() * this.size;
        if(scaleImg <= 0) return;
        // System.out.println(scaleImg);
        try{
            this.setIcon(ImageLoader.getScaledIcon("player/" + this.name + "/animation" + this.animation + ".png", scaleImg, scaleImg));
        }catch (Exception e){
            System.out.println("Error while loading PlayerImage, FileName: assets/player/" + this.name + "/animation" + this.animation + ".png");
            e.printStackTrace();
        }
        if(! this.rightSided) setIcon(ir.flipIcon(this.getIcon(), true, false));
        this.setSize(this.getPreferredSize());
    }

    public double getXPos(){
        return x;
    }

    public double getYPos(){
        return y;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
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

    public void setRightSided(boolean b){
        rightSided = b;
    }
}