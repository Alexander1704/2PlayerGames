package testClient;

import assetLoader.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**Eine Ladeseiten mit einem Button, um diese wieder zu verlassen.
 * Wenn dieser Button gefrückt wird, wird ein übergebener Thread 
 * ausgeführt
 */
public class ExitLoadingPage extends LoadingPage{
    private JButton exitButton;
    private Thread leaveThread;
    
     /**Erstellt ein neues Objekt der Klasse ExitLoadingPage und initialiert dieses
     */
    public ExitLoadingPage(GUI pGUI){
        super(pGUI);
        
        exitButton = new JButton("Leave");
        exitButton.setBackground(new Color(255, 171, 25));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(FontLoader.loadFont("LilitaOne-Regular.ttf",25));
        exitButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        add(exitButton);
        
        exitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                synchronized(leaveThread){
                     leaveThread.run();
                }
            }
        });
    }
    
    @Override 
    public void start(){
        super.start();
        leaveThread = new Thread();
    }
    
    @Override 
    public void resized(){
        super.resized();
        
        //Skaliere Elemente
        exitButton.setSize(gui.getFrame().getWidth()/ 7, gui.getFrame().getWidth()/ 7 / 3);
        FontLoader.fitFont(exitButton);
        
        //Positioniere Elemente
        FunctionLoader.position(exitButton, 0.9, 0.95);
    }
    
    /**Setze den Thread, der ausgeführt wird, wenn auf den Button zum Verlassen der Page
     * gedrückt wird. Dieser Thread darf erst nach dem Wechseln zu dieser Page gesetzt 
     * werden.
     * 
     * @param pLeaveThread Thread, der ausgeführt wird, wenn auf den Button zum Verlassen
     *          der Page geklickt wird
     */
    public void setLeaveThread(Thread pLeaveThread){
        synchronized(leaveThread){
            this.leaveThread = pLeaveThread;
        }
    }
}