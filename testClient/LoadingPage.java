package testClient;

import assetLoader.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;

public class LoadingPage extends Page{
    private class Rectangle extends JPanel{
        int x,y,width,height;
        Rectangle(int x, int y, int width, int height){
            this.x = x;
            this.y = y; 
            this.width = width;
            this.height = height;
        }
        void setX(int x){
            this.x = x;
        }
        void setY(int y){
            this.y = y;
        }
        void setHeight(int height){
            this.height = height;
        }
        protected void paintComponent(Graphics g) {
            if(x < 70 || x > gui.getFrame().getWidth() - 80) return;
            super.paintComponent(g);
            // g.setColor(new Color(76, 151, 255));
            // g.setColor(new Color(103, 230, 101));
            g.setColor(new Color(93, 252, 153));
            g.setColor(Color.WHITE);
            g.fillRect(x, y, width, height);
        }
    }
        
    private GUI gui;
    private JLabel loadingLabel;
    private Rectangle[] loadingAnimation;
    public LoadingPage(GUI gui){
        this.gui = gui;
        setBackground(new Color(94, 144, 252));
        setLayout (null);
        
        loadingLabel = new JLabel ("loading");
        loadingLabel.setForeground(new Color(93, 252, 153)); // Set the text color to green
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf", 40)); // Set the font to plain monospaced
        loadingLabel.setBounds(0, 0, 0, 0);
        add (loadingLabel);
        
        loadingAnimation = new Rectangle[(int) ((gui.getScreenSize().getWidth())/20)];
        for (int i = 0; i < loadingAnimation.length; i++){
            loadingAnimation[i] = new Rectangle(i*20 ,0, 15, 200);
            add (loadingAnimation[i]);
        }
        
        Thread updateLoadingLabel = new Thread(new Runnable() {
            @Override
            public void run() {
                updateLoadingLabel();
            }
        });
        // updateLoadingLabel.start();
    }
    private void updateLoadingLabel(){
        int start = 0;
        while(true){
            if(start == 0) loadingLabel.setText("loading");
            if(start == 1) loadingLabel.setText("loading.");
            if(start == 2) loadingLabel.setText("loading..");
            if(start == 3) loadingLabel.setText("loading...");
            positionElements();
            try{
                Thread.sleep(900);
            }
            catch (InterruptedException ie){
                ie.printStackTrace();
            }
            start++;
            if(start > 3) start = 0;
        }
    }
    public String getDescription(){
        return "loadingPage";
    }
    public void reloadData(){
        
    }
    public void componentResized(){
        FunctionLoader.warte(10);
    }
    public void positionElements(){
        int loadingWidth = (int) loadingLabel.getPreferredSize().getWidth();
        int loadingHeight = (int) loadingLabel.getPreferredSize().getHeight();
        loadingLabel.setBounds((gui.getFrame().getWidth() - loadingWidth)/ 2, (gui.getFrame().getHeight()-loadingHeight)/ 2, loadingWidth, loadingHeight);
    }
    public void paintComponent(Graphics g) {
        positionElements();
        super.paintComponent(g);
        int rectangles = (int) ((gui.getFrame().getWidth())/20.0);
        double currentRect = gui.getFrame().getWidth() * System.currentTimeMillis()/ 100000.0 % (rectangles * 2);
        if(currentRect > rectangles) currentRect = rectangles - (currentRect - rectangles);
        for(int i = 0; i < loadingAnimation.length; i++){
            loadingAnimation[i].setHeight((int) (gui.getFrame().getHeight()/ 4.0 * (Math.abs(i-currentRect) / rectangles)) + 10);
            loadingAnimation[i].setY(gui.getFrame().getHeight() - loadingAnimation[i].height - 40 + 1 );
            loadingAnimation[i].paintComponent(g);
        }
    }
    
    public void keyPressed(KeyEvent e){
        
    }
    public void resizeElements(){
        
    }
}
