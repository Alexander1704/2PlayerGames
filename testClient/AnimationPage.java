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
        

        Thread updateAnimation = new Thread(new Runnable(){
           public void run(){
               int MAX_ANIMATION = 9;
               int animation = 1;
               while(true){
                   animation++;
                   if(animation > MAX_ANIMATION) animation = 1;
               }
           }
        });
    }
    
    public void finish(){
        
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
    
    public void updateElements(){
        
    }
}
