// package game;

// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.*;
// import java.io.*;
// import javax.imageio.ImageIO;
// import java.awt.image.BufferedImage;
// import java.util.ArrayList;
// import javax.swing.border.Border;

// public class GamePanelTest implements KeyListener{
    
    // JFrame testFrame;
    // JPanel gamePanel;
    // private JLabel backgroundLabel;
    // private String backgroundImgPath;
    // private JLabel mapLabel;
    // private String mapImgPath;
    // Player player1;
    // Player player2;
    // Healthbar p1Health;
    // Healthbar p2Health;
    // ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
    // private boolean[] keyPressed;
    // private int mapNum;
    // BufferedImage bufferedMap;
    // private final int frameRate = (int) (1000 / 60.0);
    // public GamePanelTest(){
        // System.out.println("Program is running...");
        // testFrame = new JFrame();
        // testFrame.setLayout(null);
        // testFrame.setTitle("[GAME TEST] Simple Platformer");
        // testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // testFrame.setSize(710, 400);
        // testFrame.setLocationRelativeTo(null);
        // testFrame.setVisible(true);
        // // testFrame.pack();

        // keyPressed = new boolean[8];
        // for(int i = 0; i < keyPressed.length; i++){
            // keyPressed[i] = false;
        // }

        // testFrame.addKeyListener(this);
        // testFrame.setFocusable(true);
        // testFrame.setFocusTraversalKeysEnabled(true);

        // gamePanel = new JPanel();
        // gamePanel.setLayout(null);
        // gamePanel.setBackground(Color.BLUE);
        // gamePanel.setSize(710, 400);
        // testFrame.add(gamePanel);
        
        // this.player1 = new Player(this, "Granate Thrower", 1);
        // player1.setSize(50, 50);
        // player1.setLocation(0,0);
        // player1.setBackground(Color.BLUE);
        // gamePanel.add(player1);
        
        // p1Health= new Healthbar(this, player1);
        // p1Health.setSize(50, 10);
        // p1Health.setLocation(0, 0);
        // p1Health.setForeground(Color.BLUE);
        // gamePanel.add(p1Health);

        // this.player2 = new Player(this, "Snowman", 2);
        // player2.setSize(50, 50);
        // player2.setLocation(0,0);
        // player2.setVisible(true);
        // gamePanel.add(player2);
        
        // p2Health= new Healthbar(this, player2);
        // p2Health.setSize(50, 10);
        // p2Health.setLocation(0, 0);
        // p2Health.setForeground(Color.RED);
        // gamePanel.add(p2Health);

        // // test = new Bullet(this, player1);
        // // test.setSize(test.getPreferredSize());
        // // gamePanel.add(test);
        // // addBullet();
        
        // mapLabel = new JLabel(); 
        // mapImgPath = "maps/World 1.png";
        // gamePanel.add(mapLabel);
        
        // backgroundLabel = new JLabel();
        // backgroundImgPath = "maps/background.png";
        // gamePanel.add(backgroundLabel);

        // scaleComponents();
        
        // testFrame.addComponentListener(new ComponentAdapter() {
                // @Override
                // public void componentResized(ComponentEvent e) {
                    // scaleComponents();
                // }
            // });
        // Thread gameThread = new Thread(new Runnable(){
                    // @Override 
                    // public void run(){
                        // while(true){
                            // updateGame();
                            // testFrame.repaint();
                            // try{
                                // Thread.sleep(frameRate);
                            // }catch (Exception e){

                            // }

                        // }
                    // }
                // });
        // gameThread.start();
    // }

    // public void updateGame(){
        // int frameWidth = gamePanel.getWidth();
        // int frameHeight = gamePanel.getHeight();

        // mapLabel.setLocation(0, gamePanel.getHeight() / 5);

        // player1.update();
        // player2.update();
        // for(int i = 0; i < bulletList.size(); i++){
            // bulletList.get(i).update();
        // }
        // setLocation(p1Health);
        // setLocation(p2Health);
    // }
    
    // public void removeBullet(Bullet bullet){
        // System.out.println("Removed bullet :)");
        // bulletList.remove(bullet);
        // Thread waitThread = new Thread(new Runnable(){
            // public void run(){
                // if(!bullet.checkSides() && bullet.getPlayerInfo().getName() != "Boomerang Thrower") warte(500);
                // gamePanel.remove(bullet);
            // }
        // });
        // // if(bullet.getPlayer().getPlayerInfo().getName() == "Teleporter"){
        // waitThread.start();
        // // return;
        // // }
        // // gamePanel.remove(bullet);
    // }
    // public void addBullet(Player player, int direction) {
        // SwingUtilities.invokeLater(() -> {
            // Bullet bullet = new Bullet(this, player);
            // bullet.setSize(bullet.getPreferredSize());
            // bullet.setDirection(direction);
            // bulletList.add(bullet);
            // gamePanel.add(bullet);
            // gamePanel.setComponentZOrder(bullet, 0);
        // });
    // }

    // private void scaleComponents(){
        // int panelSize = Math.min(testFrame.getWidth()/16, testFrame.getHeight()/ 9);
        // gamePanel.setSize(panelSize * 16, panelSize* 9);
        // gamePanel.setLocation((testFrame.getWidth() - gamePanel.getWidth() - 20) / 2, (testFrame.getHeight() - gamePanel.getHeight() - 40) / 2);

        // try{
            // double backgroundWidth = getImageIcon(backgroundImgPath).getIconWidth();
            // double backgroundHeight = getImageIcon(backgroundImgPath).getIconHeight();
            // double backgroundScale = (backgroundWidth / 16 < backgroundHeight / 9) ? gamePanel.getWidth() / backgroundWidth :  gamePanel.getHeight() / backgroundHeight;
            // backgroundLabel.setIcon(getScaledIcon(backgroundImgPath, backgroundScale, backgroundScale));
            // backgroundLabel.setSize(backgroundLabel.getPreferredSize());
        // }
        // catch (IOException ioe){
            // ioe.printStackTrace();
        // }

        // try{
            // double mapWidth = getImageIcon(mapImgPath).getIconWidth();
            // double mapHeight = getImageIcon(mapImgPath).getIconHeight();
            // double mapScale = (mapWidth / 16 < mapHeight / 9) ?   gamePanel.getHeight() / mapHeight : gamePanel.getWidth() / mapWidth;
            // mapLabel.setIcon(getScaledIcon(mapImgPath, mapScale, mapScale));
            // mapLabel.setSize(mapLabel.getPreferredSize());
        // }catch (Exception e){
            // e.printStackTrace();
        // }
        // bufferedMap = toBufferedImage(mapLabel.getIcon());

        // player1.scaleImage();
        // player2.scaleImage();
        // p1Health.scale();
        // p2Health.scale();
        // for(int i = 0; i < bulletList.size(); i++){
            // bulletList.get(i).scaleImage();
        // }
    // }

    // @Override
    // public void keyPressed(KeyEvent e) {
        // int key = e.getKeyCode();
        // if (key == KeyEvent.VK_W) {
            // if(keyPressed[0]) return;
            // keyPressed[0] = true;
            // executeKeyThread(e, 0);
        // } else if (key == KeyEvent.VK_S) {
            // if(keyPressed[1]) return;
            // keyPressed[1] = true;
            // executeKeyThread(e, 1);
        // } else if (key == KeyEvent.VK_A) {
            // if(keyPressed[2]) return;
            // keyPressed[2] = true;
            // executeKeyThread(e, 2);
        // } else if (key == KeyEvent.VK_D) {
            // if(keyPressed[3]) return;
            // keyPressed[3] = true;
            // executeKeyThread(e, 3);
        // } else if (key == KeyEvent.VK_UP) {
            // if(keyPressed[4]) return;
            // keyPressed[4] = true;
            // executeKeyThread(e, 4);
        // } else if (key == KeyEvent.VK_DOWN) {
            // if(keyPressed[5]) return;
            // keyPressed[5] = true;
            // executeKeyThread(e, 5);
        // } else if (key == KeyEvent.VK_LEFT) {
            // if(keyPressed[6]) return;
            // keyPressed[6] = true;
            // executeKeyThread(e, 6);
        // } else if (key == KeyEvent.VK_RIGHT) {
            // if(keyPressed[7]) return;
            // keyPressed[7] = true;
            // executeKeyThread(e, 7);
        // }
    // }

    // public void executeKeyThread(KeyEvent e, int pressedNum){
        // Thread executeKeyThread = new Thread(new Runnable(){
                    // public void run(){
                        // while(keyPressed[pressedNum] ){
                            // executeKey(e);
                            // warte(frameRate);
                        // }
                    // }
                // });
        // executeKeyThread.start();
    // }

    // private void executeKey(KeyEvent e){
        // int key = e.getKeyCode();
        // if (key == KeyEvent.VK_W) {
            // player1.getInput("jump");
        // } else if (key == KeyEvent.VK_S) {
            // player1.getInput("ability");
        // } else if (key == KeyEvent.VK_A) {
            // player1.getInput("left");
        // } else if (key == KeyEvent.VK_D) {
            // player1.getInput("right");
        // } else if (key == KeyEvent.VK_UP) {
            // player2.getInput("jump");
        // } else if (key == KeyEvent.VK_DOWN) {
            // player2.getInput("ability");
        // } else if (key == KeyEvent.VK_LEFT) {
            // player2.getInput("left");
        // } else if (key == KeyEvent.VK_RIGHT) {
            // player2.getInput("right");
        // }
    // }

    // @Override
    // public void keyTyped(KeyEvent e) {
        // // Not needed for this implementation
    // }

    // @Override
    // public void keyReleased(KeyEvent e) {
        // int key = e.getKeyCode();
        // if (key == KeyEvent.VK_W) {
            // keyPressed[0] = false;
        // } else if (key == KeyEvent.VK_S) {
            // keyPressed[1] = false;
            // System.out.println("Break s");
        // } else if (key == KeyEvent.VK_A) {
            // keyPressed[2] = false;
        // } else if (key == KeyEvent.VK_D) {
            // keyPressed[3] = false;
        // } else if (key == KeyEvent.VK_UP) {
            // keyPressed[4] = false;
        // } else if (key == KeyEvent.VK_DOWN) {
            // keyPressed[5] = false;
        // } else if (key == KeyEvent.VK_LEFT) {
            // keyPressed[6] = false;
        // } else if (key == KeyEvent.VK_RIGHT) {
            // keyPressed[7] = false;
        // }
    // }
    
    // public void setLocation(Positionable o){
        // int frameWidth = gamePanel.getWidth();
        // int frameHeight = gamePanel.getHeight();
        // o.setLocation((int) (frameWidth * o.getXPos()), (int) ((frameHeight- o.getHeight() - 30) * (1-o.getYPos())) + 10);
    // }
    // public JPanel getGamePanel(){
        // return gamePanel;
    // }

    // // Hilfsmethode, um zu überprüfen, ob ein Pixel transparent ist
    // private boolean isTransparent(BufferedImage image, int x, int y) {
        // try{
            // int pixel = image.getRGB(x, y);
            // return (pixel >> 24) == 0x00;
        // }catch (Exception e){
            // return true;
        // }
    // }
    // public boolean checkTop(JLabel label){
        // for(double i = 0; i < label.getWidth(); i+= label.getWidth() * 0.2){
            // if(!isTransparent(bufferedMap, label.getX() + (int) (i), label.getY() - getMapYShift())) return true;
        // }
        // return false;
    // }
    // public boolean checkBottom(JLabel label){
        // for(double i = 0; i < label.getWidth(); i+= label.getWidth() * 0.2){
            // if(!isTransparent(bufferedMap, label.getX() + (int) (i), label.getY() + label.getHeight() - getMapYShift())) return true;
        // }
        // return false;
    // }
    // public boolean checkLeft(JLabel label){
        // if(label.getHeight() == 0) return false;

        // for(float i = 0; i <  (label.getHeight() * 0.9); i+= label.getHeight() * 0.1){
            // if(!isTransparent(bufferedMap, label.getX(), label.getY() + (int) (i) - getMapYShift())) return true;
        // }
        // return false;
    // }
    // public boolean checkRight(JLabel label){
        // for(float i = 0; i <  (label.getHeight() *0.9); i+= label.getHeight() * 0.1){
            // if(!isTransparent(bufferedMap, label.getX() + label.getWidth(), label.getY() + (int) (i) - getMapYShift())) return true;
        // }
        // return false;
    // }

    // public BufferedImage toBufferedImage(Icon icon) {
        // if (icon == null) {
            // return null;
        // }

        // int width = icon.getIconWidth();
        // int height = icon.getIconHeight();

        // BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // Graphics g = image.getGraphics(); 
        // icon.paintIcon(null, g, 0, 0);
        // g.dispose();

        // return image;
    // }

    // public Player getPlayer1(){
        // return player1;
    // }
    // public Player getPlayer2(){
        // return player2;
    // }
    // public boolean isMoving(){
        // return player1.isMoving() || player2.isMoving();
    // }

    // private void warte(int time){
        // try{
            // Thread.sleep(time);
        // }
        // catch (InterruptedException ie){
            // ie.printStackTrace();
        // }
    // }
    
    // public int getMapYShift(){
        // return mapLabel.getY();
    // }

    // public ImageIcon getScaledIcon(String path, double x_scale, double y_scale) throws IOException {
        // ImageIcon imageIcon = new ImageIcon(ImageIO.read(new File("assets/" + path))); 
        // Image image = imageIcon.getImage(); 
        // Image newimg = image.getScaledInstance((int)(imageIcon.getIconWidth() * x_scale), (int) (imageIcon.getIconHeight()*y_scale),  java.awt.Image.SCALE_AREA_AVERAGING); 
        // imageIcon = new ImageIcon(newimg);  
        // return imageIcon;
    // }

    // public ImageIcon getImageIcon (String path)  throws IOException {
        // return  new ImageIcon(ImageIO.read(new File("assets/" + path))); 
    // }
    // public boolean touching(JLabel label1, JLabel label2) {
        // Rectangle bounds1 = label1.getBounds();
        // Rectangle bounds2 = label2.getBounds();

        // // Check if the rectangles intersect or are adjacent
        // return bounds1.intersects(bounds2) || bounds1.contains(bounds2) || bounds2.contains(bounds1);
    // }
    
    // public static void main(){
        // new GamePanelTest();
    // }
// }