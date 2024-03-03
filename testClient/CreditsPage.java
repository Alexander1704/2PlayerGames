package testClient;

import assetLoader.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;

/**Page, in der die mitarbeitenden Personen aufgelistet wird. Nachdem alle Personen angezeigt
 * worden sind oder die Präsentation durch einen Mausklick beendet worden ist, wird zur
 * MenuPage zurückgekehrt
 */
public class CreditsPage extends Page{
    private class Eintrag{
        String arbeit;
        String name;
        Eintrag(String pArbeit, String pName){
            arbeit = pArbeit;
            name = pName;
        }
    }

    private GUI gui;
    private ArrayList<Eintrag> eintraege;
    private JLabel arbeitLabel;
    private JLabel nameLabel;
    private Thread changeEintragThread;
    private volatile boolean changeEintragRunning;
    
     /**Erstellt ein neues Objekt der Klasse CreditPage und initialiert dieses
     */
    public CreditsPage(GUI pGUI){
        gui = pGUI;
        setLayout(null);
        setBackground(Color.BLACK);

        //Füge neue Eintrage hinzu
        eintraege = new ArrayList<Eintrag>();
        eintraege.add(new Eintrag("Coding", "Leonard Meidt"));
        eintraege.add(new Eintrag("Coding", "Alexander Meidt"));
        eintraege.add(new Eintrag("Graphics", "Leonard Meidt"));
        eintraege.add(new Eintrag("Graphics", "Alexander Meidt"));
        eintraege.add(new Eintrag("Sounds", "Tobias Steuer"));

        //initialisiere Elemente der Page
        arbeitLabel = new JLabel("ARBEIT");
        arbeitLabel.setForeground(Color.YELLOW);
        arbeitLabel.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        add(arbeitLabel);

        nameLabel = new JLabel("NAME");
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(FontLoader.loadFont("assets/LilitaOne-Regular.ttf",25));
        add(nameLabel);

        changeEintragRunning = false;

        //Wenn die Maustaste gefrückt wird, dann wird zur MenuPage zurückgekehrt
        addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    gui.switchPage(gui.getMenuPage());
                }
            });
    }

    @Override
    public void start(){
        //starte einen neuen Thread, um die einzelnen CreditsEinträge zu wechseln
        changeEintragThread = new Thread(new Runnable(){
                @Override
                public void run(){
                    int currentEintrag = 0;
                    changeEintragRunning = true;
                    while(changeEintragRunning && currentEintrag < eintraege.size() ){
                        arbeitLabel.setText(eintraege.get(currentEintrag).arbeit);
                        nameLabel.setText(eintraege.get(currentEintrag).name);
                        resized();

                        try{
                            Thread.sleep(2000);
                        }catch (Exception e){
                            //Thread wurde beendet, da zu einer anderen Page gewechselt worden ist
                        }                        
                        currentEintrag++;
                    }
                    if(changeEintragRunning){
                        gui.switchPage(gui.getMenuPage());
                    }
                }
            });
        changeEintragThread.start();
    }

    @Override
    public void finish() {
        // Beende den Thread, der die einzelnen CreditEinträge wechselt
        if (changeEintragThread != null) {
            changeEintragRunning = false;
            changeEintragThread.interrupt();
        }
    }

    @Override
    public void resized(){
        //Skaliere Elemente
        FunctionLoader.scale(arbeitLabel, 0.5, 0.1);
        FontLoader.fitFont(arbeitLabel);
        arbeitLabel.setSize(arbeitLabel.getPreferredSize());

        FunctionLoader.scale(nameLabel, 0.5, 0.1);
        FontLoader.fitFont(nameLabel);
        nameLabel.setSize(nameLabel.getPreferredSize());

        //Positioniere Elemente
        FunctionLoader.position(arbeitLabel, 0.5, 0.5);
        arbeitLabel.setLocation(arbeitLabel.getX(), arbeitLabel.getY() - gui.getFrame().getHeight() / 20);

        FunctionLoader.position(nameLabel, 0.5, 0.5);
        nameLabel.setLocation(nameLabel.getX(), nameLabel.getY() + gui.getFrame().getHeight() / 20);
    }

    @Override
    public void update(){
    
    }
}