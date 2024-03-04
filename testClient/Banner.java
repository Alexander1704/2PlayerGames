package testClient;

import assetLoader.*;
import javax.swing.*;
import java.awt.*;

/**Erstellt ein Banner mit einem Titel. Wenn die Größe geändert wird, muss das Banner auch
 * durch die Methode resized() aktualisiert werden, damit es sich an die Größe anpasst.
 */
public class Banner extends JPanel{
    private JLabel bannerBackground; 
    private JLabel bannerPlayerName;
    private boolean redDesign;
    
    /**Erstellt ein neues Objekt der Klasse Banner und initialiert dieses
     */
    public Banner(){
        setLayout(null);
        setOpaque(false);
        setSize(100, 100);
        redDesign = true;

        bannerBackground = new JLabel();
        bannerBackground.setLocation(0, 0);
        bannerBackground.setBackground(Color.BLUE);
        add(bannerBackground);
        
        bannerPlayerName = new JLabel();
        bannerPlayerName.setForeground(Color.WHITE);
        bannerPlayerName.setFont(FontLoader.loadFont("LilitaOne-Regular.ttf", 20));
        bannerPlayerName.setSize(bannerPlayerName.getPreferredSize());
        bannerPlayerName.setLocation(0, 0);
        add(bannerPlayerName);
        setComponentZOrder(bannerPlayerName, 0);
    }

    /**Passe das Banner an die momentane Größe des Banners an
     */
    public void resized(){
        bannerBackground.setSize(getSize());
        try{
            if(redDesign) ImageLoader.fitImage(bannerBackground, "banner_red.png"); 
            else ImageLoader.fitImage(bannerBackground, "banner_blue.png"); 
        }catch (Exception e){
            e.printStackTrace();
        }
        bannerBackground.setSize(bannerBackground.getPreferredSize());
        
        //0.25 bis 0.95
        bannerPlayerName.setSize((int) (bannerBackground.getWidth() * 0.7), (int) (bannerBackground.getHeight() * 0.4));
        FontLoader.fitFont(bannerPlayerName);
        bannerPlayerName.setSize(bannerPlayerName.getPreferredSize());
        bannerPlayerName.setLocation( (int) ((bannerBackground.getWidth() - bannerPlayerName.getWidth()) * 0.75) , (bannerBackground.getHeight() - bannerPlayerName.getHeight()) / 2);
    }
    
    /**Setze den Titel des Banners und aktualisiere das Banner
     * 
     * @param pTitle Titel des Banners, der angezeigt wird
     */
    public void setTitle(String pTitle){
        bannerPlayerName.setText(pTitle);
        resized();
    }

    /**Setze die Designart des Banners
     * 
     * @param pRedDesign setze das Design (true: rotes Design, false: blaues Design)
     */
    public void setDesign(boolean pRedDesign){
        if(pRedDesign != redDesign){
            switchDesign();
        }
    }
    
    /**Wechlse das Design, wenn das Banner gerade im roten Design ist, wechsle zum 
     * Blauen und andersherum. Außerdem wird das Banner aktualisiert
     */
    public void switchDesign(){
        redDesign = !redDesign;
        resized();
    }
    
    /**Gibt das Design zurück. Wenn das banner im roten Design ist, wird true, wenn 
     * es im blauen Design ist, wird false zurückgegeben.
     * 
     * @return ist das Banner im roten Design
     */
    public boolean getDesign(){
        return redDesign;
    }
}
