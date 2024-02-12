package testClient;


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
        errorTitle.setLocation(0, 0);
        errorTitle.setForeground(new Color(255, 0, 0)); // Set the text color to green
        errorTitle.setFont(new Font("Helvetica Bold", Font.PLAIN, 36)); // Set the font to plain monospaced
        errorTitle.setSize(errorTitle.getPreferredSize());
        add(errorTitle);
        
        errorSign = new JLabel();
        errorSign.setLocation(0, 0);
        errorSign.setSize(100, 100);
        try{
           errorSign.setIcon(getScaledIcon("errorSign-removebg-preview.png", .4, .4)); 
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("ERROR");
        }
        errorSign.setSize(errorSign.getPreferredSize());
        // errorSign.setVisible(false);
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
    public String getDescription(){
        return "loadingPage";
    }
    public void reloadData(){
        
    }
    public void componentResized(){
        positionElements();
    }
    public void resizeElements(){
        
    }
    public void positionElements(){
        int frameWidth = gui.getFrame().getWidth();
        int frameHeight = gui.getFrame().getHeight();
        
        errorTitle.setLocation((frameWidth - errorTitle.getWidth() )/ 2, errorSign.getY() - 10 - frameHeight/15); 
        errorSign.setLocation((frameWidth - errorSign.getWidth())/2, (int)((frameHeight - errorSign.getHeight()) *0.4));
        errorMessage.setLocation((frameWidth - errorMessage.getWidth())/2, errorSign.getY() + errorSign.getHeight()/2 + 50 + frameHeight/10);
        retryButton.setLocation((frameWidth - retryButton.getWidth())/2, errorMessage.getY() + 20 + frameHeight/20);
    }
}
