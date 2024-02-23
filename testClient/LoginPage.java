package testClient;

import assetLoader.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginPage extends Page{
        
    private GUI gui;
    private JPanel loginPanel;
    private JButton connectButton;
    private JLabel ipLabel;
    private JLabel portLabel;
    private JLabel nameLabel;
    private JTextField ipText;
    private JTextField portText;
    private JTextField nameText;


    public LoginPage(GUI gui){
        this.gui = gui;
        setBackground(new Color(94, 144, 252));
        setLayout (null);
        setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        
        loginPanel = new JPanel();
        loginPanel.setBackground(getBackground());
        loginPanel.setSize(300, 400);
        loginPanel.setLayout(null);
        add (loginPanel);
        
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
                            gui.login(ipText.getText(), Integer.parseInt(portText.getText()), nameText.getText());
                        }
                    });
                    loginThread.start();
                }
            });
        loginPanel.add (connectButton);
        
        ipLabel = new JLabel ("IP:");
        ipLabel.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        ipLabel.setSize(ipLabel.getPreferredSize());
        ipLabel.setForeground(Color.WHITE);
        loginPanel.add (ipLabel);
        
        portLabel = new JLabel ("PORT:");
        portLabel.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        portLabel.setSize(portLabel.getPreferredSize());
        portLabel.setForeground(Color.WHITE);
        loginPanel.add (portLabel);
        
        nameLabel = new JLabel ("NAME:");
        nameLabel.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        nameLabel.setSize(nameLabel.getPreferredSize());
        nameLabel.setForeground(Color.WHITE);
        loginPanel.add (nameLabel);        
        
        ipText = new JTextField (1);
        ipText.setText("localhost");
        ipText.setSize(300, 50);
        ipText.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        ipText.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        loginPanel.add (ipText);
        
        portText = new JTextField (1);
        portText.setText("55555");
        portText.setSize(300, 50);
        portText.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        portText.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        loginPanel.add (portText);
        
        nameText = new JTextField (1);
        nameText.setText("player" + ((int) (Math.random() * 10000) + ""));
        nameText.setSize(300, 50);
        nameText.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        nameText.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        loginPanel.add (nameText);
    }
    
    public void start(){
        
    }
    public void resized(){
        FunctionLoader.position(loginPanel, 0.5, 0.4);
        FunctionLoader.position(ipLabel, 0.5, 0);
        FunctionLoader.position(ipText, ipLabel, 0.5, 35, false, true);
        FunctionLoader.position(portLabel, ipText, 0.5, 70, false, true);
        FunctionLoader.position(portText, portLabel, 0.5, 35, false, true);
        FunctionLoader.position(nameLabel, portText, 0.5, 70, false, true);
        FunctionLoader.position(nameText, nameLabel, 0.5, 35, false, true);
        FunctionLoader.position(connectButton, nameText, 0.5, 90, false, true);
    }
    public void positionElements(){
        
        
        
        
    }

    public void finish(){
        
    }
    
    public void update(){
        
    }
    
    public String getPlayerName(){
        return nameText.getText();
    }
}
