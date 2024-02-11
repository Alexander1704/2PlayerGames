package testClient;


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
        
        connectButton = new JButton ("connect");
        ipLabel = new JLabel ("IP:");
        portLabel = new JLabel ("PORT:");
        ipText = new JTextField (1);
        portText = new JTextField (1);

        //add components
        add (connectButton);
        add (ipLabel);
        add (portLabel);
        add (ipText);
        add (portText);
        
        ipLabel.setForeground(Color.WHITE);
        portLabel.setForeground(Color.WHITE);
        
        connectButton.setSize(200, 50);
        ipLabel.setSize(ipLabel.getPreferredSize());
        ipText.setText("192.168.2.158");
        ipText.setSize(300, 50);
        portLabel.setSize(portLabel.getPreferredSize());
        portText.setText("55555");
        portText.setSize(300, 50);
        
        connectButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    gui.login(ipText.getText(), Integer.parseInt(portText.getText()));
                }
            });
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
