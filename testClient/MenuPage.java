package testClient;

import assetLoader.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.event.*;

public class MenuPage extends Page { 
    private GUI gui;
    private String character;
    private ArrayList<String> characterList;
    private JLabel playerName;
    private JLabel characterImage;
    private JLabel characterName;
    private JLabel characterDescription;
    private JButton playButton;
    private JButton nextPlayerButton;
    private JButton previousPlayerButton;
    private JButton creditsButton;
    public MenuPage(GUI gui) {
        setLayout(null);
        setBackground(new Color(94, 144, 252));
        this.gui = gui;

        character = "Player";
        characterList = gui.getDBController().getPlayerNames();
        
        playerName = new JLabel("PLAYER");
        playerName.setForeground(Color.WHITE);
        playerName.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        playerName.setLocation(0, 0);
        playerName.setSize(playerName.getPreferredSize());
        add(playerName);
        
        characterName = new JLabel(character);
        characterName.setOpaque(true);
        characterName.setBackground(Color.WHITE);
        characterName.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5)); // Schwarzer Rand
        characterName.setHorizontalAlignment(SwingConstants.CENTER);
        characterName.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        add(characterName);

        characterImage = new JLabel();
        characterImage.setOpaque(true);
        characterImage.setBackground(Color.WHITE);
        characterImage.setHorizontalAlignment(SwingConstants.CENTER);
        characterImage.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        add(characterImage);

        playButton = new JButton("Play");
        playButton.setBackground(new Color(93, 252, 153));
        playButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        playButton.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        add(playButton);

        nextPlayerButton = new JButton(">");
        nextPlayerButton.setBackground(new Color(93, 252, 153));
        nextPlayerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        nextPlayerButton.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        add(nextPlayerButton);

        previousPlayerButton = new JButton("<");
        previousPlayerButton.setBackground(new Color(93, 252, 153));
        previousPlayerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        previousPlayerButton.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        add(previousPlayerButton);
        
        creditsButton = new JButton("credits");
        creditsButton.setBackground(new Color(93, 252, 153));
        creditsButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        creditsButton.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        add(creditsButton);

        nextPlayerButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    int index = characterList.indexOf(character);
                    try{
                        setCharacter(characterList.get(index + 1));
                    } catch (Exception ex){
                        setCharacter(characterList.get(0));
                    }

                }
            });
        previousPlayerButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    int index = characterList.indexOf(character);
                    try{
                        setCharacter(characterList.get(index - 1));
                    } catch (Exception ex){
                        setCharacter(characterList.get(characterList.size() - 1));
                    }

                }
            });
        playButton.addActionListener(new ActionListener(){
                @Override 
                public void actionPerformed(ActionEvent e){
                    gui.switchPage(gui.getLoadingPage());
                    gui.getUserClient().send("CONNECT SEARCHGAME");
                    
                    long threadStart = System.currentTimeMillis();
                    TickThread loginThread = new TickThread(null);
                    loginThread = new TickThread(gui.getTick(), new Runnable() {
                        public void run(){
                            // if(gui.getUserClient().inGame()) loginThread.finish();
                            
                            long elapsedTime = System.currentTimeMillis() - threadStart;
                            // if(elapsedTime > 15000) gui.switchPage(gui.getErrorPage());
                        }
                    });
                    
                    // loginThread.start();
                }
            });
        creditsButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    gui.switchPage(gui.getCreditsPage());
                }
            });
        
        resized();
    }

    @Override
    public void start() {
        // playerName.setText(gui.getLoginPage().getPlayerName());
    }
    
    @Override
    public void finish(){
        
    }

    @Override
    public void resized() {
        int frameWidth = gui.getFrame().getWidth();
        int frameHeight = gui.getFrame().getHeight();
        
        //Skaliere die Elemente
        creditsButton.setSize(frameHeight/ 10, frameHeight/ 10);
        FontLoader.fitFont(creditsButton);
        
        FunctionLoader.scale(playerName, 0.8, 0.05);
        FontLoader.fitFont(playerName);
        
        setCharacter(getCharacter());

        characterName.setSize(characterImage.getWidth(), 50);

        int buttonWidth = characterImage.getWidth() < 300 ? characterImage.getWidth() : 300;
        playButton.setSize(buttonWidth, 65);

        int characterChoosingWidth = frameWidth > 900 ? 65 : 40;
        nextPlayerButton.setSize(characterChoosingWidth, characterImage.getY() + characterImage.getHeight() - characterName.getY());
        previousPlayerButton.setSize(characterChoosingWidth, characterImage.getY() + characterImage.getHeight() - characterName.getY());
        
        
        //Positioniere die Elemente
        FunctionLoader.position(creditsButton, 0.9, 0.5);
        FunctionLoader.position(playerName, 0, 1);
        FunctionLoader.position(characterImage, 0.5, 0.3);
        FunctionLoader.position(characterName, characterImage, 0.5, - 60, false, true);
        FunctionLoader.position(playButton, characterImage, 0.5, characterImage.getHeight() + 15, false, true);
        nextPlayerButton.setLocation(characterImage.getX() + characterImage.getWidth() + 10, characterName.getY());
        previousPlayerButton.setLocation(characterImage.getX() - previousPlayerButton.getWidth() - 10, characterName.getY());
    }
    
    @Override
    public void update(){}
    
    public void setPlayerName(String pPlayerName){
        playerName.setText(pPlayerName);
    }
    public String getCharacter(){
        return character;
    }
    public void setCharacter(String name){
        character = name;
        characterName.setText(name);
        
        characterImage.setSize((int) (gui.getFrame().getWidth() / 3.0), gui.getFrame().getHeight() / 2);
        try {
            String imgPath = "player/" + character + "/animation10.png";
            ImageIcon icon = (new ImageIcon("assets/" + imgPath));
            Dimension imgSize = new Dimension(icon.getIconWidth(), icon.getIconHeight());
            int border = gui.getFrame().getHeight()/ 10;
            double scaling = Math.min((characterImage.getWidth() - border) * 1.0 /imgSize.getWidth(), (characterImage.getHeight() - border) * 1.0 / imgSize.getHeight());
            characterImage.setIcon((ImageLoader.getScaledIcon(imgPath, scaling, scaling)));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(character +"::: " + "player/" + character + "/a (1).png");
        }
        
    }
}
