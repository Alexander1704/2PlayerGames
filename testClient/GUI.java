package testClient;

import assetLoader.*;
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
    ServerStart server = new ServerStart();
    private JFrame frame;
    private DBController dbc;
    private Page currentPage;
    private LoginPage loginPage;
    private MenuPage menuPage;
    private LoadingPage loadingPage;
    private GamePage gamePage;
    private ErrorPage errorPage;
    private InfoPage infoPage;
    private boolean running;
    private UserClient userClient;
    private UserClient userClientTest;
    
    private PanelTest2 panelTest2;
    private AnimationPage animationPage;
    public GUI(){
        System.out.println("Sys start");
        frame = new JFrame("2PlayerGames");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setLocationRelativeTo(null);
        frame.pack();
        
        frame.addKeyListener(this);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(true);
        running = true;

        dbc = new DBController();
        loginPage = new LoginPage(this);
        menuPage = new MenuPage(this);
        loadingPage = new LoadingPage(this);
        gamePage = new GamePage(this);
        errorPage = new ErrorPage(this);
        // infoPage = new InfoPage(this);
        
        // animationPage = new AnimationPage(this);
        // panelTest2 = new PanelTest2(this);
        currentPage = loginPage;
        switchPage(currentPage);

        // Thread renderFrame = new Thread(new Runnable() {
                    // @Override
                    // public void run() {
                        // renderFrame();
                    // }
                // });
        // renderFrame.start();
        
        
        TickThread renderFrameThread = new TickThread(60, new Runnable(){
            @Override
            public void run(){
                currentPage.repaint();                
            }
        });
        renderFrameThread.start();
        

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

        // frame.addKeyListener(new KeyAdapter() {
        // public void keyPressed(KeyEvent e) {
        // currentPage.keyPressed(e);
        // }
        // });
    }

    public void login(String ip, int port){
        switchPage(loadingPage);   
        Thread connectToGame = new Thread(new Runnable() {
                    public void run(){
                        int counter = 0;
                        final int WAIT_TIME = 5000;
                        while(counter < WAIT_TIME && (userClient == null || !userClient.hasConnected())){
                            warte (100);
                            counter += 100;
                        }
                        if(userClient != null && userClient.hasConnected()) switchPage(menuPage);
                        else switchPage(errorPage);
                    }
                });
        connectToGame.start();
        
        userClient = new UserClient(this, ip, port);
        userClientTest = new UserClient(this, ip, port);
        userClientTest.setProcessingMessages(false);
        userClientTest.send("CONNECT SEARCHGAME");
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

    public DBController getDBController(){
        return dbc;
    }

    public MenuPage getMenuPage(){
        return menuPage;
    }

    public LoadingPage getLoadingPage(){
        return loadingPage;
    }

    public GamePage getGamePage(){
        return gamePage;
    }

    public ErrorPage getErrorPage(){
        return errorPage;
    }
    
    public LoginPage getLoginPage(){
        return loginPage;
    }

    public UserClient getUserClient(){
        return userClient;
    }

    public PanelTest2 getTestPage2(){
        return panelTest2;
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
