package serverGame;

import assetLoader.*;
import clientGame.*;
import database.*;

public class Player extends clientGame.Player implements Healthy{
    public class PlayerInfo{
        DBController dbc;
        String name;
        String texture;
        String bullet_texture;
        String bullet_damage;
        String bullet_direction;
        PlayerInfo(String name){
            dbc = new DBController();
            String[] info = dbc.getPlayerInfo("Player");
            init(name, info[1], info[2], info[3], info[4]);
        }

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
    private final double MAX_JUMP_VELO = 0.025;
    private final double POS_ACCURACY = MAX_VELO;
    private final int ID;
    private final PlayerInfo playerInfo;
    private boolean initialized;
    private boolean falling;
    private boolean reloadingBullet;
    private boolean frozen;
    private double xVelo, yVelo;
    private int health;
    private int animationNum;
    private GameFrame gamePanel;
    public Player(GameFrame pGamePanel, String pName, int pID){
        super(pGamePanel.getGamePanel());
        gamePanel = pGamePanel;
        playerInfo = new PlayerInfo(pName);
        ID = pID;
        initialized = false;
        
        xPos = ID == 0 ?  0.1 : 0.9; 
        yPos = 0.5;
        xVelo = 0;
        yVelo = 0;
        health = 100;
        rightSided = ID == 0 ? true : false;
        falling = true;
        reloadingBullet = false;
        animationNum = 10;
        animation = 10;
        frozen = false;
        
        updateImage();
        gamePanel.getMessageInterpreter().interpretMessage("RIGHTSIDED " + ID + " " + rightSided);

        final int MAX_ANIMATION = 9;
        TickThread animationThread = new TickThread(15, new Runnable(){
                    @Override 
                    public void run(){
                        if(isMoving()){
                            animationNum++;
                            if(animationNum > MAX_ANIMATION) animationNum = 1;
                            sendAnimationNum();
                        }else{
                            if(animationNum != 10){
                                if (animationNum == 7 || animationNum == 8 || animationNum == 9) {
                                    animationNum = 10;
                                }else{
                                    if(animationNum > 8 || animationNum == 2){
                                        animationNum--;
                                    }else if(animationNum == 1){
                                        animationNum = 9;
                                    }else {
                                        animationNum++;
                                    }
                                }
                                sendAnimationNum();
                            }
                        } 

                    }
                });
        animationThread.start();
    }

    public void sendAnimationNum(){
        gamePanel.getMessageInterpreter().interpretMessage("ANIMATION " + ID + " " + animationNum);
    }

    public String getPosition(){
        return xPos + " " + yPos;
    }

    public int getHealth(){
        return health;
    }

    public boolean onGround(){
        return !falling;
    }

    public boolean isMoving(){
        return initialized && (xVelo != 0 || yVelo != 0);
    }

    @Override
    public String toString(){
        return xPos + " " + yPos + " " + health + " " + witched + " " + animationNum;
    }

    public String getCharacter(){
        return playerInfo.getName();
    }

    public int getId(){
        return ID;
    }

    public PlayerInfo getPlayerInfo(){
        return playerInfo;
    }
    
    public boolean isInitialized(){
        return initialized;
    }

    public synchronized void freeze(int time){
        Thread freezeThread = new Thread(new Runnable(){
                    public void run(){
                        frozen = true;
                        FunctionLoader.warte (time);
                        frozen = false;
                    }
                });
        freezeThread.start();
    }

    public synchronized void witch(int time){
        Thread witchThread = new Thread(new Runnable(){
                    public void run(){
                        gamePanel.getMessageInterpreter().interpretMessage("WITCH " + ID + " true");
                        witched = true;
                        updateImage();
                        FunctionLoader.warte (time);
                        gamePanel.getMessageInterpreter().interpretMessage("WITCH " + ID + " false");
                        witched = false;
                        updateImage();
                    }
                });
        witchThread.start();
    }

    public synchronized void poison(int times, int damage){
        Thread witchThread = new Thread(new Runnable(){
                    public void run(){
                        for(int i = 0; i < times; i++){
                            changeHealth(-damage);
                            FunctionLoader.warte(500);
                        }
                    }
                });
        witchThread.start();
    }

    public void changeHealth(int a){
        health += a;
        if(health > 100) health = 100;
        gamePanel.getMessageInterpreter().interpretMessage("HEALTH " + ID  + " " + health);
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
                        yPos += POS_ACCURACY / 5;
                        gamePanel.setLocation(this);
                    }
                    yPos -= POS_ACCURACY;
                    initialized = true;
                }
                return;
            }
            yPos += a < 0 ? -POS_ACCURACY : POS_ACCURACY;
        }
        yPos += a % POS_ACCURACY;
        // if(falling && gamePanel.checkBottom(this)) falling = false;
        if(falling) yVelo -= MAX_JUMP_VELO / 15;
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
        changeX(xVelo * 0.8);
        changeY(yVelo * velo_change);
        xVelo *= 0.9;
        if(Math.abs(xVelo) < 0.001) xVelo = 0;
        gamePanel.setLocation(this);
    }
    
    public void updateImage(){
        super.update();
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
                        gamePanel.getMessageInterpreter().interpretMessage("RIGHTSIDED " + ID + " " + rightSided);
                        updateImage();
                    }

                }
            case "right" ->{
                    xVelo =  gamePanel.checkRight(this) ? 0 : MAX_VELO;
                    if(!rightSided){
                        rightSided = true;
                        gamePanel.getMessageInterpreter().interpretMessage("RIGHTSIDED " + ID + " " + rightSided);
                        updateImage();
                    }

                }
            case "ability" ->{
                    if(frozen || witched) return;
                    if(!reloadingBullet){
                        reloadingBullet = true;
                        activateAbility();
                        Thread reloadBullet = new Thread(new Runnable(){
                                    public void run(){
                                        FunctionLoader.warte(1000);
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
                    FunctionLoader.warte(100);
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
}
