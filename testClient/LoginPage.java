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
    public String getDescription(){
        return "loginPage";
    }
    public void reloadData(){
        
    }
    public void componentResized(){
    }
    public void positionElements(){
        int frameWidth = gui.getFrame().getWidth();
        int frameHeight = gui.getFrame().getHeight();
        
        ipLabel.setLocation((frameWidth - ipLabel.getWidth())/ 2, (int) ((frameHeight- ipLabel.getHeight()) * 1.0 / 5));
        ipText.setLocation((frameWidth - ipText.getWidth())/ 2, ipLabel.getY() + 50);
        portLabel.setLocation((frameWidth - portLabel.getWidth())/ 2, ipLabel.getY() + 150);
        portText.setLocation((frameWidth - portText.getWidth())/ 2, ipLabel.getY() + 200);
        
        connectButton.setLocation((frameWidth - connectButton.getWidth())/ 2, ipLabel.getY() + 300);
        
    }
    public void resizeElements(){
    }
}
