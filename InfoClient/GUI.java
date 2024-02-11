package InfoClient;

import database.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Toolkit;
import java.util.Map;
import java.awt.event.*;

public class GUI implements KeyListener{
    private JFrame frame;
    private Page currentPage;
    private DBController dbc;
    private boolean running;
    private InfoPanel infoPanel; 
    public GUI(){
        System.out.println("Sys start");
        frame = new JFrame("");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setSize(new Dimension(400, 300));
        frame.pack();
        
        frame.addKeyListener(this);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(true);

        infoPanel = new InfoPanel(this);
        
        currentPage = infoPanel;
        running = true;

        dbc = new DBController();

        
        switchPage(currentPage);

        Thread renderFrame = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        renderFrame();
                    }
                });
        renderFrame.start();

        frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    currentPage.componentResized();
                }
            });
        frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.out.println("Fenster wird geschlossen.");
                }
            });
    }

    public void renderFrame(){
        final int TARGET_FPS = 30;
        final long TARGET_TIME = 1000000000 / TARGET_FPS; // nanoseconds per frame

        long lastLoopTime = System.nanoTime();

        while (running) {
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - lastLoopTime;
            lastLoopTime = currentTime;

            // Perform your game logic or operations here
            if(currentPage != null) {
                currentPage.repaint();
            }

            // Calculate time to sleep to maintain desired FPS
            long sleepTime = TARGET_TIME - elapsedTime;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1000000); // Convert nanoseconds to milliseconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public JFrame getFrame(){
        return frame;
    }

    public Page getCurrentPage(){ 
        return currentPage;
    }

    public Dimension getScreenSize(){
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    public int getFrameWait(){
        return 1000/ 60;
    }

    public void switchPage(Page to){
        switchPage(currentPage, to);
    }

    public void switchPage(Page from, Page to){
        if (from != null) frame.remove(from);
        frame.add(to);
        currentPage = to;
        currentPage.reloadData();
        currentPage.componentResized();
        frame.setTitle("2PlayerGames ALPHA - " + currentPage.getDescription());
        frame.validate();
        frame.repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // keyListener
        if(currentPage instanceof KeyListener){
            KeyListener keyListener = (KeyListener) currentPage;
            keyListener.keyPressed(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // keyListener
        if(currentPage instanceof KeyListener){
            KeyListener keyListener = (KeyListener) currentPage;
            keyListener.keyTyped(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // keyListener
        if(currentPage instanceof KeyListener){
            KeyListener keyListener = (KeyListener) currentPage;
            keyListener.keyReleased(e);
        }
    }

    public void warte(int time){
        try{
            Thread.sleep(time);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main (String[] args) {
        new GUI();
    }
}
