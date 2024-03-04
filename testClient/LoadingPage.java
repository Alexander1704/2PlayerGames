package testClient;

import assetLoader.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;

/**Ladeseite, die augerufen wird, wenn es Prozesse gibt, die länger brauchen
 */
public class LoadingPage extends Page{
    /**Ein Rectangle stellt einen Ladebalken der Ladeanimation dar
     */
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
        public void paintComponent(Graphics g) {
            if(x < 70 || x > gui.getFrame().getWidth() - 80) return;
            super.paintComponent(g);
            g.setColor(new Color(93, 252, 153));
            g.setColor(Color.WHITE);
            g.fillRect(x, y, width, height);
        }
    }
        
    protected GUI gui;
    private JLabel loadingLabel;
    private Rectangle[] loadingAnimation;
    private long lastUpdate;
    
    /**Erstellt ein neues Objekt der Klasse LoadingPage und initialiert dieses
     */
    public LoadingPage(GUI gui){
        this.gui = gui;
        setBackground(new Color(94, 144, 252));
        setLayout (null);
        
        loadingLabel = new JLabel ("loading");
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setFont(FontLoader.loadFont("LilitaOne-Regular.ttf", 40)); // Set the font to plain monospaced
        loadingLabel.setSize(loadingLabel.getPreferredSize());
        add (loadingLabel);
        
        loadingAnimation = new Rectangle[(int) ((gui.getScreenSize().getWidth())/20)];
        for (int i = 0; i < loadingAnimation.length; i++){
            loadingAnimation[i] = new Rectangle(i*20 ,0, 15, 200);
            add (loadingAnimation[i]);
        }
    }
    
    @Override
    public void start(){}
    
    @Override
    public void finish(){}
    
    @Override
    public void resized(){
        FunctionLoader.position(loadingLabel, 0.5, 0.5);
    }
    
    @Override
    public void update(){
        repaint();
    }
    
    /**Überschreibe die paintComponent-Methode, um die Ladeanimation zu malen
     * 
     * @param g Graphik Objekt, auf dem die Ladeanimation gemalt wird
     */
    @Override
    public void paintComponent(Graphics g) {
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
}
