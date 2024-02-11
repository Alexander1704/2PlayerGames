package testClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class PanelTest {
    private class GamePanel extends JPanel{
        JLabel gameLabel;
        JLabel imageLabel;
        final int GAME_ID;
        GamePanel(int pGameId){
            super();
            setLayout(null);
            
            GAME_ID = pGameId;
            
            gameLabel = new JLabel("Client 123 vs Client 432");
            gameLabel.setSize(gameLabel.getPreferredSize());
            add (gameLabel);
            
            imageLabel = new JLabel();
            imageLabel.setLocation(0, 0);
            imageLabel.setSize(100, 100);
            imageLabel.setSize(imageLabel.getPreferredSize());
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

    public PanelTest() {
        JFrame frame = new JFrame("Clickable Panels with Separation Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        
        // Setze den EmptyBorder für die Abtrennung zwischen den Panels
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        for (int i = 0; i < 10; i++) {
            JPanel panel = createClickablePanel((i));
            contentPanel.add(panel);

            // Füge eine zusätzliche Abtrennung ein, außer beim letzten Panel
            if (i < 9) {
                contentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        scrollPane.setViewportView(contentPanel);

        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createClickablePanel(int pGameId) {
        GamePanel panel = new GamePanel(pGameId);
        panel.setPreferredSize(new Dimension(300, 50));
        panel.setBackground(Color.WHITE);
        panel.setSize(panel.getPreferredSize());

        

        return panel;
    }
    
    public ImageIcon getResizedIcon(String path, int x_scale, int y_scale) throws IOException {
            // Lade das Bild von der Datei
            Image img = ImageIO.read(new File("assets/" + path));
            // Skaliere das Bild auf die Größe des JLabels
            Image scaledImg = img.getScaledInstance(x_scale, y_scale, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
    }
}
