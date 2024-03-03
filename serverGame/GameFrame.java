package serverGame;

import assetLoader.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.border.Border;

public class GameFrame implements KeyListener{
    private MessageInterpreter messageInterpreter;
    JFrame testFrame;
    JPanel gamePanel;
    private JLabel backgroundLabel;
    private String backgroundImgPath;
    private JLabel mapLabel;
    private String mapImgPath;
    Player player1;
    Player player2;
    Healthbar p1Health;
    Healthbar p2Health;
    ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
    private int bulletId;
    private boolean[] keyPressed;
    private int mapNum;
    BufferedImage bufferedMap;
    private final int frameRate = (int) (1000 / 60.0);
    TickThread gameThread;
    public GameFrame(MessageInterpreter mI){
        messageInterpreter = mI;
        
        // System.out.println("Program is running...");
        testFrame = new JFrame();
        testFrame.setLayout(null);
        testFrame.setTitle("[SERVER] Simple Platformer");
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testFrame.setPreferredSize(new Dimension(710, 400));
        testFrame.setLocationRelativeTo(null);
        testFrame.setVisible(true);
        testFrame.pack();

        keyPressed = new boolean[8];
        for(int i = 0; i < keyPressed.length; i++){
            keyPressed[i] = false;
        }

        testFrame.addKeyListener(this);
        testFrame.setFocusable(true);
        testFrame.setFocusTraversalKeysEnabled(true);

        gamePanel = new JPanel();
        gamePanel.setLayout(null);
        gamePanel.setBackground(Color.BLUE);
        gamePanel.setSize(710, 400);
        testFrame.add(gamePanel);

        this.player1 = null;

        this.p1Health= null;

        this.player2 = null;

        p2Health= null;
        
        bulletId = 0;

        int mapNum = (int) (Math.random() * 3) + 1;
        mapLabel = new JLabel(); 
        mapImgPath = "maps/World " + mapNum + ".png";
        gamePanel.add(mapLabel);
        mI.interpretMessage("MAP " + mapNum);

        backgroundLabel = new JLabel();
        backgroundImgPath = "maps/background.png";
        gamePanel.add(backgroundLabel);

        testFrame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    if(!isInitialized()) return;
                    scaleComponents();
                }
            });

        // setPlayer(0, "King");
        // setPlayer(1, "King");
    }
    
    public void show(){
        testFrame.setVisible(true);
    }
    
    public void start(){
        if(! isInitialized()) return;
        gameThread = new TickThread(60, new Runnable(){
                    @Override 
                    public void run(){
                        updateGame();
                        testFrame.repaint();
                    }
                });
        gameThread.start();
    }
    public void dispose(){
        testFrame.dispose();
    }

    public boolean isInitialized(){
        if(player1 == null || player2 == null) return false;
        return true;
    }

    public void setPlayer(int id, String character){
        if(id == 0) {
            player1 = new Player(this, character, 0);
            gamePanel.add(player1);
            gamePanel.setComponentZOrder(player1, 0);

            p1Health = new Healthbar(gamePanel, player1);
            p1Health.setForeground(Color.BLUE);
            gamePanel.add(p1Health);
            gamePanel.setComponentZOrder(p1Health, 0);
        }
        else if (id == 1) {
            player2 = new Player(this, character, 1);
            gamePanel.add(player2);
            gamePanel.setComponentZOrder(player2, 0);

            p2Health = new Healthbar(gamePanel, player2);
            p2Health.setForeground(Color.RED);
            gamePanel.add(p2Health);
            gamePanel.setComponentZOrder(p2Health, 0);
        }
        if(isInitialized()) {
            scaleComponents();
            updateGame();
            testFrame.repaint();
        }
    }

    public void updateGame(){
        int frameWidth = gamePanel.getWidth();
        int frameHeight = gamePanel.getHeight();

        mapLabel.setLocation(0, 0);

        player1.update();
        player2.update(); 
        for(int i = 0; i < bulletList.size(); i++){
            bulletList.get(i).update();
        }
        // setLocation(p1Health);
        // setLocation(p2Health);
    }

    public void removeBullet(Bullet bullet){
        bulletList.remove(bullet);
        Thread waitThread = new Thread(new Runnable(){
                    public void run(){
                        if(!bullet.checkSides() && bullet.getPlayerInfo().getName() != "Boomerang Thrower") warte(500);
                        messageInterpreter.interpretMessage("BULLET REMOVE " + bullet.getID());
                        gamePanel.remove(bullet);
                    }
                });
        // if(bullet.getPlayer().getPlayerInfo().getName() == "Teleporter"){
        waitThread.start();
        // return;
        // }
        // gamePanel.remove(bullet);
    }

    public void addBullet(Player player, int direction) {
        SwingUtilities.invokeLater(() -> {
                    Bullet bullet = new Bullet(bulletId, this, player);
                    bullet.setSize(bullet.getPreferredSize());
                    bullet.setDirection(direction);
                    bulletList.add(bullet);
                    gamePanel.add(bullet);
                    gamePanel.setComponentZOrder(bullet, 0);
                    bulletId++;
            });
    }

    private void scaleComponents(){
        int panelSize = Math.min(testFrame.getWidth()/16, testFrame.getHeight()/ 9);
        gamePanel.setSize(panelSize * 16, panelSize* 9);
        gamePanel.setLocation((testFrame.getWidth() - gamePanel.getWidth() - 20) / 2, (testFrame.getHeight() - gamePanel.getHeight() - 40) / 2);

        try{
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
            double mapWidth = getImageIcon(mapImgPath).getIconWidth();
            double mapHeight = getImageIcon(mapImgPath).getIconHeight();
            double mapScale = (mapWidth / 16 < mapHeight / 9) ?   gamePanel.getHeight() / mapHeight : gamePanel.getWidth() / mapWidth;
            mapLabel.setIcon(getScaledIcon(mapImgPath, mapScale, mapScale));
            mapLabel.setSize(mapLabel.getPreferredSize());
        }catch (Exception e){
            e.printStackTrace();
        }
        bufferedMap = toBufferedImage(mapLabel.getIcon());

        player1.updateImage();
        player2.updateImage();
        p1Health.scale();
        p2Health.scale();
        for(int i = 0; i < bulletList.size(); i++){
            bulletList.get(i).scaleImage();
        }
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
        } else if (key == KeyEvent.VK_UP) {
            if(keyPressed[4]) return;
            keyPressed[4] = true;
            executeKeyThread(e, 4);
        } else if (key == KeyEvent.VK_DOWN) {
            if(keyPressed[5]) return;
            keyPressed[5] = true;
            executeKeyThread(e, 5);
        } else if (key == KeyEvent.VK_LEFT) {
            if(keyPressed[6]) return;
            keyPressed[6] = true;
            executeKeyThread(e, 6);
        } else if (key == KeyEvent.VK_RIGHT) {
            if(keyPressed[7]) return;
            keyPressed[7] = true;
            executeKeyThread(e, 7);
        }
    }

    public void executeKeyThread(KeyEvent e, int pressedNum){
        Thread executeKeyThread = new Thread(new Runnable(){
                    public void run(){
                        while(keyPressed[pressedNum] ){
                            executeKey(e);
                            warte(frameRate);
                        }
                    }
                });
        executeKeyThread.start();
    }

    private void executeKey(KeyEvent e){
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            player1.getInput("jump");
        } else if (key == KeyEvent.VK_S) {
            player1.getInput("ability");
        } else if (key == KeyEvent.VK_A) {
            player1.getInput("left");
        } else if (key == KeyEvent.VK_D) {
            player1.getInput("right");
        } else if (key == KeyEvent.VK_UP) {
            player2.getInput("jump");
        } else if (key == KeyEvent.VK_DOWN) {
            player2.getInput("ability");
        } else if (key == KeyEvent.VK_LEFT) {
            player2.getInput("left");
        } else if (key == KeyEvent.VK_RIGHT) {
            player2.getInput("right");
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
        } else if (key == KeyEvent.VK_A) {
            keyPressed[2] = false;
        } else if (key == KeyEvent.VK_D) {
            keyPressed[3] = false;
        } else if (key == KeyEvent.VK_UP) {
            keyPressed[4] = false;
        } else if (key == KeyEvent.VK_DOWN) {
            keyPressed[5] = false;
        } else if (key == KeyEvent.VK_LEFT) {
            keyPressed[6] = false;
        } else if (key == KeyEvent.VK_RIGHT) {
            keyPressed[7] = false;
        }
    }

    public void getUserInput(int player, String input){
        if(player == 0 && player1 != null) player1.getInput(input);
        if(player == 1 && player1 != null) player2.getInput(input);
        
        // int key = -1;
        // switch(input){
            // case "jump" -> key = 0;
            // case "ability" -> key = 1;
            // case "left" -> key = 2;
            // case "right" -> key = 3;
        // }
        
        // if(player == 0) key = -1;
        // else if(player == 1) key += 4;
        
        // if(key == -1) return;
        
        // if(keyPressed[key]) return;
        // keyPressed[key] = true;
        // executeKeyThread(e, key);
    }

    public Player getPlayer(int player){
        if(player == 1) return player1;
        if(player == 2) return player2;
        return null;
    }

    public void setLocation(Positionable o){
        int frameWidth = gamePanel.getWidth();
        int frameHeight = gamePanel.getHeight();
        o.setLocation((int) ((frameWidth - o.getScaleWidth()) * o.getXPos()), (int) ((frameHeight- o.getHeight()) * (1-o.getYPos())));
    }

    public JPanel getGamePanel(){
        return gamePanel;
    }

    // Hilfsmethode, um zu überprüfen, ob ein Pixel transparent ist
    private boolean isTransparent(BufferedImage image, int x, int y) {
        try{
            int pixel = image.getRGB(x, y);
            return (pixel >> 24) == 0x00;
        }catch (Exception e){
            return true;
        }
    }

    public boolean checkTop(JLabel label){
        for(double i = 0; i < label.getWidth(); i+= label.getWidth() * 0.2){
            if(!isTransparent(bufferedMap, label.getX() + (int) (i), label.getY() - getMapYShift())) return true;
        }
        return false;
    }

    public boolean checkBottom(JLabel label){
        for(double i = 0; i < label.getWidth(); i+= label.getWidth() * 0.2){
            if(!isTransparent(bufferedMap, label.getX() + (int) (i), label.getY() + label.getHeight() - getMapYShift())) return true;
        }
        return false;
    }

    public boolean checkLeft(JLabel label){
        if(label.getHeight() == 0) return false;

        for(float i = 0; i <  (label.getHeight() * 0.9); i+= label.getHeight() * 0.1){
            if(!isTransparent(bufferedMap, label.getX(), label.getY() + (int) (i) - getMapYShift())) return true;
        }
        return false;
    }

    public boolean checkRight(JLabel label){
        for(float i = 0; i <  (label.getHeight() *0.9); i+= label.getHeight() * 0.1){
            if(!isTransparent(bufferedMap, label.getX() + label.getWidth(), label.getY() + (int) (i) - getMapYShift())) return true;
        }
        return false;
    }

    public BufferedImage toBufferedImage(Icon icon) {
        if (icon == null) {
            return null;
        }

        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics(); 
        icon.paintIcon(null, g, 0, 0);
        g.dispose();

        return image;
    }

    public Player getPlayer1(){
        return player1;
    }

    public Player getPlayer2(){
        return player2;
    }

    public boolean isMoving(){
        return player1.isMoving() || player2.isMoving();
    }

    private void warte(int time){
        try{
            Thread.sleep(time);
        }
        catch (InterruptedException ie){
            ie.printStackTrace();
        }
    }

    public int getMapYShift(){
        return mapLabel.getY();
    }

    public ImageIcon getScaledIcon(String path, double x_scale, double y_scale) throws IOException {
        ImageIcon imageIcon = new ImageIcon(ImageIO.read(new File("assets/" + path))); 
        Image image = imageIcon.getImage(); 
        Image newimg = image.getScaledInstance((int)(imageIcon.getIconWidth() * x_scale), (int) (imageIcon.getIconHeight()*y_scale),  java.awt.Image.SCALE_AREA_AVERAGING); 
        imageIcon = new ImageIcon(newimg);  
        return imageIcon;
    }

    public ImageIcon getImageIcon (String path)  throws IOException {
        return  new ImageIcon(ImageIO.read(new File("assets/" + path))); 
    }

    public boolean touching(JLabel label1, JLabel label2) {
        Rectangle bounds1 = label1.getBounds();
        Rectangle bounds2 = label2.getBounds();

        // Check if the rectangles intersect or are adjacent
        return bounds1.intersects(bounds2) || bounds1.contains(bounds2) || bounds2.contains(bounds1);
    }
    
    public MessageInterpreter getMessageInterpreter(){
        return messageInterpreter;
    }
}
