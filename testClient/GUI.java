package testClient;

import assetLoader.*;
import database.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**Ein Objekt der Klasse GUI erstellt ein neues JFrame und verwaltet den UserClient
 * und alle Pages 
 * 
 * Außerdem erbt die GUI von einem KeyListener, sodass Tastenanschläge an die 
 * zugehörigen Pages weitergeleitet werden können, wenn die Pages diese verarbeiten 
 * können.
 */
public class GUI implements KeyListener{
    //Deklaration der Objekte
    private JFrame frame;
    ServerStart server = new ServerStart();
    private TickThread mainThread;
    private DBController dbc;
    private UserClient userClient;

    private Page currentPage;
    private LoginPage loginPage;
    private MenuPage menuPage;
    private LoadingPage loadingPage;
    private GamePage gamePage;
    private ErrorPage errorPage;
    private InfoPage infoPage;
    private CreditsPage creditsPage;
    private ExitLoadingPage exitLoadingPage;

    /**Erstellt ein neues Objekt der Klasse GUI und initialiert dieses
     */
    public GUI(){
        //Erstellt neues JFrame mit übergebenen Eigenschaften
        System.out.println("Sys start");
        frame = new JFrame("2PlayerGames");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setLocationRelativeTo(null);
        frame.pack();

        //Fügt KeyListener zu JFrame hinzu
        frame.addKeyListener(this);
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(true);

        //Initialisierung der Objekte
        dbc = new DBController();
        loginPage = new LoginPage(this);
        menuPage = new MenuPage(this);
        loadingPage = new LoadingPage(this);
        gamePage = new GamePage(this);
        errorPage = new ErrorPage(this);
        creditsPage = new CreditsPage(this);
        exitLoadingPage = new ExitLoadingPage(this);

        //Wechsle zur LoginPage
        switchPage(loginPage);

        //Rufe die Methode update() der aktuellen Page in einem 60 Tick pro Sekunde Intervall auf
        mainThread = new TickThread(60, new Runnable(){
                @Override
                public void run(){
                    if(currentPage != null) currentPage.update();                
                }
            });
        mainThread.start();

        //Wenn die Größe des Frames geändert wird, rufe die Methode resized() der aktuellen Page auf
        frame.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    if(currentPage != null) currentPage.resized();
                }
            });

        //Wenn das Programm geschlossen wird, rufe die finish-Methode der aktuellen Page auf
        frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    if(currentPage != null) currentPage.finish();
                    System.out.println("Fenster wird geschlossen.");
                }
            });
    }

    /**Gibt die Tickanzahl des main-Threads wieder
     * 
     * @return Tickanzahl des main-Threads
     */
    public int getTick(){
        if(mainThread == null) return 30;
        return mainThread.getTick();
    }

    /**Setzt die Tickanzahl des main-Threads auf pTick
     * 
     * @param pTick Parameter, um die Tickanzahl des main-Threads zu setzen
     */
    public void setTick(int pTick){
        mainThread.setTick(pTick);
    }

    /**Gibt das JFrame frame zurück, auf dem die aktuelle Page hinzugefügt wird
     * 
     * @return frame
     */
    public JFrame getFrame(){
        return frame;
    }

    /**Gibt die aktuelle Page zurück
     * 
     * @return currentPage
     */
    public Page getCurrentPage(){ 
        return currentPage;
    }

    /**Gibt den DatabaseController für SQL-Abfragen zurück
     * 
     * @return dbc
     */
    public DBController getDBController(){
        return dbc;
    }
    
    /**Gibt die LoginPage zurück
     * 
     * @return loginPage
     */
    public LoginPage getLoginPage(){
        return loginPage;
    }

    /**Gibt die MenuPage zurück
     * 
     * @return menuPage
     */
    public MenuPage getMenuPage(){
        return menuPage;
    }

    /**Gibt die LoadingPage zurück
     * 
     * @return loadingPage
     */
    public LoadingPage getLoadingPage(){
        return loadingPage;
    }

    /**Gibt die ExitLoadingPage zurück
     * 
     * @return exitLoadingPage
     */
    public ExitLoadingPage getExitLoadingPage(){
        return exitLoadingPage;
    }

    /**Gibt die GamePage zurück
     * 
     * @return gamePage
     */
    public GamePage getGamePage(){
        return gamePage;
    }

    /**Gibt die ErrorPage zurück
     * 
     * @return errorPage
     */
    public ErrorPage getErrorPage(){
        return errorPage;
    }

    /**Gibt die CreditsPage zurück
     * 
     * @return creditsPage
     */
    public CreditsPage getCreditsPage(){
        return creditsPage;
    }

    /**Gibt den UserClient zurück
     * 
     * @return userClient
     */
    public UserClient getUserClient(){
        return userClient;
    }
    
    /**Initialisert den UserClient
     * 
     * @param pIP, setzt die IP des Servers, an den der UserClient senden soll
     * @param pPort setzt den Port des Servers, an den der UserClient senden soll
     */
    public void setUserClient(String pIP, int pPort){
        userClient = new UserClient(this, pIP, pPort);
    }

    /**Gibt die Größe des gesamte Screens des Computers als Dimension zurück
     * 
     * @return gesamte Größe des Screens des Computers
     */
    public Dimension getScreenSize(){
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**Wechsle von der aktuellen Page zu der übergebene Page pTo
     * 
     * @param pTo page, zu der gewechselt werden soll
     */
    public void switchPage(Page pTo){
        if (currentPage != null) {
            frame.remove(currentPage);
            currentPage.finish();
        }
        frame.add(pTo);
        currentPage = pTo;
        frame.validate();
        if(currentPage != null){
            currentPage.start();
            currentPage.resized();
        } 
        frame.repaint();
    }

    /**Wenn eine Taste gedrückt wird und die aktuelle Page diesen Input auch verwerten kann
     * (von KeyListener erbt), dann rufe die Methode keyPressed der Page auf
     * 
     * @param e gedrückte Taste
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if(currentPage instanceof KeyListener){
            KeyListener keyListener = (KeyListener) currentPage;
            keyListener.keyPressed(e);
        }
    }

    /**Wenn eine Taste gedrückt wird und die aktuelle Page diesen Input auch verwerten kann
     * (von KeyListener erbt), dann rufe die Methode keyTyped der Page auf
     * 
     * @param e Buchstabe der Taste
     */
    @Override
    public void keyTyped(KeyEvent e) {
        if(currentPage instanceof KeyListener){
            KeyListener keyListener = (KeyListener) currentPage;
            keyListener.keyTyped(e);
        }
    }

    /**Wenn eine Taste nicht mehr gedrückt wird und die aktuelle Page diesen Input auch verwerten kann
     * (von KeyListener erbt), dann rufe die Methode keyReleased der Page auf
     * 
     * @param e Taste, die nicht mehr gedrückt wird
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if(currentPage instanceof KeyListener){
            KeyListener keyListener = (KeyListener) currentPage;
            keyListener.keyReleased(e);
        }
    }

    /**Main Methode zum Starten des Projekts
     * 
     * Erstellt ein neues Objekt der Klasse GUI
     */
    public static void main (String[] args) {
        new GUI();
    }
}
