package testClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import server.*;

public class GameTest implements KeyListener{
    private class Player extends JPanel{
        double xPos, yPos;

        public void setPosition(double a, double b){
            xPos = a;
            yPos = b;
        }
    }
    JFrame testFrame;
    JPanel gamePanel;
    Player player1;
    Player player2;
    private UserClient userClient1;
    private UserClient userClient2;
    BroadcastServer broadcastServer;

    public GameTest(){
        testFrame = new JFrame();
        testFrame.setLayout(null);
        testFrame.setTitle("[USER] Simple Platformer");
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testFrame.setSize(800, 400);
        testFrame.setLocationRelativeTo(null);
        testFrame.setVisible(true);

        testFrame.addKeyListener(this);
        testFrame.setFocusable(true);
        testFrame.setFocusTraversalKeysEnabled(true);

        gamePanel = new JPanel();
        gamePanel.setLocation(0, 0);
        gamePanel.setSize(800, 400);
        // gamePanel.setBackground(Color.BLACK);

        player1 = new Player();
        player1.setSize(50, 50);
        player1.setLocation(0,0);
        player1.setBackground(Color.BLUE);
        testFrame.add(player1);

        player2 = new Player();
        player2.setSize(50, 50);
        player2.setLocation(0,0);
        player2.setBackground(Color.RED);
        testFrame.add(player2);

        testFrame.add(gamePanel);

        Thread gameThread = new Thread(new Runnable(){
                    @Override 
                    public void run(){
                        while(true){
                            updateGame();
                            testFrame.repaint();
                            try{
                                Thread.sleep(10);
                            }catch (Exception e){

                            }

                        }
                    }
                });
        gameThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    // Code to be executed when the application is closing
                    System.out.println("Performing cleanup tasks...");
                    broadcastServer.close();
                    // Perform cleanup actions here
                }
            });

        int port = 55319;
        broadcastServer= new BroadcastServer(port);

        userClient1= new UserClient(null, "localhost", port);
        while(! userClient1.hasConnected()){
            try{
                Thread.sleep(10);
            }
            catch (InterruptedException ie){
                ie.printStackTrace();
            }
        }
        userClient2= new UserClient(null, "localhost", port);
        while(! userClient2.hasConnected()){
            try{
                Thread.sleep(10);
            }
            catch (InterruptedException ie){
                ie.printStackTrace();
            }
        }
        userClient1.send("INFO GAME NUMBER");

        userClient1.send("CONNECT SEARCHGAME");
        userClient2.send("CONNECT SEARCHGAME");

        System.out.println("Both players are connected");

        while(! userClient1.inGame()){
            try{
                Thread.sleep(10);
            }
            catch (InterruptedException ie){
                ie.printStackTrace();
            }
        }
        while(! userClient2.inGame()){}

        System.out.println("Both players are in game");

        warte(2000);
        userClient1.send("USERINPUT d");
        warte(2000);
        userClient1.send("USERINPUT a");
        // userClient1.send("INFO GAME NUMBER");
        // userClient1.send("CONNECT EXITGAME");
        // warte(3000);
        // userClient2.send("CONNECT EXITGAME");
        // userClient1.send("INFO GAME NUMBER");
        userClient1.send("INFO GAME NUMBER");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {

        } else if (key == KeyEvent.VK_S) {

        } else if (key == KeyEvent.VK_A) {
            userClient1.send("USERINPUT a");
        } else if (key == KeyEvent.VK_D) {
            userClient1.send("USERINPUT d");
        } else if (key == KeyEvent.VK_LEFT) {
            userClient2.send("USERINPUT a");
        }
        else if (key == KeyEvent.VK_RIGHT) {
            userClient2.send("USERINPUT d");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed for this implementation
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed for this implementation
    }

    public void updateGame(){
        int frameWidth = testFrame.getWidth();
        int frameHeight = testFrame.getHeight();

        player1.setLocation((int) (frameWidth * player1.xPos), (int) (frameHeight * (1-player1.yPos)));
        player2.setLocation((int) (frameWidth * player2.xPos), (int) (frameHeight * (1-player2.yPos)));
    }

    public Player getPlayer1(){
        return player1;
    }

    public Player getPlayer2(){
        return player2;
    }

    private void warte(int time){
        try{
            Thread.sleep(time);
        }
        catch (InterruptedException ie){
            ie.printStackTrace();
        }
    }

    public void setPosition(int player, double x, double y){
        if(player == 0) player1.setPosition(x, y);
        if(player == 1) player2.setPosition(x, y);
    }
}
