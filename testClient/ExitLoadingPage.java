package testClient;

import assetLoader.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ExitLoadingPage extends LoadingPage{
    private JButton exitButton;
    private Thread leaveThread;
    public ExitLoadingPage(GUI pGUI){
        super(pGUI);
        
        exitButton = new JButton("Leave");
        exitButton.setBackground(new Color(255, 171, 25));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        exitButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        add(exitButton);
        
        exitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                synchronized(leaveThread){
                     leaveThread.run();
                }
            }
        });
    }
    
    @Override 
    public void start(){
        super.start();
        leaveThread = new Thread();
    }
    
    @Override 
    public void resized(){
        super.resized();
        
        //Skaliere Elemente
        exitButton.setSize(gui.getFrame().getWidth()/ 7, gui.getFrame().getWidth()/ 7 / 3);
        FontLoader.fitFont(exitButton);
        
        //Positioniere Elemente
        FunctionLoader.position(exitButton, 0.9, 0.95);
    }
    
    public void setLeaveThread(Thread pLeaveThread){
        synchronized(leaveThread){
            this.leaveThread = pLeaveThread;
        }
    }
}