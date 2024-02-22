package testClient;

import assetLoader.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class InfoPage extends Page{
    private class GamePanel extends JPanel{
        JLabel gameLabel;
        JLabel imageLabel;
        final int GAME_ID;
        GamePanel(int pGameId){
            super();
            setLayout(null);
            this.setBackground(Color.WHITE);
            GAME_ID = pGameId;
            
            gameLabel = new JLabel("Client 123 vs Client 432");
            gameLabel.setSize(gameLabel.getPreferredSize());
            add (gameLabel);
            
            imageLabel = new JLabel("Here your image is placed");
            imageLabel.setLocation(0, 0);
            imageLabel.setSize(100, 100);
            imageLabel.setSize(imageLabel.getPreferredSize());
            imageLabel.setVisible(false);
            add (imageLabel);
            
            
            this.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JOptionPane.showMessageDialog(null, "Panel " + GAME_ID + " wurde geklickt!");
                }
    
                @Override
                public void mousePressed(MouseEvent e) {}
    
                @Override
                public void mouseReleased(MouseEvent e) {}
    
                @Override
                public void mouseEntered(MouseEvent e) {}
    
                @Override
                public void mouseExited(MouseEvent e) {}
            });
        }
        
        @Override 
        public void setSize(int pX, int pY){
            super.setSize(pX, pY);
            
            gameLabel.setLocation(25, (getHeight() - gameLabel.getHeight()) / 2); 
            int panelSize = (int) (Math.min(getHeight() , getWidth() - (gameLabel.getWidth() + 75) ) * 0.7);
            try{
               imageLabel.setIcon(ImageLoader.getResizedIcon("lightning_background.png", panelSize, panelSize)); 
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("ERROR");
            }
            imageLabel.setSize(imageLabel.getPreferredSize());
            imageLabel.setLocation(getWidth() - imageLabel.getWidth() - 25 , (getHeight() - imageLabel.getHeight())/2);
            
            if(panelSize <0) imageLabel.setVisible(false);
            else imageLabel.setVisible(true);
        }
        public void update(){
            // setSize(getWidth(), getHeight());;
            gameLabel.setLocation(25, (getHeight() - gameLabel.getHeight()) / 2); 
            int panelSize = (int) (Math.min(getHeight() , getWidth() - (gameLabel.getWidth() + 75) ) * 0.7);
            try{
               imageLabel.setIcon(ImageLoader.getResizedIcon("lightning_background.png", panelSize, panelSize)); 
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("ERROR");
            }
            imageLabel.setSize(imageLabel.getPreferredSize());
            imageLabel.setLocation(getWidth() - imageLabel.getWidth() - 25 , (getHeight() - imageLabel.getHeight())/2);
            
            if(panelSize <0) imageLabel.setVisible(false);
            else imageLabel.setVisible(true);
        }
        
        private ImageIcon combineImageIcon(ImageIcon back, ImageIcon front, int startingX){
            Image img1 = back.getImage();
            Image img2 = front.getImage();
            
            BufferedImage resultImage = new BufferedImage(
            img1.getWidth(null), img1.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            
            Graphics2D g = resultImage.createGraphics();
            g.drawImage(img1, 0, 0, null);
            g.drawImage(img2, startingX, 0, null);
            g.dispose();
            
            return new ImageIcon(resultImage);
        }
    }
    private GUI gui;
    private ArrayList<JPanel> gamesList;
    private GamePanel gamePanel;
    private JButton testButton;
    private JScrollPane scrollPane;
    public InfoPage(GUI pGUI){
        this.gui = pGUI;
        setLayout(null);
        
        gamePanel = new GamePanel(0);
        gamePanel.setBackground(Color.WHITE);
        // add(gamePanel);
        
        testButton = new JButton("test");
        // add (testButton);
        
        
        scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        // Setze den EmptyBorder für die Abtrennung zwischen den Panels
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        for (int i = 0; i < 10; i++) {
            GamePanel panel = new GamePanel(i);
            contentPanel.add(panel);

            // Füge eine zusätzliche Abtrennung ein, außer beim letzten Panel
            if (i < 9) {
                contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        scrollPane.setViewportView(contentPanel);

        // add(scrollPane);
        // scrollPane.setLocation(0, 0);
        // scrollPane.setSize(100, 100);
        gui.getFrame().setLayout(new BorderLayout());
        gui.getFrame().add(scrollPane, BorderLayout.CENTER);
        gui.getFrame().setSize(400, 300);
        gui.getFrame().setLocationRelativeTo(null);
        gui.getFrame().setVisible(true);
    }
    
    public String getDescription(){
        return "InfoPanel";
    }
    public void positionElements(){
        // testButton.setLocation(0, 0);
        // gamePanel.setLocation(0, testButton.getHeight());
    }
    public void resizeElements(){
        // int frameWidth = gui.getFrame().getWidth();
        // int frameHeight = gui.getFrame().getHeight();
        
        // testButton.setSize(testButton.getPreferredSize());
        // gamePanel.setSize(frameWidth, frameHeight - testButton.getY() - 150);
        // scrollPane.setSize(frameWidth, frameHeight);
        // for(int i = 0; i < scrollPane.getComponentCount(); i++){
            // // GamePanel gP = (GamePanel) (scrollPane.getComponent(i));
            // // gP.update();
        // }
    }
    public void reloadData(){
        
    }
    public void componentResized(){
        
    }
    public void finish(){
        
    }
    public void updateElements(){
        
    }
}
