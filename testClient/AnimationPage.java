package testClient;

import assetLoader.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.File;
import java.io.IOException;
import java.io.*;
import javax.imageio.ImageIO;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.*;
import javax.imageio.ImageIO;

public class AnimationPage extends Page{
    private GUI gui;
    private JLabel animationLabel;
    public AnimationPage (GUI pGUI){
        this.gui = pGUI;
        setLayout(null);

        animationLabel = new JLabel("here is the label placed");
        add (animationLabel);
        

        Thread playerAnimation = new Thread(new Runnable(){
                    public void run(){
                        int animationNum = 0;
                        int MAX_ANIMATION_NUM = 43;
                        while(true){
                            System.out.println("Curr Animation: " + animationNum);
                            String imgPath = "player/playerAnimation/running_ basic player_" + getNumString(animationNum + "") + ".png";
                            System.out.println(imgPath);
                            // imgPath = "assets/player/playerAnimation/running_ basic player_0010.png";
                            // imgPath = "assets/player/playerAnimation/running_ basic player_0010.png";
                            try{
                                // animationLabel.setIcon(getResizedIcon("player/playerAnimation/running_ basic player_" + getNumString(animationNum + "") + ".png", 640, 360)); 
                                animationLabel.setIcon(ImageLoader.getResizedIcon(imgPath , 640, 360));
                            }catch (Exception e){
                                e.printStackTrace();
                                System.out.println("ERROR");
                                System.err.println(e);
                                System.out.println("THE ERROR IS: " + e);
                            }
                            animationLabel.setSize(animationLabel.getPreferredSize());
                            animationLabel.setLocation(0, 0);
                            FunctionLoader.warte(10);
                            
                            animationNum++;
                            if(animationNum > MAX_ANIMATION_NUM) animationNum = 0;
                        }
                    }
                });
        // playerAnimation.start();
    }

    private String getNumString(String a){
        for(int i = a.length(); i < 4; i++){
            a = "0" + a;
        }
        return a;
    }

    public String getDescription(){
        return "AnimationPage";
    }

    public void positionElements(){
    }

    public void resizeElements(){
    }

    public void reloadData(){

    }

    public void componentResized(){

    }
}
