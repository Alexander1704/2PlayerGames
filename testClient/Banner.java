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

public class Banner extends JPanel{
    private JLabel bannerBackground; 
    private JLabel bannerPlayerName;
    private boolean redDesign;
    public Banner (){
        setLayout(null);
        setOpaque(false);
        setSize(100, 100);
        redDesign = true;

        bannerBackground = new JLabel();
        bannerBackground.setLocation(0, 0);
        bannerBackground.setBackground(Color.BLUE);
        add(bannerBackground);
        
        bannerPlayerName = new JLabel();
        bannerPlayerName.setForeground(Color.WHITE);
        bannerPlayerName.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf", 20));
        bannerPlayerName.setSize(bannerPlayerName.getPreferredSize());
        bannerPlayerName.setLocation(0, 0);
        add(bannerPlayerName);
        setComponentZOrder(bannerPlayerName, 0);
    }

    public void resized(){
        bannerBackground.setSize(getSize());
        try{
            if(redDesign) ImageLoader.fitImage(bannerBackground, "banner_red.png"); 
            else ImageLoader.fitImage(bannerBackground, "banner_blue.png"); 
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("ERROR");
        }
        bannerBackground.setSize(bannerBackground.getPreferredSize());
        
        //0.25 bis 0.95
        bannerPlayerName.setSize((int) (bannerBackground.getWidth() * 0.7), (int) (bannerBackground.getHeight() * 0.4));
        FontLoader.fitFont(bannerPlayerName);
        bannerPlayerName.setSize(bannerPlayerName.getPreferredSize());
        bannerPlayerName.setLocation( (int) ((bannerBackground.getWidth() - bannerPlayerName.getWidth()) * 0.75) , (bannerBackground.getHeight() - bannerPlayerName.getHeight()) / 2);
    }
    
    public void setTitle(String pTitle){
        bannerPlayerName.setText(pTitle);
        resized();
    }
    
    public void switchDesign(){
        redDesign = !redDesign;
    }
    public boolean getDesign(){
        return redDesign;
    }
}
