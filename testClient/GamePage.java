package testClient;

import assetLoader.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.ArrayList;

import clientGame.*;
import serverGame.Positionable;

public class GamePage extends Page implements KeyListener{
    
    private GUI gui;
    TickThread repaintThread;
    private JPanel gamePanel;
    private Player[] player;
    private HealthPanel[] healthPanel;
    private JLabel backgroundLabel;
    private JLabel mapLabel;
    private boolean[] keyPressed;
    private ArrayList<Bullet> bulletList;
    private JLabel gameClosingLabel;
    private JLabel winnerLabel;
    private OutlinedLabel startingLabel;
    private Banner player0Banner;
    private Banner player1Banner;
    private int mapNum;
    

    GamePage(GUI gui){
        this.gui = gui;
        setLayout(null);

        gamePanel = new JPanel();
        gamePanel.setLayout(null);
        gamePanel.setBackground(Color.BLUE);
        gamePanel.setSize(710, 400);
        add(gamePanel);

        player = new Player[2];
        healthPanel = new HealthPanel[player.length];
        for(int i = 0; i < player.length; i++){
            player[i] = new Player(gamePanel);
            gamePanel.add(player[i]);

            healthPanel[i] = new HealthPanel(gamePanel);
            gamePanel.add (healthPanel[i]);
        }
        
        healthPanel[0].setPosition(0., 0); 
        healthPanel[1].setPosition(1, 0);

        mapLabel = new JLabel(); 
        gamePanel.add(mapLabel);

        backgroundLabel = new JLabel();
        backgroundLabel.setLocation(0, 0);
        gamePanel.add(backgroundLabel);

        bulletList = new ArrayList<Bullet>();

        gameClosingLabel = new JLabel("GAME IS CLOSING...");
        gameClosingLabel.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",50));
        gameClosingLabel.setVisible(false);
        gameClosingLabel.setForeground(Color.RED);
        gamePanel.add(gameClosingLabel);
        gamePanel.setComponentZOrder(gameClosingLabel, 0);

        winnerLabel = new JLabel("YOU WON");
        winnerLabel.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",50));
        winnerLabel.setVisible(false);
        winnerLabel.setForeground(Color.YELLOW);
        gamePanel.add(winnerLabel);
        gamePanel.setComponentZOrder(winnerLabel, 0);
        
        player0Banner = new Banner();
        player0Banner.setLocation(0, 0);
        player0Banner.setVisible(false);
        gamePanel.add(player0Banner);
        gamePanel.setComponentZOrder(player0Banner, 0);
        
        player1Banner = new Banner();
        player1Banner.setLocation(0, 0);
        player1Banner.setVisible(false);
        gamePanel.add(player1Banner);
        gamePanel.setComponentZOrder(player1Banner, 0);
        
        startingLabel = new OutlinedLabel("3");
        startingLabel.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",50));
        startingLabel.setForeground(Color.RED); 
        startingLabel.setVisible(false);
        gamePanel.add(startingLabel);
        gamePanel.setComponentZOrder(startingLabel, 0);

        mapNum = 0;
        resized();

        keyPressed = new boolean[4];
        for(int i = 0; i < keyPressed.length; i++){
            keyPressed[i] = false;
        }
    }
    
    public void start(){
        repaintThread = new TickThread(gui.getTick(), new Runnable(){
            @Override
            public void run(){
                repaint();
            }
        });
        repaintThread.start();
        
        startingLabel.setVisible(true);
        gameClosingLabel.setVisible(false);
        winnerLabel.setVisible(false);
 
        for(int i = 0; i < player.length; i++){
            // player[i].setTexture("King.png");
            player[i].setHealth(100);
            // if(! player[i].rightSided) player[i].turnImage();
            
            // healthbar[i].setForeground(Color.RED);
            // healthbar[i].setVisible(false);
        }
        
        ArrayList<Integer> temp = new ArrayList<Integer>();
        for(int i = 0; i < bulletList.size(); i++){
            temp.add(new Integer(bulletList.get(i).getID()));
        }
        for(int i = 0; i < temp.size(); i++){
            removeBullet(temp.get(i));
        }
        
    }

    public void finish(){
    }
    
    @Override
    public void resized(){
        int panelSize = Math.min(gui.getFrame().getWidth()/16, gui.getFrame().getHeight()/ 9);
        gamePanel.setSize(panelSize * 16, panelSize* 9);
        gamePanel.setLocation((gui.getFrame().getWidth() - gamePanel.getWidth()) / 2, (gui.getFrame().getHeight() - gamePanel.getHeight()) / 2);

        scalePlayers();
        double scaleBullet = (gamePanel.getHeight() * 0.5) / 170.0;
        for(int i = 0; i < bulletList.size(); i++){
            try{
                bulletList.get(i).setIcon(ImageLoader.getScaledIcon("bullets/" + bulletList.get(i).getTexture(), scaleBullet, scaleBullet));
            }catch (Exception e){
                e.printStackTrace();
            }
            bulletList.get(i).setSize(bulletList.get(i).getPreferredSize());
            bulletList.get(i).imageTurning();
        }

        try{
            String backgroundImgPath = "maps/background.png";
            double backgroundWidth = getImageIcon(backgroundImgPath).getIconWidth();
            double backgroundHeight = getImageIcon(backgroundImgPath).getIconHeight();
            double backgroundScale = (backgroundWidth / 16 < backgroundHeight / 9) ? gamePanel.getWidth() / backgroundWidth :  gamePanel.getHeight() / backgroundHeight;
            backgroundLabel.setIcon(ImageLoader.getScaledIcon(backgroundImgPath, backgroundScale, backgroundScale));
            backgroundLabel.setSize(backgroundLabel.getPreferredSize());
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }

        try{
            if(mapNum != 0){
                String mapImgPath = "maps/World " + mapNum + ".png";
                double mapWidth = getImageIcon(mapImgPath).getIconWidth();
                double mapHeight = getImageIcon(mapImgPath).getIconHeight();
                double mapScale = (mapWidth / 16 < mapHeight / 9) ?   gamePanel.getHeight() / mapHeight : gamePanel.getWidth() / mapWidth;
                mapLabel.setIcon(ImageLoader.getScaledIcon(mapImgPath, mapScale, mapScale));
                mapLabel.setSize(mapLabel.getPreferredSize());
            }            
        }catch (Exception e){
            e.printStackTrace();
        } 

        if(gameClosingLabel.isVisible()) {
            gameClosingLabel.setSize((int) (gamePanel.getWidth() * 0.75), (int) (gamePanel.getWidth() * 0.5));
            FontLoader.fitFont(gameClosingLabel);
            gameClosingLabel.setSize(gameClosingLabel.getPreferredSize());
        }
        if(winnerLabel.isVisible()) {
            winnerLabel.setSize((int) (gamePanel.getWidth() * 0.5), (int) (gamePanel.getWidth() * 0.3));
            FontLoader.fitFont(winnerLabel);
            winnerLabel.setSize(gameClosingLabel.getPreferredSize());
        }
        
        if(player0Banner.isVisible()) {
            player0Banner.setSize(gamePanel.getWidth(), (int) (gamePanel.getHeight() * 0.2));
            player0Banner.resized();
        }
        
        if(player1Banner.isVisible()) {
            player1Banner.setSize(gamePanel.getWidth(), (int) (gamePanel.getHeight() * 0.2));
            player1Banner.resized();
        }
        if(startingLabel.isVisible()){
            FunctionLoader.scale(startingLabel, 0.2, 0.3);
            FontLoader.fitFont(startingLabel);
            startingLabel.setSize(startingLabel.getPreferredSize());
            FunctionLoader.position(startingLabel, 0.5, 0.5);
        }
    }
    
    public void update(){
        gamePanel.setLocation((gui.getFrame().getWidth() - gamePanel.getWidth() - 20) / 2, (gui.getFrame().getHeight() - gamePanel.getHeight() - 40) / 2);
        mapLabel.setLocation(0, 0);
        for(int i = 0; i < player.length; i++){
            setPosition(player[i]);
            setPosition(healthPanel[i]);
        }
        for(int i = 0; i < bulletList.size(); i++){
            setPosition(bulletList.get(i));
        }

        if(gameClosingLabel.isVisible()) gameClosingLabel.setLocation((gamePanel.getWidth() - gameClosingLabel.getWidth() )/ 2, (int) ((gamePanel.getHeight() - gameClosingLabel.getHeight()) * 2.0/ 5));
        if(winnerLabel.isVisible()) winnerLabel.setLocation((gamePanel.getWidth() - winnerLabel.getWidth() )/ 2, gameClosingLabel.getY() + gameClosingLabel.getHeight() + 100);
    }
    
    public void setPlayerName(int pPlayerId, String pName){
        if(pPlayerId == 0) player0Banner.setTitle(pName);;
        if(pPlayerId == 1) player1Banner.setTitle(pName);
    }
    
    private void scalePlayers(){
        for(int i = 0; i < player.length; i++){
            player[i].update();
            healthPanel[i].update();
        }
    }

    public void addBullet(Integer id, String texture, double x, double y, boolean rightSided){
        Bullet bullet = new Bullet(gamePanel, id, texture, x, y, rightSided);

        double scaleImg = (gamePanel.getHeight() * 0.5) / 170.0;
        try{
            bullet.setIcon(ImageLoader.getScaledIcon("bullets/" + texture, scaleImg, scaleImg));
        }catch (Exception e){
            e.printStackTrace();
        }
        bullet.setSize(bullet.getPreferredSize());
        bullet.imageTurning();

        gamePanel.add(bullet);
        gamePanel.setComponentZOrder(bullet, 0);
        bulletList.add(bullet);
    }

    public void updateBullet(Integer id, String texture, double x, double y){
        for(int i = 0; i < bulletList.size(); i++){
            if(bulletList.get(i).equals(id)) {
                bulletList.get(i).setXPos(x);
                bulletList.get(i).setYPos(y);
                return;
            }
        }

    }

    public void removeBullet(Integer id){
        System.out.println("removing --> " + id);
        for(int i = 0; i < bulletList.size(); i++){
            if(bulletList.get(i).equals(id)) {
                gamePanel.remove(bulletList.get(i));
                bulletList.remove(i);
                return;
            }
        }
    }

    public void setCharacter(int pNum, String character){
        resized();
    }

    public void setYourPlayer(int pNum){
        healthPanel[pNum].setColor(new Color(94, 144, 252));
        if(pNum == 0) player0Banner.switchDesign();
        else player1Banner.switchDesign();
    }
    
    public void setStarting(String pNum){
        startingLabel.setText(pNum);
        if(pNum.equals("3")){
            Sound countdownSound = new Sound("countdown.wav");
            countdownSound.playSound();

            Thread bannerLocationThread = new Thread(new Runnable(){
                @Override 
                public void run(){
                    player0Banner.setSize(gamePanel.getWidth(), (int) (gamePanel.getHeight() * 0.2));
                    player0Banner.resized();
                    player0Banner.setVisible(true);
                    player1Banner.setSize(gamePanel.getWidth(), (int) (gamePanel.getHeight() * 0.2));
                    player1Banner.resized();
                    player1Banner.setVisible(true);
                    
                    double bannerChange = 0;
                    double bannerSpeed = 0.002;
            
                    long lastLoopTime = System.nanoTime();
                    while(player0Banner.isVisible()) {
                        long currentTime = System.nanoTime();
                        long elapsedTime = currentTime - lastLoopTime;
                        lastLoopTime = currentTime;
                        
                        player0Banner.setLocation( (int) ((gamePanel.getWidth()) * (-0.1 + bannerChange)), (int) ((gamePanel.getHeight() - player0Banner.getHeight()) * 0.2));
                        player1Banner.setLocation( (int) ((gamePanel.getWidth()) * ( 0.65 - bannerChange)), (int) ((gamePanel.getHeight() - player1Banner.getHeight()) * 0.8));
                        bannerChange += bannerSpeed;
                        bannerSpeed *= 0.995;
            
                        System.out.println(bannerChange);
                        
                        // Calculate time to sleep to maintain desired tick
                        long waitTime = 1000000000 / 60;
                        long sleepTime = waitTime - elapsedTime;
            
                        try {
                            Thread.sleep(Math.max(0, sleepTime / 1000000));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            bannerLocationThread.start();
        }
        if(pNum.equals("0")){
            player0Banner.setVisible(false);
            player1Banner.setVisible(false);
            startingLabel.setText("GO!");
            Thread hideThread = new Thread(new Runnable(){
                @Override
                public void run(){
                    FunctionLoader.warte(1000);
                    startingLabel.setVisible(false);
                }
            });
            hideThread.start();
        }
        startingLabel.setVisible(true);
        FunctionLoader.scale(startingLabel, 0.2, 0.3);
        FontLoader.fitFont(startingLabel);
        startingLabel.setSize(startingLabel.getPreferredSize());
        FunctionLoader.position(startingLabel, 0.5, 0.5);
    }
    
    public void setAnimation(int pPlayerId, int pNum){
        if(pNum != player[pPlayerId].getAnimation()){
            player[pPlayerId] .setAnimation(pNum);
            scalePlayers();
        }
    }

    public void setMap(int pMapNum){
        this.mapNum = pMapNum;
    }
    
    public void setHealth(int pNum, int health){
        healthPanel[pNum].setHealth(health);
    }

    public void setRightSided(int pNum, boolean pRightSided){
        player[pNum].setRightSided(pRightSided);
    }

    public void witchPlayer(int pNum, boolean b){
        player[pNum].setWitched(b);
        player[pNum].update();
    }

    public ImageIcon getImageIcon (String path)  throws IOException {
        return  new ImageIcon(ImageIO.read(new File("assets/" + path))); 
    }

    public void setPosition(int pPlayerNum, double x, double y){
        player[pPlayerNum] .setX(x);
        player[pPlayerNum] .setY(y);
    }

    public void setPosition(Positionable o){
        int frameWidth = gamePanel.getWidth();
        int frameHeight = gamePanel.getHeight();
        o.setLocation((int) ((frameWidth - o.getScaleWidth()) * o.getXPos()), (int) ((frameHeight- o.getHeight()) * (1-o.getYPos())));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            if(keyPressed[0]) return;
            keyPressed[0] = true;
            executeKeyThread(e, 0);
        } else if (key == KeyEvent.VK_S) {
            if(keyPressed[1]) return;
            keyPressed[1] = true;
            executeKeyThread(e, 1);
        } else if (key == KeyEvent.VK_A) {
            if(keyPressed[2]) return;
            keyPressed[2] = true;
            executeKeyThread(e, 2);
        } else if (key == KeyEvent.VK_D) {
            if(keyPressed[3]) return;
            keyPressed[3] = true;
            executeKeyThread(e, 3);
        }
    }

    public void executeKeyThread(KeyEvent e, int pressedNum){
        Thread executeKeyThread = new Thread(new Runnable(){
                    public void run(){
                        while( gui.getCurrentPage().equals(gui.getGamePage()) && keyPressed[pressedNum] ){
                            executeKey(e);
                            FunctionLoader.warte(gui.getFrameWait());
                        }
                    }
                });
        executeKeyThread.start();
    }

    private void executeKey(KeyEvent e){
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            gui.getUserClient().send("USERINPUT jump");
        } else if (key == KeyEvent.VK_S) {
            gui.getUserClient().send("USERINPUT ability");
        } else if (key == KeyEvent.VK_A) {
            gui.getUserClient().send("USERINPUT left");
        } else if (key == KeyEvent.VK_D) {
            gui.getUserClient().send("USERINPUT right");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            keyPressed[0] = false;
        } else if (key == KeyEvent.VK_S) {
            keyPressed[1] = false;
            System.out.println("Break s");
        } else if (key == KeyEvent.VK_A) {
            keyPressed[2] = false;
        } else if (key == KeyEvent.VK_D) {
            keyPressed[3] = false;
        }
    }

    public void closingGame(String time){
        gameClosingLabel.setText("GAME IS CLOSING IN " + time + "s");
        gameClosingLabel.setSize((int) (gamePanel.getWidth() * 0.75), (int) (gamePanel.getWidth() * 0.5));
        FontLoader.fitFont(gameClosingLabel);
        gameClosingLabel.setSize(gameClosingLabel.getPreferredSize());
        if(! gameClosingLabel.isVisible()){
            resized();
            gameClosingLabel.setVisible(true);
        }

    }

    public void gameWon(boolean b){
        System.out.println("---> " + b);
        Thread warte = new Thread(new Runnable(){
                    public void run(){
                        FunctionLoader.warte(10);
                        if(b){
                            winnerLabel.setText("YOU WON!");
                            winnerLabel.setForeground(Color.YELLOW);
                        }
                        else {
                            winnerLabel.setText("YOU LOST!");
                            winnerLabel.setForeground(Color.RED);
                        }
                        winnerLabel.setVisible(true);
                    }
                });
        warte.start();
    }
}
