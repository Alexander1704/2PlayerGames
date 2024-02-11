package game;

import database.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends JLabel implements Healthy{
    public class PlayerInfo{
        DBController dbc;
        String name;
        String texture;
        String bullet_texture;
        String bullet_damage;
        String bullet_direction;
        PlayerInfo(String name){
            dbc = new DBController();
            String[] info = dbc.getPlayerInfo(name);
            init(name, info[1], info[2], info[3], info[4]);
            System.out.println(info[4]);
        }
        // PlayerInfo(String name, String texture, String bullet_texture, String bullet_damage){
            // init(name, texture, bullet_texture, bullet_damage);
        // }
        private void init(String name, String texture, String bullet_texture, String bullet_damage, String bullet_direction){
            this.name = name;
            this.texture = texture;
            this.bullet_texture = bullet_texture;
            this.bullet_damage = bullet_damage;
            this.bullet_direction = bullet_direction;
        }
        public String getName(){
            return name;
        }
        public String getTexture(){
            return texture;
        }
        public String getBulletTexture(){
            return bullet_texture;
        }
        public String getBulletDamage(){
            return bullet_damage;
        }
        public String getBulletDirection(){
            return bullet_direction;
        }
    }
    private final double MAX_VELO = 0.005;
    private final double MAX_JUMP_VELO = MAX_VELO * 5;
    private final double POS_ACCURACY = MAX_VELO;
    private final PlayerInfo playerInfo;
    private boolean rightSided;
    private boolean falling;
    private boolean reloadingBullet;
    private boolean frozen, witched;
    private double xPos, yPos;
    private double xVelo, yVelo;
    private int health;
    private final int id;
    private GameFrame gamePanel;
    private ImageRendering ir;
    public Player(GameFrame pGamePanel, String name, int id){
        this.playerInfo = new PlayerInfo(name);
        this.xPos = id == 0 ?  0.1 : 0.9; 
        this.yPos = 0.5;
        this.xVelo = 0;
        this.yVelo = 0;
        this.health = 100;
        this.rightSided = id == 0 ? true : false;
        this.falling = true;
        this.frozen = false;
        this.witched = false;
        this.gamePanel = pGamePanel;
        this.reloadingBullet = false;
        this.ir = new ImageRendering();
        this.id = id;
        scaleImage();
    }
    public boolean rightSided(){
        return rightSided;
    }
    public double getXPos(){
        return xPos;
    }
    public double getYPos(){
        return yPos;
    }
    public int getHealth(){
        return health;
    }
    public boolean onGround(){
        return !falling;
    }
    public boolean isMoving(){
        return xVelo != 0 || yVelo != 0;
    }
    @Override
    public String toString(){
        return xPos + " " + yPos + " " + health + " " + witched;
    }
    public String getCharacter(){
        return "angel";
    }
    public int getId(){
        return id;
    }
    public PlayerInfo getPlayerInfo(){
        return playerInfo;
    }
    public synchronized void freeze(int time){
        Thread freezeThread = new Thread(new Runnable(){
            public void run(){
                frozen = true;
                warte (time);
                frozen = false;
            }
        });
        freezeThread.start();
    }
    public synchronized void witch(int time){
        Thread witchThread = new Thread(new Runnable(){
            public void run(){
                gamePanel.getMessageInterpreter().interpretMessage("WITCH " + id + " true");
                witched = true;
                scaleImage();
                warte (time);
                gamePanel.getMessageInterpreter().interpretMessage("WITCH " + id + " false");
                witched = false;
                scaleImage();
            }
        });
        witchThread.start();
    }
    public synchronized void poison(int times, int damage){
        Thread witchThread = new Thread(new Runnable(){
            public void run(){
                for(int i = 0; i < times; i++){
                    changeHealth(-damage);
                    warte(500);
                }
            }
        });
        witchThread.start();
    }

    public void changeHealth(int a){
        health += a;
        if(health > 100) health = 100;
        gamePanel.getMessageInterpreter().interpretMessage("HEALTH " + id  + " " + health);
    }
    public void scaleImage(){
        try{
            double scaleImg = (gamePanel.getGamePanel().getHeight() * 0.125) / 170.0;
            if(witched) this.setIcon(ir.getScaledIcon("player/Frog.png", scaleImg, scaleImg));
            else this.setIcon(ir.getScaledIcon("player/" + playerInfo.getTexture(), scaleImg, scaleImg));
            this.setSize(this.getPreferredSize());
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!rightSided) turnPlayerImage();
    }

    public void changeX(double a){
        if(a != 0 && !gamePanel.checkBottom(this)) falling = true;
        if(a < 0 && gamePanel.checkLeft(this) || xVelo > 0 && gamePanel.checkRight(this)) {
            xVelo = 0;
            return;
        }
        xPos += a;
        if( xPos < 0) xPos = 0;
        if( xPos > 1) xPos = 1;
    }

    public void changeY(double a){        
        for(int i = 1; i < Math.abs(a/ POS_ACCURACY) ; i++){
            if(a < 0 && gamePanel.checkBottom(this) || a > 0 && gamePanel.checkTop(this)){
                yVelo = 0;
                if(gamePanel.checkBottom(this)){
                    falling = false;
                    while(gamePanel.checkBottom(this)){
                        yPos+= POS_ACCURACY / 5;
                        gamePanel.setLocation(this);
                    }
                    // for(int j = 0; j < 5; j++){
                        // if(gamePanel.checkBottom(this)){
                            // yPos += POS_ACCURACY / 5;
                            // gamePanel.setLocation(this);
                        // }else{
                            // break;
                        // }
                    // }
                    // if(gamePanel.checkBottom(this)) {
                        // yPos -= POS_ACCURACY/ 5 * 5;
                        // gamePanel.setLocation(this);
                    // }
                }
                return;
            }
            yPos += a < 0 ? -POS_ACCURACY : POS_ACCURACY;
        }
        yPos += a % POS_ACCURACY;
        // if(falling && gamePanel.checkBottom(this)) falling = false;
        if(falling) yVelo -= MAX_JUMP_VELO / 24;
        if(!falling ){

        }
        if( yPos < 0) {
            yPos = 0;
            yVelo = 0;
        }
        if( yPos > 1){
            yPos = 1;
            yVelo = 0;
        }
    }

    public void update(){
        double velo_change = 1;
        if(frozen) velo_change = 0.2;
        changeX(xVelo * velo_change);
        changeY(yVelo * velo_change);
        xVelo *= 0.9;
        if(Math.abs(xVelo) < 0.001) xVelo = 0;
        // if(Math.abs(yVelo) < 0.001) yVelo = 0;
        gamePanel.setLocation(this);
    }

    public void getInput(String a){
        switch (a){
            case "jump" ->{
                    if(onGround()) {
                        falling = true;
                        yVelo = MAX_JUMP_VELO;
                    }
                }
            case "left" ->{ 
                    xVelo =  gamePanel.checkLeft(this) ? 0 : -MAX_VELO;
                    if(rightSided) {
                        rightSided = false;
                        turnPlayerImage();
                    }
                    
                }
            case "right" ->{
                    xVelo =  gamePanel.checkRight(this) ? 0 : MAX_VELO;
                    if(!rightSided){
                        rightSided = true;
                        turnPlayerImage();
                    }
                    
                }
            case "ability" ->{
                if(frozen || witched) return;
                if(!reloadingBullet){
                    // System.out.println("ABILITY");
                    reloadingBullet = true;
                    activateAbility();
                    Thread reloadBullet = new Thread(new Runnable(){
                        public void run(){
                            warte(1000);
                            reloadingBullet = false;
                        }
                    });
                    reloadBullet.start();
                }
                
            }
        }
    }
    private void activateAbility(){
        switch(playerInfo.getName()){
            case "Soldier" ->{
                int dir = 135;
                for(int i = 0; i < 3; i++){
                    gamePanel.addBullet(this, dir);
                    dir -= 25;
                }
            }
            case "Criminal" -> {
                int dir = 50;
                for(int i = 0; i < 6; i++){
                    gamePanel.addBullet(this, dir);
                    dir += 15;
                }
            }
            case "Teleporter" ->{
                gamePanel.addBullet(this, -1);
                warte(100);
                xPos += rightSided ? 0.11 : -0.11;
                while(gamePanel.checkBottom(this)){
                    yPos += POS_ACCURACY;
                    gamePanel.setLocation(this);
                }
                falling = true;
            }
            default -> {
                gamePanel.addBullet(this, -1);
            }
        }
    }

    public void turnPlayerImage(){
        ImageIcon flippedIcon = ir.flipIcon(this.getIcon(), true, false);
        this.setIcon(flippedIcon);
        gamePanel.getMessageInterpreter().interpretMessage("RIGHTSIDED " + id  + " " + rightSided);
    }
    public void warte(int time){
        try{
            Thread.sleep(time);
        }
        catch (InterruptedException ie){
            ie.printStackTrace();
        }
    }
    
    public int getScaleWidth(){
        return getWidth();
    }
}
