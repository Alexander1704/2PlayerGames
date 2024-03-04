package server;

import assetLoader.*;
import serverGame.*;
import java.util.ArrayList;

public class Game implements MessageInterpreter{ 
    private BroadcastServer server;
    String gameState;
    Client[] clients;
    ArrayList<Client> viewer;
    GameFrame gameFrame;
    boolean closed, updatingThread;
    Game(BroadcastServer pServer, Client clientA, Client clientB){
        this.server = pServer;
        closed = false;
        updatingThread = false;

        clients = new Client[2];
        clients[0] = clientA;
        clients[1] = clientB;
        
        for(int i = 0; i < clients.length; i++){
            clients[i].setGame(this);
        }

        viewer = new ArrayList<Client>();
        gameFrame = new GameFrame(this);

        gameState = "INIT";

        Thread initThread = new Thread(new Runnable(){
                    @Override
                    public void run(){
                        sendMessage("GAME PLAYERNAME 0 " + clientA.getName());
                        sendMessage("GAME PLAYERNAME 1 " + clientB.getName());
                        while(! gameFrame.isInitialized()){
                            FunctionLoader.warte(10);
                        }
                        
                        for(int i = 0; i < clients.length; i++){
                            sendMessage("GAME POSITION " + i + " " + gameFrame.getPlayer(1 + i).getPosition());
                        }
 
                        gameState = "STARTING";
                        for(int i = 3; i > -1; i--){
                            sendMessage("GAME STARTING " + i);
                            if(i != 0) FunctionLoader.warte(2000); 
                        }
                        gameState = "GAME";
                        gameFrame.start();

                        TickThread updatePositionThread = new TickThread(60, new Runnable(){ 
                                    @Override 
                                    public void run(){ 
                                        for(int i = 0; i < clients.length; i++){
                                            // if(gameFrame.getPlayer(1 + i).isMoving() || true) sendMessage("GAME POSITION " + i + " " + gameFrame.getPlayer(1 + i).getPosition());
                                            sendMessage("GAME POSITION " + i + " " + gameFrame.getPlayer(1 + i).getPosition());
                                        }
                                    }
                                });
                        updatePositionThread.start();
                    }
                });
        initThread.start();
    }

    Game(BroadcastServer pServer, Client[] clients){ 
        this(pServer, clients[0], clients[1]);
    }

    public void userInput(Client a, String input){
        if(gameState.equals("GAME") || (!input.contains("ability") || gameState.equals("CLOSE_COUNTDOWN"))) {
            int index = getIndex(clients, a);
            gameFrame.getUserInput(index, input); 
        }
    }

    public void sendMessage(String pMessage){
        if(!closed){
            for(int i = 0; i < clients.length; i++){
                if(clients[i] != null) send (clients[i], pMessage);
            }
        }
    }

    public void addViewer(Client a){
        if(! viewer.contains(a)) viewer.add(a);
    }

    public int clientNum(Client a){
        for(int i = 0; i < clients.length; i++){
            if(clients[i].equals(a)) return i;
        }
        return -1;

    }

    public void setPlayer(Client a, String character){
        for(int i = 0; i < clients.length; i++){
            if(clients[i] != null && clients[i].equals(a)) {
                sendMessage("GAME INIT " + i + " " + character);
                send(a, "GAME PLAYERNUM " + i);
                gameFrame.setPlayer(i, character);
                return;
            }
        }
    }

    public void exit(Client a){
        for(int i = 0; i < clients.length; i++){
            if(clients[i] != null && clients[i].equals(a)) clients[i] = null;
        }

        if(isEmpty()) {
            closeThisGame();
            return;
        }

        Thread closeServerThread = new Thread(new Runnable() {
                    public void run(){
                        closeCountDown();
                    }
                });
        closeServerThread.start();
    }

    public void closeThisGame(){
        for(int i = 0; i < clients.length; i++){
            clients[i].setGame(null);
        }
        gameFrame.dispose();
        closed = true;
        server.closeGame(this);
    }

    private void closeCountDown(){
        if(gameState.equals("CLOSING"))return;
        gameState = "CLOSING";
        for(int i = 11; i >= 0; i--){
            try{
                sendMessage("GAME CLOSING " + i);
                Thread.sleep(1000);  // wait for 1 s
            }
            catch (InterruptedException ie){
                ie.printStackTrace();
            }
        }
        gameState = "CLOSED";
        sendMessage("+GAME CLOSED");
        if(!closed) closeThisGame();
    }

    public boolean isEmpty(){
        for(int i = 0; i < clients.length; i++){
            if(clients[i] != null) return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(o instanceof Client){
            Client cl = (Client) o;
            for(int i = 0; i < clients.length; i++){
                if(cl.equals(clients[i])) return true;
            }
            return false;
        }
        if(o instanceof Game){
            Game ga = (Game) o;
            for(int i = 0; i< clients.length; i++){
                if(ga.equals(clients[i])) return true;
            }
            return false;
        }
        return false;
    }

    @Override 
    public void interpretMessage(String str){
        if(!closed  && str.startsWith("HEALTH")){
            if(! gameState.equals("GAME")) return; 
            String[] temp = str.split(" ");
            if(Integer.parseInt(temp[2]) <= 0){
                gameState = "END_GAME";
                Thread closeServer = new Thread(new Runnable(){
                            public void run(){
                                if(temp[1].equals("0")){
                                    send(clients[0], "GAME WON false");
                                    send(clients[1], "GAME WON true");
                                }else if (temp[1].equals("1")){
                                    send(clients[0], "GAME WON true");
                                    send(clients[1], "GAME WON false");
                                }
                                closeCountDown();
                            }
                        });
                closeServer.start();
            }
        }
        sendMessage("GAME " + str);
    }

    public int getIndex(Object[] arr, Object o){
        for(int i = 0; i < arr.length; i++){
            if(arr[i].equals(o)) return i;
        }
        return -1;
    }

    public String listToString(ArrayList<Client> a){
        String clientString = "";
        for(int i = 0; i < a.size(); i++){
            clientString += a.get(i).toString() + "|";
        }
        return a.size() + ">>" + clientString;
    }

    public void send(Client a, String pMessage){
        server.send(a.ip, a.port, pMessage);
    }

    private int searchIndex(ArrayList list, Object a){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).equals(a) ) return i;
        }
        return -1;
    }

    private int searchIndex2(ArrayList<Game> list, Game a){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).equals(a) ) return i;
        }
        return -1;
    }

    private void warte(int a){
        try{
            Thread.sleep(a); 
        }
        catch (InterruptedException ie){
            ie.printStackTrace();
        }
    }
    
    public Client getClient(int pNum){
        if(pNum >= clients.length || pNum < 0 ) return null;
        return clients[pNum];
    }
    
    public Client[] getClients(){
        return clients;
    }
    
    @Override
    public String toString(){
        String temp = "";
        
        return "";
    }
}
