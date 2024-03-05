package testClient;

/**Der UserClient erhält Nachrichten vom Server und leitet diese an 
 * passende Teile des Programms weiter
 * 
 * Außerdem können über den UserClient Nachrichten an der Server gesendet
 * werden.
 */
public class UserClient extends Client{
    //Deklaration der Objekte
    private boolean connected;
    private boolean inGame;
    private boolean processMessages;
    private GUI gui;

    /**Erstellt ein neues Objekt der Klasse UserClient und initialiert dieses
     * 
     * @param pGUI Parameter, damit die Nachrichten vom Server von der GUI umgesetzt werden
     * @param pIPAdresse IP-Adresse des Servers, der mit dem Client verbunden wird
     * @param pPort Port des Servers, der mit dem Client verbunden wird
     */
    public UserClient(GUI pGUI, String pIPAdresse, int pPort){
        super(pIPAdresse, pPort);
        this.gui = pGUI;

        connected = false;  //UserClient ist anfangs noch nicht mit Server verbunden
        inGame = false;     //UserClient ist anfangs in keinem Spiel 
        processMessages = true;     //UserClient leitet standardmäßig die Nachrichten an die GUI weiter
    }

    /**Sendet eine Nachricht an den Server
     * 
     * @param pMessage Nachricht, die an den Server geht
     */
    @Override
    public void send(String pMessage){
        super.send(pMessage);
    }

    /**UserClient erhält Nachricht vom Server und verwertet diese, indem es diese an passende Teile
     * des Programms schickt
     */
    public void processMessage(String pMessage){
        //Rückmeldungen des Servers
        if(pMessage.charAt(0) == '+') {
            switch(pMessage){
                case "+SPIELER OK" ->{
                        this.connected = true;
                    }
                case "+GAME FOUND" ->{
                        inGame = true;
                        if(! processMessages) {     //Wenn ein UserClient keine Nachrichten verarbeitet, dann setze Spieler auf "Player"
                            send("GAME INIT " + "Player");
                            return;
                        }
                        gui.switchPage(gui.getGamePage());
                        send("GAME INIT " + gui.getMenuPage().getCharacter());
                    }
                case "+GAME CLOSED" ->{
                        inGame = false;
                        if(! processMessages)  return;
                        gui.switchPage(gui.getMenuPage());
                    }
            }
            if(processMessages && pMessage.startsWith("+OK SETNAME")){
                String[] args = pMessage.split(" ", 3);
                gui.getLoginPage().setPlayerName(args[2]);
                gui.getMenuPage().setPlayerName(args[2]);
            }
            return;
        }
        if(pMessage.startsWith("-ERR")){
            String[] args = pMessage.split(" ", 2);
            System.err.println("ClientError occured: " + args[1]);
        }
        if(! processMessages) {
            return;
        }

        // if(!pMessage.startsWith("GAME POSITION")) System.out.println("[Client] received: " + pMessage);

        //Verwerte GUI-relevante Nachrichten
        String[] args = pMessage.split(" ");
        switch(args[0]){
            case "GAME" ->{
                    switch(args[1]){
                        case "STARTING" -> {
                                // System.out.println("starting in " + args[2]);
                                gui.getGamePage().setStarting(args[2]);
                            }
                        case "PLAYERNAME" -> {
                                args = pMessage.split(" ", 4);
                                gui.getGamePage().setPlayerName(Integer.parseInt(args[2]), args[3]);
                            }
                        case "ANIMATION" ->{
                                gui.getGamePage().setAnimation(Integer.parseInt(args[2]), Integer.parseInt(args[3])); 
                            }
                        case "POSITION" ->{
                                gui.getGamePage().setPosition(Integer.parseInt(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));                     
                            }
                        case "UPDATE" ->{
                                gui.getGamePage().setPosition(Integer.parseInt(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));
                                gui.getGamePage().setHealth(Integer.parseInt(args[2]), Integer.parseInt(args[5]));
                                gui.getGamePage().witchPlayer(Integer.parseInt(args[2]), Boolean.parseBoolean(args[6]));
                                gui.getGamePage().setAnimation(Integer.parseInt(args[2]), Integer.parseInt(args[7]));                   
                            }
                        case "INIT" -> {
                                if(args.length >= 5) {
                                    gui.getGamePage().setCharacter(Integer.parseInt(args[2]), args[3] + " " + args[4]);
                                    return;
                                }
                                gui.getGamePage().setCharacter(Integer.parseInt(args[2]), args[3]);
                            }
                        case "MAP" -> {
                                gui.getGamePage().setMap(Integer.parseInt(args[2]));
                            }
                        case "WITCH" -> {
                                gui.getGamePage().witchPlayer(Integer.parseInt(args[2]), (Boolean.parseBoolean(args[3])));
                            }

                        case "HEALTH" -> {
                                gui.getGamePage().setHealth(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                            }
                        case "RIGHTSIDED" -> {
                                gui.getGamePage().setRightSided(Integer.parseInt(args[2]), Boolean.parseBoolean(args[3]));
                            }
                        case "PLAYERNUM" -> {
                                gui.getGamePage().setYourPlayer(Integer.parseInt(args[2]));
                            }
                        case "BULLET" -> {
                                switch (args[2]){
                                    case "NEW" ->{
                                            gui.getGamePage().addBullet(Integer.parseInt(args[3]) , args[6], Double.parseDouble(args[4]), Double.parseDouble(args[5]), Boolean.parseBoolean(args[8]));
                                        }
                                    case "UPDATE" ->{
                                            gui.getGamePage().updateBullet(Integer.parseInt(args[3]) , args[6], Double.parseDouble(args[4]), Double.parseDouble(args[5]));
                                        }
                                    case "REMOVE" ->{
                                            gui.getGamePage().removeBullet(Integer.parseInt(args[3]));
                                        }
                                }
                            }
                        case "CLOSING" -> {
                                gui.getGamePage().closingGame(args[2]);
                            }
                        case "WON" -> {
                                gui.getGamePage().gameWon(Boolean.parseBoolean(args[2]));
                            }
                        default -> System.out.println("ERR in client: function " + args[1] + " could not be found");
                    }
                }
        }
    }

    /**Gibt zurück, ob der UserClient mit dem Server verbunden ist
     * 
     * @return connected
     */
    public boolean hasConnected(){
        return this.connected;
    }

    /**Gibt zurück, ob sich der UserClient gerade in einem Spiel befindet
     * 
     * @return inGame
     */
    public boolean inGame(){
        return this.inGame;
    }

    /**Setzt, ob der UserClient Nachrichten an die GUI weiter leiten soll oder 
     * nicht
     * 
     * @param pProcessMessages Boolean-Wert, ob Nachrichten verarbeitet werden sollen
     */
    public void setProcessingMessages(boolean pProcessMessages){
        this.processMessages = pProcessMessages;
    }

    /**Gibt zurück, ob der UserClient Nachrichten verarbeitet
     * 
     * @return processMessages
     */
    public boolean processingMesssages(){
        return this.processMessages;
    }
}
