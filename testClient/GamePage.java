package testClient;

import game.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class GamePage extends Page implements KeyListener{
    private class Player extends JLabel implements Healthy{ 
        ImageRendering ir;
        double x; 
        double y;
        int health;
        String texture;
        boolean witched;
        boolean rightSided;
        Player(){
            ir = new ImageRendering();
            x = 0;
            y = 0;
            health = 100;
            texture = "";
            witched = false;
            rightSided = true;

            Thread test = new Thread (new Runnable() {
                        public void run(){
                            // System.out.println("mark0");
                            // System.out.println(gui.getUserClient() == null);
                            // warteSolange(gui.getUserClient() == null);
                            // System.out.println("mark1");
                            while(true){
                                if(gui.getUserClient() != null && gui.getCurrentPage() == gui.getGamePage()){
                                    gui.getUserClient().send("USERINPUT jump");
                                }
                                gui.warte(500);
                            }
                        }
                    });
            // test.start();
        }

        public void turnImage(){
            ImageIcon flippedIcon = ir.flipIcon(this.getIcon(), true, false);
            this.setIcon(flippedIcon);
            rightSided = rightSided ? false : true;
        }

        public void turn(boolean b){
            if(rightSided != b) turnImage();
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

        public void setHealth(int a){
            health = a;
        }

        public int getHealth(){
            return health;
        }

        public void setWitched(boolean b){
            witched = b;
        }

        public boolean isWitched(){
            return witched;
        }

        public void setTexture(String str){
            texture = str;
        }

        public String getTexture(){
            return texture;
        }

        public int getScaleWidth(){
            return getWidth();
        }

        public void setRightSided(boolean b){
            rightSided = b;
        }
    }
    private class Bullet extends JLabel implements Positionable{
        final int ID;
        final boolean rightSided;
        String texture;
        double x; 
        double y;
        Bullet(int id, String texture, double x, double y, boolean rightSided){
            this.ID = id;
            this.x = x;
            this.y = y;
            this.texture = texture;
            double scaleImg = (gamePanel.getHeight() * 0.125) / 170.0;
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
            ImageRendering ir = new ImageRendering();
            ImageIcon flippedIcon = ir.flipIcon(this.getIcon(), true, false);
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
    private GUI gui;
    private JPanel gamePanel;
    private Player[] player;
    private Healthbar[] healthbar;
    private JLabel backgroundLabel;
    private JLabel mapLabel;
    private boolean[] keyPressed;
    private ArrayList<Bullet> bulletList;
    private JLabel gameClosingLabel;
    private JLabel winnerLabel;

    GamePage(GUI gui){
        this.gui = gui;
        setLayout(null);

        gamePanel = new JPanel();
        gamePanel.setLayout(null);
        gamePanel.setBackground(Color.BLUE);
        gamePanel.setSize(710, 400);
        add(gamePanel);

        player = new Player[2];
        healthbar = new Healthbar[player.length];
        for(int i = 0; i < player.length; i++){
            player[i] = new Player();
            player[i].setTexture("King.png");
            gamePanel.add(player[i]);

            healthbar[i] = new Healthbar(gamePanel, player[i]);
            healthbar[i].setForeground(Color.RED);
            healthbar[i].scale();
            gamePanel.add (healthbar[i]);
        }

        mapLabel = new JLabel(); 
        gamePanel.add(mapLabel);

        backgroundLabel = new JLabel();
        backgroundLabel.setLocation(0, 0);
        gamePanel.add(backgroundLabel);

        bulletList = new ArrayList<Bullet>();

        gameClosingLabel = new JLabel("GAME IS CLOSING...");
        gameClosingLabel.setFont(loadFont("assets/LilitaOne-Regular.ttf",50));
        gameClosingLabel.setVisible(false);
        gameClosingLabel.setForeground(Color.RED);
        gamePanel.add(gameClosingLabel);
        gamePanel.setComponentZOrder(gameClosingLabel, 0);

        winnerLabel = new JLabel("YOU WON");
        winnerLabel.setFont(loadFont("assets/LilitaOne-Regular.ttf",50));
        winnerLabel.setVisible(false);
        winnerLabel.setForeground(Color.YELLOW);
        gamePanel.add(winnerLabel);
        gamePanel.setComponentZOrder(winnerLabel, 0);

        componentResized();

        keyPressed = new boolean[4];
        for(int i = 0; i < keyPressed.length; i++){
            keyPressed[i] = false;
        }
    }

    public void addBullet(Integer id, String texture, double x, double y, boolean rightSided){
        Bullet bullet = new Bullet(id, texture, x, y, rightSided);

        double scaleImg = (gamePanel.getHeight() * 0.5) / 170.0;
        try{
            bullet.setIcon(getScaledIcon("bullets/" + texture, scaleImg, scaleImg));
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
        player[pNum].setTexture(character + ".png");
        componentResized();
    }

    public void setYourPlayer(int pNum){
        healthbar[pNum].setForeground(Color.BLUE);
    }

    public String getDescription(){
        return "gamePage";
    }

    public void positionElements(){
        gamePanel.setLocation((gui.getFrame().getWidth() - gamePanel.getWidth() - 20) / 2, (gui.getFrame().getHeight() - gamePanel.getHeight() - 40) / 2);
        mapLabel.setLocation(0, gamePanel.getHeight() / 5);
        for(int i = 0; i < player.length; i++){
            setPosition(player[i]);
            setPosition(healthbar[i]);
        }
        for(int i = 0; i < bulletList.size(); i++){
            setPosition(bulletList.get(i));
        }

        if(gameClosingLabel.isVisible()) gameClosingLabel.setLocation((gamePanel.getWidth() - gameClosingLabel.getWidth() )/ 2, (int) ((gamePanel.getHeight() - gameClosingLabel.getHeight()) * 2.0/ 5));
        if(winnerLabel.isVisible()) winnerLabel.setLocation((gamePanel.getWidth() - winnerLabel.getWidth() )/ 2, gameClosingLabel.getY() + gameClosingLabel.getHeight() + 100);
    }

    public void setHealth(int pNum, int health){
        player[pNum].setHealth(health);
    }

    public void setRightSided(int pNum, boolean rightSided){
        player[pNum].turn(rightSided);
    }

    public void resizeElements(){
    }

    public void reloadData(){
        gameClosingLabel.setVisible(false);
        winnerLabel.setVisible(false);
 
        for(int i = 0; i < player.length; i++){
            player[i].setTexture("King.png");
            player[i].setHealth(100);
            if(! player[i].rightSided) player[i].turnImage();
            
            healthbar[i].setForeground(Color.RED);
        }
        
        ArrayList<Integer> temp = new ArrayList<Integer>();
        for(int i = 0; i < bulletList.size(); i++){
            temp.add(new Integer(bulletList.get(i).getID()));
        }
        for(int i = 0; i < temp.size(); i++){
            removeBullet(temp.get(i));
        }
    }

    public void componentResized(){
        int panelSize = Math.min(gui.getFrame().getWidth()/16, gui.getFrame().getHeight()/ 9);
        gamePanel.setSize(panelSize * 16, panelSize* 9);
        gamePanel.setLocation((gui.getFrame().getWidth() - gamePanel.getWidth() - 20) / 2, (gui.getFrame().getHeight() - gamePanel.getHeight() - 40) / 2);

        double scaleImg = (gamePanel.getHeight() * 0.125) / 170.0;
        for(int i = 0; i < player.length; i++){
            try{
                if(player[i].isWitched()) player[i].setIcon(getScaledIcon("player/Frog.png", scaleImg, scaleImg));
                else player[i].setIcon(getScaledIcon("player/" + player[i].getTexture(), scaleImg, scaleImg));
            }catch (Exception e){
                e.printStackTrace();
            }
            if(! player[i].rightSided) player[i].turnImage();
            player[i].setSize(player[i].getPreferredSize());
            healthbar[i].scale();
        }
        double scaleBullet = (gamePanel.getHeight() * 0.5) / 170.0;
        for(int i = 0; i < bulletList.size(); i++){
            try{
                bulletList.get(i).setIcon(getScaledIcon("bullets/" + bulletList.get(i).getTexture(), scaleBullet, scaleBullet));
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
            backgroundLabel.setIcon(getScaledIcon(backgroundImgPath, backgroundScale, backgroundScale));
            backgroundLabel.setSize(backgroundLabel.getPreferredSize());
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }

        try{
            String mapImgPath = "maps/World 1.png";
            double mapWidth = getImageIcon(mapImgPath).getIconWidth();
            double mapHeight = getImageIcon(mapImgPath).getIconHeight();
            double mapScale = (mapWidth / 16 < mapHeight / 9) ?   gamePanel.getHeight() / mapHeight : gamePanel.getWidth() / mapWidth;
            mapLabel.setIcon(getScaledIcon(mapImgPath, mapScale, mapScale));
            mapLabel.setSize(mapLabel.getPreferredSize());
        }catch (Exception e){
            e.printStackTrace();
        } 

        if(gameClosingLabel.isVisible()) {
            gameClosingLabel.setSize((int) (gamePanel.getWidth() * 0.75), (int) (gamePanel.getWidth() * 0.5));
            scaleLabel(gameClosingLabel);
            gameClosingLabel.setSize(gameClosingLabel.getPreferredSize());
        }
        if(winnerLabel.isVisible()) {
            winnerLabel.setSize((int) (gamePanel.getWidth() * 0.5), (int) (gamePanel.getWidth() * 0.3));
            scaleLabel(winnerLabel);
            winnerLabel.setSize(gameClosingLabel.getPreferredSize());
        }

    }

    public void witchPlayer(int pNum, boolean b){
        player[pNum].setWitched(b);

        double scaleImg = (gamePanel.getHeight() * 0.125) / 170.0;
        try{
            if(player[pNum].isWitched()) player[pNum].setIcon(getScaledIcon("player/Frog.png", scaleImg, scaleImg));
            else player[pNum].setIcon(getScaledIcon("player/" + player[pNum].getTexture(), scaleImg, scaleImg));
        }catch (Exception e){
            e.printStackTrace();
        }
        player[pNum].setSize(player[pNum].getPreferredSize());
        healthbar[pNum].scale();
    }

    public ImageIcon getImageIcon (String path)  throws IOException {
        return  new ImageIcon(ImageIO.read(new File("assets/" + path))); 
    }

    public void setPosition(int pNum, double x, double y){
        player[pNum] .setX(x);
        player[pNum] .setY(y);
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
                        while(running() && keyPressed[pressedNum] ){
                            executeKey(e);
                            warte(gui.getFrameWait());
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
    public void keyTyped(KeyEvent e) {
        // Not needed for this implementation
    }

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

    public boolean running(){
        return gui.getCurrentPage() == this; 
    }

    public void closingGame(String time){
        gameClosingLabel.setText("GAME IS CLOSING IN " + time + "s");
        gameClosingLabel.setSize((int) (gamePanel.getWidth() * 0.75), (int) (gamePanel.getWidth() * 0.5));
        scaleLabel(gameClosingLabel);
        gameClosingLabel.setSize(gameClosingLabel.getPreferredSize());
        if(! gameClosingLabel.isVisible()){
            componentResized();
            gameClosingLabel.setVisible(true);
        }

    }

    public void gameWon(boolean b){
        System.out.println("---> " + b);
        Thread warte = new Thread(new Runnable(){
                    public void run(){
                        warte(10);
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

    private void scaleLabel(JLabel label) {
        Font labelFont = label.getFont();
        String labelText = label.getText();

        // Berechne die Breite des Textes im JLabel
        int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);

        // Erhalte die aktuelle Breite und Höhe des JLabels
        int componentWidth = label.getWidth();
        int componentHeight = label.getHeight();

        // Berechne die Skalierungsfaktoren für Breite und Höhe
        double widthRatio = (double) componentWidth / (double) stringWidth;
        int newFontSize = (int) (labelFont.getSize() * widthRatio);

        // Setze die neue Schriftgröße, sodass der Text passt
        int fontSizeToUse = Math.min(newFontSize, componentHeight); // verhindere zu große Schrift
        label.setFont(new Font(labelFont.getName(), labelFont.getStyle(), fontSizeToUse));
    }
}
