package testClient;

import assetLoader.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginPage extends Page{
        
    private GUI gui;
    private JButton connectButton;
    private JLabel ipLabel;
    private JLabel portLabel;
    private JTextField ipText;
    private JTextField portText;


    public LoginPage(GUI gui){
        this.gui = gui;
        setBackground(new Color(94, 144, 252));
        setLayout (null);
        setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        
        
        connectButton = new JButton ("connect");
        connectButton.setSize(200, 50);
        connectButton.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        connectButton.setBackground(new Color(93, 252, 153));
        connectButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        connectButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    Thread loginThread = new Thread(new Runnable() {
                        @Override
                        public void run(){
                            gui.login(ipText.getText(), Integer.parseInt(portText.getText()));
                        }
                    });
                    loginThread.start();
                }
            });
        add (connectButton);
        
        ipLabel = new JLabel ("IP:");
        ipLabel.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        ipLabel.setSize(ipLabel.getPreferredSize());
        ipLabel.setForeground(Color.WHITE);
        add (ipLabel);
        
        portLabel = new JLabel ("PORT:");
        portLabel.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        portLabel.setSize(portLabel.getPreferredSize());
        portLabel.setForeground(Color.WHITE);
        add (portLabel);
        
        ipText = new JTextField (1);
        ipText.setText("localhost");
        ipText.setSize(300, 50);
        ipText.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        ipText.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        add (ipText);
        
        portText = new JTextField (1);
        portText.setText("55555");
        portText.setSize(300, 50);
        portText.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        portText.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        add (portText);
    }
    
    public void start(){
        
    }
    public void resized(){
        FunctionLoader.position(ipLabel, 0.5, 0.2);
        FunctionLoader.position(ipText, ipLabel, 0.5, 50, false, true);
        FunctionLoader.position(portLabel, ipLabel, 0.5, 150, false, true);
        FunctionLoader.position(portText, ipLabel, 0.5, 200, false, true);
        FunctionLoader.position(connectButton, ipLabel, 0.5, 300, false, true);
    }
    public void positionElements(){
        
        
        
        
    }

    public void finish(){
        
    }
    public void update(){
        
    }
}
