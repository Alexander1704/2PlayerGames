package serverGame;

import clientGame.*;
import javax.swing.*;

public class Bullet extends JLabel implements Positionable{
    private final int ID;
    private final double MAX_VELO = 0.007;
    private int MAX_UPDATES;
    private final String[] dontCheck = {"Teleporter", "Sword Man", "Zombie", "Boomerang Thrower", "Granate Thrower"};
    private boolean removed;
    private double direction;
    private GameFrame gamePanel;
    private Player player;
    private int updates;
    private double x, y;
    private double size = 0.4;
    private ImageRendering ir;
    public Bullet(int id, GameFrame gamePanel, Player pl){
        ID = id;
        this.gamePanel = gamePanel;
        this.player = pl;
        int bulletDirection = Integer.parseInt(player.getPlayerInfo().getBulletDirection()) ;
        this.direction = bulletDirection/ 180.0 * Math.PI;
        if( !pl.isRightSided()) direction += (180 - (bulletDirection - 90 ) * 2)/ 180.0 * Math.PI; 
        this.updates = 0;
        this.x = pl.getXPos();
        this.y = pl.getYPos() + 1.0 * player.getHeight() / gamePanel.getGamePanel().getHeight() / 2;
        this.removed = false;
        this.ir = new ImageRendering();
        scaleImage();
        init();

        this.update();

        if(!contains(dontCheck, getPlayerInfo().getName()) && gamePanel.checkTop(this)) {
            gamePanel.removeBullet(this);
            return;
        }
        gamePanel.getMessageInterpreter().interpretMessage("BULLET NEW " + ID + " " + x + " " + y + " " + getPlayerInfo().getBulletTexture() + " " + getPlayerInfo().getBulletDirection() + " " + player.isRightSided());
    }

    public int getID(){
        return ID;
    }
    public Player getPlayer(){
        return player;
    }

    private void init(){
        switch(player.getPlayerInfo().getName()){
            case "Sword Man"->{
                    x += Math.sin(direction) * MAX_VELO * 5;
                    y -= 0.125 / 2;
                    MAX_UPDATES = 20;
                }
            case "Teleporter" ->{
                    x += Math.sin(direction) * MAX_VELO * 5;
                    y -= 0.125 / 2;
                    MAX_UPDATES = 20;
                }
            case "Zombie"->{
                    x += Math.sin(direction) * MAX_VELO * 5;
                    y -= 0.125 / 2;
                    MAX_UPDATES = 20;
                }
            default ->{
                    MAX_UPDATES = 200;
                }
        }
    }

    public void setDirection(int bulletDirection){
        if(bulletDirection == -1) return;
        this.direction = bulletDirection/ 180.0 * Math.PI;
        if( !player.isRightSided()) direction += (180 - (bulletDirection - 90 ) * 2)/ 180.0 * Math.PI;
    }

    public void scaleImage(){
        try{
            double scaleImg = (gamePanel.getGamePanel().getHeight() * 0.5) / 170.0;
            scaleImg = 1;
            this.setIcon(ir.getScaledIcon("bullets/" + player.getPlayerInfo().getBulletTexture(), scaleImg, scaleImg));
            this.setSize(this.getPreferredSize());
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!player.isRightSided()) setIcon(ir.flipIcon(this.getIcon(), true, false));
    }

    public boolean checkSides(){
        return !contains(dontCheck, player.getPlayerInfo().getName());
    }

    public void update(){
        gamePanel.getMessageInterpreter().interpretMessage("BULLET UPDATE " + ID + " " + x + " " + y + " " + getPlayerInfo().getBulletTexture() + " " + getPlayerInfo().getBulletDirection());
        
        updates++;
        if(updates > MAX_UPDATES) {
            removed = true;
            gamePanel.removeBullet(this);
            return;
        }
        if((checkSides() && checkEdges((int) (direction * 180 / Math.PI)))){
            gamePanel.removeBullet(this);
            return;
        }
        updateLocation();
        gamePanel.setLocation(this);
        checkTouchingPlayer();
    }

    private void updateLocation(){
        switch(player.getPlayerInfo().getName()){
            case "Sword Man" ->{

                }
            case "Teleporter" ->{

                }
            case "Zombie" ->{

                }
            case "Boomerang Thrower" -> {
                    x += Math.sin(direction) * MAX_VELO;
                    y += Math.cos(direction) * MAX_VELO;
                    direction += 2/ 180.0 * Math.PI;
                    if(direction > 1.8 * Math.PI) gamePanel.removeBullet(this);
                    rotateIcon(direction);
                    this.setSize(this.getIcon().getIconWidth(), this.getIcon().getIconHeight());
                }
            case "Granate Thrower" -> {
                    x += Math.sin(direction) * MAX_VELO;
                    y += Math.cos(direction) * MAX_VELO;
                    direction += (Math.PI - direction) * 0.05;
                    if(gamePanel.checkBottom(this)){
                        direction = 45;
                        x += Math.sin(direction) * MAX_VELO;
                        y += Math.cos(direction) * MAX_VELO;
                    }
                }
            default ->{
                    x += Math.sin(direction) * MAX_VELO;
                    y += Math.cos(direction) * MAX_VELO;
                }
        }
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    public Player.PlayerInfo getPlayerInfo(){
        return player.getPlayerInfo();
    }

    public boolean checkEdges(int direction){
        if(direction < 0) return checkEdges(direction + 360);
        if(direction > 359) return checkEdges(direction - 360);
        switch (direction){
            case 0 -> {
                    return gamePanel.checkTop(this);
                }
            case 90 -> {
                    return gamePanel.checkRight(this);
                }
            case 180 -> {
                    return gamePanel.checkBottom(this);
                }
            case 270 -> {
                    return gamePanel.checkLeft(this);
                }
        }

        if(direction < 90){
            if(gamePanel.checkTop(this)) return true;
            return gamePanel.checkRight(this);
        }
        if(direction < 180){
            if(gamePanel.checkRight(this)) return true;
            return gamePanel.checkBottom(this);
        }
        if(direction < 270){
            if(gamePanel.checkBottom(this)) return true;
            return gamePanel.checkLeft(this);
        }
        if(direction < 360){
            if(gamePanel.checkLeft(this)) return true;
            return gamePanel.checkTop(this);
        }
        return false;
    }

    public void checkTouchingPlayer(){
        Player pl = player.getId() == 0 ? gamePanel.getPlayer2() : gamePanel.getPlayer1();
        if(gamePanel.touching(this, pl)){
            this.removed = true;
            pl.changeHealth(-Integer.parseInt(player.getPlayerInfo().getBulletDamage()));
            doSpecialAbilities();
            gamePanel.removeBullet(this);
        }
    }

    public void doSpecialAbilities(){
        Player pl = player.getId() == 0 ? gamePanel.getPlayer2() : gamePanel.getPlayer1();
        switch (player.getPlayerInfo().getName()){
            case "Snowman" -> {
                    pl.freeze(1000);
                }
            case "Witch" -> {
                    pl.witch(1000);
                }
            case "Zombie" -> {
                    pl.poison(7, 2);
                }
            case "Python" -> {
                    pl.poison(5, 3);
                }
            case "Angel" -> {
                    pl = player.getId() == 1 ? gamePanel.getPlayer2() : gamePanel.getPlayer1();
                    pl.changeHealth(10);
                }
        }
    }

    public double getXPos(){
        return x;
    }

    public double getYPos(){
        return y;
    }

    public boolean contains(Object[] a, Object b){
        if(a == null || b == null) return false;
        for(int i = 0; i < a.length; i++){
            if(a[i].getClass() == b.getClass() && a[i].equals(b)){
                return true;
            }
        }
        return false;
    }

    private void rotateIcon(double degrees){
        this.setIcon(ir.rotateIcon(new ImageIcon("assets/bullets/boomerang.png"), degrees));
        // scaleImage();
    }
    
    public int getScaleWidth(){
        return getWidth();
    }
}
