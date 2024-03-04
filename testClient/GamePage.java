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
    private TickThread executeKeyThread;
    private JPanel gamePanel;
    private Player[] player;
    private HealthPanel[] healthPanel;
    private JLabel backgroundLabel;
    private JLabel mapLabel;
    private boolean[] keyPressed;
    private ArrayList<Bullet> bulletList;
    private OutlinedLabel gameClosingLabel;
    private OutlinedLabel winnerLabel;
    private OutlinedLabel startingLabel;
    private Banner player0Banner;
    private Banner player1Banner;
    private int mapNum;

    /**Erstellt ein neues Objekt der Klasse GamePage und initialiert dieses
     */
    public GamePage(GUI gui){
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

        gameClosingLabel = new OutlinedLabel("GAME IS CLOSING...");
        gameClosingLabel.setFont(FontLoader.loadFont("LilitaOne-Regular.ttf",50));
        gameClosingLabel.setVisible(false);
        gameClosingLabel.setForeground(Color.RED);
        gamePanel.add(gameClosingLabel);
        gamePanel.setComponentZOrder(gameClosingLabel, 0);

        winnerLabel = new OutlinedLabel("YOU WON");
        winnerLabel.setFont(FontLoader.loadFont("LilitaOne-Regular.ttf",50));
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
        startingLabel.setFont(FontLoader.loadFont("LilitaOne-Regular.ttf",50));
        startingLabel.setForeground(Color.RED); 
        startingLabel.setVisible(false);
        gamePanel.add(startingLabel);
        gamePanel.setComponentZOrder(startingLabel, 0);

        mapNum = 0;
        keyPressed = new boolean[4];
        
        resized();
    }

    @Override
    public void start(){
        startingLabel.setVisible(true);
        gameClosingLabel.setVisible(false);
        winnerLabel.setVisible(false);

        for(int i = 0; i < player.length; i++){
            player[i].setAnimation(10);
            healthPanel[i].setHealth(100);
            healthPanel[i].setColor(Color.RED);
        }

        ArrayList<Integer> temp = new ArrayList<Integer>();
        for(int i = 0; i < bulletList.size(); i++){
            temp.add(new Integer(bulletList.get(i).getID()));
        }
        for(int i = 0; i < temp.size(); i++){
            removeBullet(temp.get(i));
        }

        player0Banner.setDesign(true);
        player1Banner.setDesign(true);
        
        for(int i = 0; i < keyPressed.length; i++){
            keyPressed[i] = false;
        }
        //Wenn eine Taste (WASD) gedrückt wird, sende die jeweilige resultierende Aktion an den Server
        executeKeyThread = new TickThread(60, new Runnable(){
                    public void run(){
                        if(keyPressed[0]) gui.getUserClient().send("USERINPUT jump");
                        if(keyPressed[1]) gui.getUserClient().send("USERINPUT ability");
                        if(keyPressed[2]) gui.getUserClient().send("USERINPUT left");
                        if(keyPressed[3]) gui.getUserClient().send("USERINPUT right");
                    }
                });
        executeKeyThread.start();
    }

    @Override
    public void resized(){
        int panelSize = Math.min(gui.getFrame().getWidth()/16, gui.getFrame().getHeight()/ 9);
        gamePanel.setSize(panelSize * 16, panelSize* 9);
        gamePanel.setLocation((gui.getFrame().getWidth() - gamePanel.getWidth()) / 2, (gui.getFrame().getHeight() - gamePanel.getHeight()) / 2);

        for(int i = 0; i < healthPanel.length; i++){
            healthPanel[i].update();
        }
        
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

    @Override
    public void update(){
        repaint();
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
    
    @Override
    public void finish(){
        executeKeyThread.finish();
    }
    
    /**Aktualisiert alle Spieler
     */
    private void scalePlayers(){
        for(int i = 0; i < player.length; i++){
            player[i].update();
        }
    }

    /**Setze den Namen der Spieler, die auf den Banner angezeigt werden
     * 
     * @param pPlayerId PlayerId des Players
     * @param pName Name des Spielers
     */
    public void setPlayerName(int pPlayerId, String pName){
        if(pPlayerId == 0) player0Banner.setTitle(pName);;
        if(pPlayerId == 1) player1Banner.setTitle(pName);
    }
    
    /**Setze den Charakter eines Spielers 
     * 
     * @param pPlayer PlayerId des Spielers
     * @param pCharacter Charakter des Spielers
     */
    public void setCharacter(int pPlayerId, String pCharacter){
        player[pPlayerId].setCharacter(pCharacter);
        player[pPlayerId].update();
    }
    
    /**Setze den Player deines UserClients. Verändert die Farbe des HealthPanels
     * und des Banners
     * 
     * @param pPlayerID ID deines Spielers
     */
    public void setYourPlayer(int pPlayerID){
        healthPanel[pPlayerID].setColor(new Color(94, 144, 252));
        if(pPlayerID == 0) player0Banner.setDesign(false);
        else if(pPlayerID == 1)player1Banner.setDesign(false);
    }
    
    /**Setze die Map
     * 
     * @param pMapNum Nummer der ausgewählten Map
     */
    public void setMap(int pMapNum){
        this.mapNum = pMapNum;
    }

    /**Füge einen Schuss hinzu
     * 
     * @param pID des Schusses
     * @param pTexture Textur des Schusses
     * @param pXPos x-Position des Schusses
     * @param pYPOS y-Position des Schusses
     * @param pRightSided Richtung des Schusses (true: nach rechts, false: nach links)
     */
    public void addBullet(Integer pID, String pTexture, double pXPos, double pYPos, boolean pRightSided){
        Bullet bullet = new Bullet(gamePanel, pID, pTexture, pXPos, pYPos, pRightSided);

        double scaleImg = (gamePanel.getHeight() * 0.5) / 170.0;
        try{
            bullet.setIcon(ImageLoader.getScaledIcon("bullets/" + pTexture, scaleImg, scaleImg));
        }catch (Exception e){
            e.printStackTrace();
        }
        bullet.setSize(bullet.getPreferredSize());
        bullet.imageTurning();

        gamePanel.add(bullet);
        gamePanel.setComponentZOrder(bullet, 0);
        bulletList.add(bullet);
    }

    /**Aktualisiert die Position eines Schusses
     * 
     * @param pID ID des zu setzenden Schusses
     * @param pTexture
     * @param pXPos x-Position des Schusses
     * @param pYPos y-Position des Schusses
     */
    public void updateBullet(Integer pID, String pTexture, double pXPos, double pYPos){
        for(int i = 0; i < bulletList.size(); i++){
            if(bulletList.get(i).getID() == pID) {
                bulletList.get(i).setXPos(pXPos);
                bulletList.get(i).setYPos(pYPos);
                return;
            }
        }

    }

    /**Entfernt einen Schuss
     * 
     * @param pID ID des zu löschenden Schusses
     */
    public void removeBullet(Integer pID){
        for(int i = 0; i < bulletList.size(); i++){
            if(bulletList.get(i).getID() == pID) {
                gamePanel.remove(bulletList.get(i));
                bulletList.remove(i);
                return;
            }
        }
    }

    /**Setze die verbleibenden Sekunden des Countdowns
     * 
     * @param pNum verbleibende Sekunden bis zum Start des Spiels
     */
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
    
    /**Setze die Position eines Spielers
     * 
     * @param pPlayerNum Id des Spielers
     * @param pXPos x-Position des Spielers (0 - 1)
     * @param pYPos y-Position des Spielers (0 - 1)
     */
    public void setPosition(int pPlayerNum, double pXPos, double pYPos){
        player[pPlayerNum] .setX(pXPos);
        player[pPlayerNum] .setY(pYPos);
    }

    /**Setze die Animationsnummer eines Spielers
     * 
     * @param pPlayerId Id des Spielers
     * @param pNum Nummer der Animation
     */
    public void setAnimation(int pPlayerId, int pNum){
        if(pNum != player[pPlayerId].getAnimation()){
            player[pPlayerId] .setAnimation(pNum);
            player[pPlayerId].update();
        }
    }

    /**Setze die Leben eines Spielers
     * 
     * @param pPlayerId Id des Spielers
     * @param pHealth Leben des Spielers
     */
    public void setHealth(int pPlayerId, int pHealth){
        healthPanel[pPlayerId].setHealth(pHealth);
    }

    /**Setze Richtung eines Spielers 
     * 
     * @param pPlayerId Id des Spielers
     * @param pRughtSided Richtung des Spielers (true: rechts, false: links)
     */
    public void setRightSided(int pPlayerId, boolean pRightSided){
        player[pPlayerId].setRightSided(pRightSided);
        player[pPlayerId].update();
    }

    /**Setze den witch-Effekt eines Spielers
     * 
     * @param pPlayerId Id des Spielers
     * @param pWitched Witch-Effekt des Spielers (true: Spieler hat Effekt. false: Spieler hat Effekt nicht)
     */
    public void witchPlayer(int pPlayerId, boolean pWitched){
        player[pPlayerId].setWitched(pWitched);
        player[pPlayerId].update();
    }

    /**Positioniere ein Positionable-Objekt
     * xPos (0 - 1); je höher die x-Position ist, desto weiter rechts ist das Positionable-Objekt
     * yPos (0 - 1); je höher die y-Position ist, desto weiter oben ist das Positionable-Objekt
     */
    public void setPosition(Positionable o){
        int frameWidth = gamePanel.getWidth();
        int frameHeight = gamePanel.getHeight();
        o.setLocation((int) ((frameWidth - o.getScaleWidth()) * o.getXPos()), (int) ((frameHeight- o.getHeight()) * (1-o.getYPos())));
    }
    
    /**Setzt den Countdown zum Schließen des Spiels
     * 
     * @param pTime Zeit in Sekunden, wann das Spiel schließt
     */
    public void closingGame(String pTime){
        gameClosingLabel.setText("GAME IS CLOSING IN " + pTime + "s");
        gameClosingLabel.setSize((int) (gamePanel.getWidth() * 0.75), (int) (gamePanel.getWidth() * 0.5));
        FontLoader.fitFont(gameClosingLabel);
        gameClosingLabel.setSize(gameClosingLabel.getPreferredSize());
        if(! gameClosingLabel.isVisible()){
            resized();
            gameClosingLabel.setVisible(true);
        }

    }
    
    /**Setze das Label, ob das Spiel gewonnen wurde oder nicht
     * 
     * @param pWon Spiel gewonnen? (true: Spiel wurde gewonnen, false: Spiel wurde verloren)
     */
    public void gameWon(boolean pWon){
        Thread warte = new Thread(new Runnable(){
                    public void run(){
                        if(pWon){
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

    /**Wenn eine Taste gedrückt wird, und es WASD ist, dann setze den jeweiligen Boolean-Wert
     * des Arrays keyPressed, der anzeigt, ob eine Taste gedrückt wird, auf true
     * 
     * W -> 0
     * A -> 2
     * S -> 1
     * D -> 3
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            keyPressed[0] = true;
        } else if (key == KeyEvent.VK_S) {
            keyPressed[1] = true;
        } else if (key == KeyEvent.VK_A) {
            keyPressed[2] = true;
        } else if (key == KeyEvent.VK_D) {
            keyPressed[3] = true;
        }
    }
    
    /**Leere Methode, die implementiert werden muss, damit diese Page alle Methoden
     * eines KeyListeners implementiert
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**Wenn eine Taste nicht mehr gedrückt wird, setze den zugehörigen Boolean-Wert
     * des Arrays keyPressed (nur für WASD), der anzeigt, ob eine Tast gedrückt wird, auf false
     * 
     */
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
        }
    }
    
    /**Gib ein ImageIcon zurück
     * 
     * @param path Pfad des Bildes ausgehend vom asset-Ordner
     */
    public ImageIcon getImageIcon (String path)  throws IOException {
        return  new ImageIcon(ImageIO.read(new File("assets/" + path))); 
    }
}
