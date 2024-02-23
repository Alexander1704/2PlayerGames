package testClient;

public class UserClient extends Client{
    private boolean connected;
    private boolean inGame;
    private boolean processMessages;
    private GUI gui;
    public UserClient(GUI pGUI, String pIPAdresse, int pPort){
        super(pIPAdresse, pPort);
        this.gui = pGUI;
        connected = false;
        inGame = false;
        processMessages = true;
    }

    @Override
    public void send(String pMessage){
        // System.out.println("Sende Nachricht an Server: " + pMessage);
        super.send(pMessage);
    }

    public void processMessage(String pMessage){
        if(pMessage.charAt(0) == '+') {
            switch(pMessage){
                case "+SPIELER OK" ->{
                        this.connected = true;
                        // if(!processMessages)  return;
                        // gui.switchPage(gui.getMenuPage());
                    }
                case "+GAME FOUND" ->{
                        inGame = true;
                        if(! processMessages) send("GAME INIT " + "Sword Man");
                        if(! processMessages)  return;
                        gui.switchPage(gui.getGamePage());
                        send("GAME INIT " + gui.getMenuPage().getCharacter());
                    }
                case "+GAME CLOSED" ->{
                        inGame = false;
                        if(! processMessages)  return;
                        gui.switchPage(gui.getMenuPage());
                    }
            }
        }
        if(! processMessages) {
            // System.out.println("[Muted Client]" + pMessage);
            return;
        }

        String[] args = pMessage.split(" ");
        switch(args[0]){
            case "GAME" ->{
                    switch(args[1]){
                        case "STARTING" -> gui.getGamePage().setStarting(args[2]);
                        case "PLAYERNAME" -> gui.getGamePage().setPlayerName(Integer.parseInt(args[2]), args[3]);
                        case "ANIMATION" ->{
                                gui.getGamePage().setAnimation(Integer.parseInt(args[2]), Integer.parseInt(args[3])); 
                            }
                        case "POSITION" ->{
                                gui.getGamePage().setPosition(Integer.parseInt(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));                     
                            }
                        case "UPDATE" ->{
                                // gui.getGamePage().setPosition(Integer.parseInt(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]), Integer.parseInt(args[7]));

                                // gui..setPosition(Integer.parseInt(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));                        
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

    public boolean hasConnected(){
        return this.connected;
    }

    public boolean inGame(){
        return inGame;
    }

    public void setProcessingMessages(boolean b){
        processMessages = b;
    }

    public boolean processingMesssages(){
        return processMessages;
    }
}
