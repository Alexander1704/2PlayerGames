package testClient;

import assetLoader.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.Map;
import java.awt.event.*;

public abstract class Page extends JPanel{
    public abstract void positionElements();
    public abstract void resizeElements();
    public abstract void reloadData();
    public abstract void componentResized();
    
    @Override
    public void paintComponent(Graphics g){
        updateElements();
        super.paintComponent(g);
    }
    public void updateElements(){
        resizeElements();
        positionElements();
    }
}
