package testClient;

import assetLoader.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ErrorPage extends Page{  
    private GUI gui;
    private JLabel errorTitle;
    private JLabel errorSign;
    private JLabel errorMessage;
    private JButton retryButton;
    public ErrorPage(GUI gui) {
        this.gui = gui;
        setBackground(new Color(249, 249, 249));
        setLayout (null);
        
        errorTitle = new JLabel("An error occured :(");
        errorTitle.setForeground(new Color(255, 0, 0)); // Set the text color to green
        errorTitle.setFont(new Font("Helvetica Bold", Font.PLAIN, 36)); // Set the font to plain monospaced
        errorTitle.setSize(errorTitle.getPreferredSize());
        add(errorTitle);
        
        errorSign = new JLabel();
        errorSign.setLocation(0, 0);
        errorSign.setSize(100, 100);
        try{
           errorSign.setIcon(ImageLoader.getScaledIcon("errorSign-removebg-preview.png", .4, .4)); 
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("ERROR");
        }
        errorSign.setSize(errorSign.getPreferredSize());
        add(errorSign);
        
        
        errorMessage = new JLabel("Sorry! An error occured while running this program!");
        errorMessage.setForeground(new Color(0, 0, 0)); // Set the text color to green
        errorMessage.setFont(new Font("Helvetica Bold", Font.PLAIN, 24)); // Set the font to plain monospaced
        errorMessage.setLocation(0,0);
        errorMessage.setSize(errorMessage.getPreferredSize());
        add(errorMessage);
        
        retryButton = new JButton("go back");
        retryButton.setBackground(new Color(76, 151, 255));
        retryButton.setForeground(Color.WHITE);
        retryButton.setFont(new Font("Helvetica Bold", Font.PLAIN, 24));
        retryButton.setLocation(0,0);
        retryButton.setSize(150, 40);
        add(retryButton);
        
        retryButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event){
                if(event.getSource() == retryButton) {
                    if(gui.getUserClient() != null && gui.getUserClient().hasConnected())gui.switchPage(gui.getMenuPage());
                    else gui.switchPage(gui.getLoginPage());
                    
                }
            }
        });
    }

    @Override
    public void start(){
        
    }
    
    @Override
    public void finish(){
        
    }
    
    @Override
    public void resized(){
        FunctionLoader.position(errorSign, 0.5, 0.4);
        FunctionLoader.position(errorTitle, errorSign, 0.5, - 10 - gui.getFrame().getHeight()/ 15, false, true);
        FunctionLoader.position(errorMessage, errorSign, 0.5, errorSign.getHeight() / 2+ 50 + gui.getFrame().getHeight() / 10, false, true);
        FunctionLoader.position(retryButton, errorMessage, 0.5, 20 + gui.getFrame().getHeight() / 20, false, true);
    }
    
    @Override
    public void update(){
        
    }
}
