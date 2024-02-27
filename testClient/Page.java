package testClient;

import javax.swing.JPanel;

/**Die abstrakte Klasse Page erbt von einem JPanel und sorgt dafür, dass die Methoden
 * start, resized, update und finish implementiert werden müssen
 */

public abstract class Page extends JPanel{
    /**In dieser Methode können Daten einer Page beim Start aktualisiert werden.
     * Die start-Methode wird beim Wechseln zu dieser Page aufgerufen.
     */
    public abstract void start();
    
    /**In diese Methode können Arbeitsprozesse beendet werden, die das Programm verlangsamen
     * würden
     * 
     * Die Methode wird beim Wechseln zu einer neuen Page aufgerufen, wenn es eine aktuelle Page gibt.
     */
    public abstract void finish();
    
    /**In dieser Methode werden Elemente repositiert und skaliert.
     * Die resized-Methode wird bei einer Änderung der Größe des JFrames und damit der Page
     * aufgerufen.
     */
    public abstract void resized();
    
    /**In der update-Methode können Elemente geupdatet werden, die auch unabhängig
     * vom Ändern der Größe des JFrames aktualiert werden müssen.
     * 
     * Die update-Methode wird ticks * mal pro Sekunde von der GUI aufgerufen.
     */
    public abstract void update();
}
