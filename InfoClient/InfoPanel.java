package InfoClient;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class InfoPanel extends Page{
    private class GamePanel extends JPanel{
        JLabel gameLabel;
        JLabel imageLabel;
        GamePanel(){
            super();
            setLayout(null);
            
            gameLabel = new JLabel("Client 123 vs Client 432");
            gameLabel.setSize(gameLabel.getPreferredSize());
            add (gameLabel);
            
            imageLabel = new JLabel();
            imageLabel.setLocation(0, 0);
            imageLabel.setSize(100, 100);
            imageLabel.setSize(imageLabel.getPreferredSize());
            add (imageLabel);
        }
        
        @Override 
        public void setSize(int pX, int pY){
            super.setSize(pX, pY);
            
            gameLabel.setLocation(25, (getHeight() - gameLabel.getHeight()) / 2); 
            int panelSize = (int) (Math.min(getHeight() , getWidth() - (gameLabel.getWidth() + 75) ) * 0.7);
            try{
               imageLabel.setIcon(getResizedIcon("lightning_background.png", panelSize, panelSize)); 
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
    public InfoPanel(GUI pGUI){
        this.gui = pGUI;
        setLayout(null);
        
        gamePanel = new GamePanel();
        gamePanel.setBackground(Color.WHITE);
        add(gamePanel);
        
        testButton = new JButton("test");
        add (testButton);
    }
    
    public String getDescription(){
        return "InfoPanel";
    }
    public void positionElements(){
        testButton.setLocation(0, 0);
        gamePanel.setLocation(0, testButton.getHeight());
    }
    public void resizeElements(){
        int frameWidth = gui.getFrame().getWidth();
        int frameHeight = gui.getFrame().getHeight();
        
        testButton.setSize(testButton.getPreferredSize());
        gamePanel.setSize(frameWidth, frameHeight - testButton.getY() - 150);
    }
    public void reloadData(){
        
    }
    public void componentResized(){
        
    }
}