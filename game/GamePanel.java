package game;
import javax.swing.*;
import java.awt.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel {
    JFrame testFrame;
    JPanel gamePanel;
    Player player1;
    Player player2;
    public GamePanel(Player p1, Player p2){
        testFrame = new JFrame();
        testFrame.setLayout(null);
        testFrame.setTitle("[SERVER] Simple Platformer");
        testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        testFrame.setSize(800, 400);
        testFrame.setLocationRelativeTo(null);
        testFrame.setVisible(true);
        // testFrame.pack();
        
        gamePanel = new JPanel();
        gamePanel.setLocation(0, 0);
        gamePanel.setSize(800, 400);
        // gamePanel.setBackground(Color.BLACK);
        
        p1.setBackground(Color.BLUE);
        player1 = p1;
        testFrame.add(player1);
        
        p2.setBackground(Color.RED);
        player2 = p2;
        testFrame.add(player2);
        
        testFrame.add(gamePanel);
        
        Thread gameThread = new Thread(new Runnable(){
            @Override 
            public void run(){
                 while(true){
                     updateGame();
                     testFrame.repaint();
                     try{
                         Thread.sleep(1000/ 100);
                     }catch (Exception e){
                         
                     }
                     
                 }
            }
        });
        gameThread.start();
    }
    
    public void updateGame(){
        int frameWidth = testFrame.getWidth();
        int frameHeight = testFrame.getHeight();
        
        player1.setLocation((int) (frameWidth * player1.getXPos()), (int) (frameHeight * (1-player1.getYPos())));
        player2.setLocation((int) (frameWidth * player2.getXPos()), (int) (frameHeight * (1-player2.getYPos())));
    }
    
    public boolean isMoving(){
        return player1.isMoving() || player2.isMoving();
    }
}