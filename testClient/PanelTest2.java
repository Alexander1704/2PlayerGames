package testClient;

import assetLoader.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JViewport;
import java.util.ArrayList;

public class PanelTest2 extends Page {
    private class SpectatePanel extends JPanel{
        JLabel timeLabel;
        JLabel gameIdLabel;
        JLabel player1Label;
        JLabel player2Label;
        JLabel vsLabel;
        
        JLabel backgroundImageLabel;
        SpectatePanel(){
            setLayout(null);
            setPreferredSize(new Dimension(3000, 500));
            setSize(getPreferredSize());
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            setBackground(Color.WHITE);
            setMinimumSize(new Dimension(0, 200));
            
            gameIdLabel = new JLabel("Game id: #1");
            gameIdLabel.setLocation(5, 5);
            gameIdLabel.setSize(gameIdLabel.getPreferredSize());
            add(gameIdLabel);
            
            timeLabel = new JLabel("<html>time: <font color='red'>0min 45s</font></html>");
            timeLabel.setSize(timeLabel.getPreferredSize());
            add(timeLabel);
            
            player1Label = new JLabel("<html><font color='blue' size='5'>Player 1</font></html>");
            player1Label.setSize(player1Label.getPreferredSize());
            add(player1Label);
            
            player2Label = new JLabel("<html><font color='blue' size='5'>Player 2</font></html>");
            player2Label.setSize(player1Label.getPreferredSize());
            // add(player2Label);
            
            //code for the image on the right
            backgroundImageLabel = new JLabel();
            backgroundImageLabel.setLocation(0, 0);
            backgroundImageLabel.setSize(100, 100);
            backgroundImageLabel.setSize(backgroundImageLabel.getPreferredSize());
            add (backgroundImageLabel);
            
            updateElements();
            resized();
        }
        
        void updateElements(){
            timeLabel.setLocation((getWidth() - timeLabel.getWidth()) / 2, gameIdLabel.getY());
            player1Label.setLocation(5, (getHeight() - (gameIdLabel.getY() + gameIdLabel.getHeight()) + player1Label.getHeight())/ 2);
            player2Label.setLocation(30, (getHeight() - (gameIdLabel.getY() + gameIdLabel.getHeight()) + player2Label.getHeight())/ 2);
        }
        void resized(){
            //image code
            int panelSize = (int) (Math.min(getHeight() , getWidth() - (timeLabel.getWidth() + 75) ) * 0.7);
            try{
               backgroundImageLabel.setIcon(ImageLoader.getResizedIcon("lightning_background.png", panelSize, panelSize)); 
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("ERROR");
            }
            backgroundImageLabel.setSize(backgroundImageLabel.getPreferredSize());
            backgroundImageLabel.setLocation(getWidth() - backgroundImageLabel.getWidth() - 25 , (getHeight() - backgroundImageLabel.getHeight())/2);
        }
    }
    private GUI gui;
    private JScrollPane scrollPane;
    private JPanel contentPanel;

    public PanelTest2(GUI pGui) {
        this.gui = pGui;

        scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setMinimumSize(new Dimension(0, 2000));
        scrollPane.setViewportView(contentPanel);

        addRedPanels(10); // Füge 5 rote Panels hinzu

        // Hinzufügen eines Komponentenadapters, um auf Größenänderungen zu reagieren
        gui.getFrame().getContentPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                resizeElements();
            }
        });
    }
    
    public void addRedPanels(int count) {
        for (int i = 0; i < count; i++) {
            // JPanel redPanel = new JPanel();
            // redPanel.setBackground(Color.RED);
            // redPanel.setPreferredSize(new Dimension(100, 100));
            // redPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Border um die roten Panels hinzufügen
            contentPanel.add(new SpectatePanel());
        }
        contentPanel.revalidate(); // Aktualisieren des Layouts
    }


    public String getDescription() {
        return "testPage2";
    }

    public void positionElements() {
        Component[] components = contentPanel.getComponents(); // Überprüfen Sie den Inhalt des contentPanel, nicht des JViewport
        for (Component component : components) {
            if (component instanceof JPanel) {
                SpectatePanel gamePanel = (SpectatePanel) component;
                gamePanel.updateElements();
            }
        }
    }

    public void resizeElements() {
        int frameWidth = gui.getFrame().getWidth();
        int frameHeight = gui.getFrame().getHeight();

        // Setzen der Größe des ScrollPane
        scrollPane.setBounds(100, 100, frameWidth - 200, frameHeight - 200);

        // Setzen der Größe des inneren Panels
        contentPanel.setPreferredSize(new Dimension(frameWidth - 200, frameHeight - 200));

        Timer timer = new Timer(100, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scrollPane.revalidate();
                scrollPane.repaint();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    //Erstellt mir chatgpt
    public ArrayList<JPanel> getAddedJPanels() {
        ArrayList<JPanel> addedPanels = new ArrayList<>();
        Component[] components = contentPanel.getComponents(); // Überprüfen Sie den Inhalt des contentPanel, nicht des JViewport
        for (Component component : components) {
            if (component instanceof JPanel) {
                addedPanels.add((JPanel) component);
            }
        }
        return addedPanels;
    }

    public void test(){
        ArrayList<JPanel> test = getAddedJPanels();
        for(int i = 0; i < test.size(); i++){
            System.out.println(test.get(i));
        }
        System.out.println(test.size());
    }

    public void reloadData() {}

    public void componentResized() {
        Component[] components = contentPanel.getComponents(); // Überprüfen Sie den Inhalt des contentPanel, nicht des JViewport
        for (Component component : components) {
            if (component instanceof JPanel) {
                SpectatePanel gamePanel = (SpectatePanel) component;
                // if (gamePanel.getHeight() < 200) {
                    // gamePanel.setSize(gamePanel.getWidth(), 200);
                // }
                gamePanel.resized();
            }
        }
    }
}
