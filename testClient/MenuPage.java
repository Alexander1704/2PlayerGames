package testClient;

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
    private JLabel characterImage;
    private JLabel characterName;
    private JLabel characterDescription;
    private JButton playButton;
    private JButton nextPlayerButton;
    private JButton previousPlayerButton;
    public MenuPage(GUI gui) {
        setLayout(null);
        setBackground(new Color(94, 144, 252));
        this.gui = gui;

        character = "Player";
        characterList = gui.getDBController().getPlayerNames();

        characterName = new JLabel(character);
        characterName.setOpaque(true);
        characterName.setBackground(Color.WHITE);
        characterName.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5)); // Schwarzer Rand
        characterName.setHorizontalAlignment(SwingConstants.CENTER);
        characterName.setFont(loadFont("assets/LilitaOne-Regular.ttf",25));
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
        playButton.setFont(loadFont("assets/LilitaOne-Regular.ttf",25));
        add(playButton);

        nextPlayerButton = new JButton(">");
        nextPlayerButton.setBackground(new Color(93, 252, 153));
        nextPlayerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        nextPlayerButton.setFont(loadFont("assets/LilitaOne-Regular.ttf",25));
        add(nextPlayerButton);

        previousPlayerButton = new JButton("<");
        previousPlayerButton.setBackground(new Color(93, 252, 153));
        previousPlayerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        previousPlayerButton.setFont(loadFont("assets/LilitaOne-Regular.ttf",25));
        add(previousPlayerButton);

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
                    Thread loginThread = new Thread(new Runnable() {
                        public void run(){
                            gui.warte(30000);
                            if(! gui.getUserClient().inGame()) gui.switchPage(gui.getErrorPage());
                            // gui.switchPage(gui.getLoadingPage(), gui.getGamePage());
                        }
                    });
                    loginThread.start();
                }
            });

        updateElements();
    }

    public boolean running(){
        return gui.getCurrentPage() == this; 
    }
    public String getCharacter(){
        return character;
    }
    public void setCharacter(String name){
        character = name;
        characterName.setText(name);
    }

    @Override
    public void paintComponent(Graphics g){
        updateElements();
        super.paintComponent(g);
    }

    public String getDescription() {
        return "menuPage";
    }

    public void positionElements() {
        int frameWidth = gui.getFrame().getWidth();
        int frameHeight = gui.getFrame().getHeight();

        characterImage.setLocation((frameWidth - characterImage.getWidth()) / 2, (int) ((frameHeight - characterImage.getHeight()) * 0.3));
        characterName.setLocation((frameWidth - characterName.getWidth()) / 2, (characterImage.getY()) - 60);
        playButton.setLocation((frameWidth - playButton.getWidth())/ 2, (characterImage.getY() + characterImage.getHeight()) + 15);
        nextPlayerButton.setLocation(characterImage.getX() + characterImage.getWidth() + 10, characterName.getY());
        previousPlayerButton.setLocation(characterImage.getX() - previousPlayerButton.getWidth() - 10, characterName.getY());
    }

    public void resizeElements() {
        int frameWidth = gui.getFrame().getWidth();
        int frameHeight = gui.getFrame().getHeight();

        characterImage.setSize((int) (frameWidth / 3.0), frameHeight / 2);
        try {
            String imgPath = "player/" + character + "/animation1.png";
            ImageIcon icon = cropIcon(new ImageIcon("assets/" + imgPath), 0, 0, 0, 0);
            // Dimension imgSize = getImageSize("player/" + character + "/a (1).png");
            Dimension imgSize = new Dimension(icon.getIconWidth(), icon.getIconHeight());
            int border = frameHeight/ 10;
            double scaling = Math.min((characterImage.getWidth() - border) * 1.0 /imgSize.getWidth(), (characterImage.getHeight() - border) * 1.0 / imgSize.getHeight());
            characterImage.setIcon(cropIcon(getScaledIcon(imgPath, scaling, scaling), 0, 0, 0, 0));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(character +"::: " + "player/" + character + "/a (1).png");
        }

        characterName.setSize(characterImage.getWidth(), 50);

        int buttonWidth = characterImage.getWidth() < 300 ? characterImage.getWidth() : 300;
        playButton.setSize(buttonWidth, 65);

        int characterChoosingWidth = frameWidth > 900 ? 65 : 40;
        nextPlayerButton.setSize(characterChoosingWidth, characterImage.getY() + characterImage.getHeight() - characterName.getY());
        previousPlayerButton.setSize(characterChoosingWidth, characterImage.getY() + characterImage.getHeight() - characterName.getY());
    }

    public void reloadData() {

    }

    public void componentResized() {
        int frameWidth = gui.getFrame().getWidth();
        int frameHeight = gui.getFrame().getHeight();

    }
    public void keyPressed(KeyEvent e) {
    }
    public Dimension getImageSize(String path){
        try {
            File file = new File("assets/" + path); 
            Image image = ImageIO.read(file);

            if (image != null) {
                int width = image.getWidth(null); // Breite des Bildes
                int height = image.getHeight(null); // Höhe des Bildes
                return new Dimension(width, height);
            } else {
                System.out.println("Das Bild konnte nicht geladen werden.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
