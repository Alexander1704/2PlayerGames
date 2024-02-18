package clientGame;

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

public class ComponentTester{
    private JFrame frame;
    private JPanel panel;
    private ImageLoader imageLoader;
    private HealthPanel component;
    public ComponentTester(){
        System.out.println("ComponentTester start");
        frame = new JFrame("Component Test");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();

        panel = new JPanel();
        panel.setLayout(null);
        frame.add (panel);

        imageLoader = new ImageLoader();
        
        component = new HealthPanel(panel);
        component.setLocation(50, 50);
        component.setHealth(25);
        panel.add(component);
        
        component.componentResized();
        
        
        
        frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    component.componentResized();
                }
            });
            
        Thread updateThread = new Thread(new Runnable() {
            public void run(){
                updateThread();
            }
        });
        updateThread.start();
    }
    
    public void updateThread(){
        final int TARGET_FPS = 30;
        final long TARGET_TIME = 1000000000 / TARGET_FPS; // nanoseconds per frame

        long lastLoopTime = System.nanoTime();
        boolean running = true;
        
        while (running) {
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - lastLoopTime;
            lastLoopTime = currentTime;

            // Perform your game logic or operations here
            update();
            
            // Calculate time to sleep to maintain desired FPS
            long sleepTime = TARGET_TIME - elapsedTime;
            if (sleepTime > 0) {
                new FunctionLoader().warte( (int) (sleepTime/1000000 )); // Convert nanoseconds to milliseconds
            }
        }
    }

    public void update(){
        int panelSize = Math.min(frame.getWidth() / 16, frame.getHeight() / 9);
        panel.setSize(panelSize * 16, panelSize * 9);
        
        component.update();
        

    }
    public static void main(){
        new ComponentTester();
    }
}
