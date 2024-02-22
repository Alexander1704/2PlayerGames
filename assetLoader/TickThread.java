package assetLoader;

/**Ein TickThread ist ein Thread, der die Methode start() eines Threads überschreibt, sodass wenn ein TickThread gestartet 
 * wird,dieser Thread solange aufgerufen wird, solange dieser nicht mit der Methode finish() beendet wurde. Der
 * Thread läuft auf einer übergebenen Tickanzahl (int)
 */
public class TickThread extends Thread{
    private int ticks;
    private boolean running;

    /**Konstruktor, der einen TickThread erstellt
     * Standardmäßig werden die ticks auf 60 gesetzt
     */
    public TickThread(Runnable pRunnable){
        this(60, pRunnable);
    }

    /**Konstruktor, der einen TickThread erstellt 
     * und die ticks auf die übergebene Anzahl pTick setzt
     * 
     * @param pTick setzt die ticks eines TickThreads
     */
    public TickThread(int pTick, Runnable pRunnable){
        super(pRunnable);
        ticks = pTick;
        running = false;
    }

    /**Setter für die ticks
     * 
     * @param pTick setzt die ticks eines TickThreads
     */
    public void setTick(int pTick){
        this.ticks = pTick;
    }
    
    /**Getter für die Variable tick
     */
    public int getTick(){
        return this.ticks;
    }

    /**Beendet die Aufgabe des TickThreads, sofern dieser gestartet worden ist 
     */
    public void finish(){
        this.running = false;
    }

    /**Diese Methode überschreibt die run()-Methode eines Threads.
     * Diese run-Methode wird ticks mal pro Sekunde aufgerufen und läuft bis der Thread 
     * durch die Methode finish() beendet wird
     * 
     * Stack Overflow
     */
    @Override 
    public void run(){
        if(running) return;
        running = true;

        long lastLoopTime = System.nanoTime();
        while (running) {
            long currentTime = System.nanoTime();
            long elapsedTime = currentTime - lastLoopTime;
            lastLoopTime = currentTime;

            super.run();

            // Calculate time to sleep to maintain desired tick
            long waitTime = 1000000000 / ticks;
            long sleepTime = waitTime - elapsedTime;

            try {
                Thread.sleep(Math.max(0, sleepTime / 1000000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}