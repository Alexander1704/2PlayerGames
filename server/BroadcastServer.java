package server;

import java.util.ArrayList;
import java.awt.*;
import game.*;

public class BroadcastServer extends Server{
    private class Client{
        public String ip;
        public int port;
        public Client(String pIP, int pPort){
            ip = pIP;
            port = pPort;
        }

        @Override 
        public boolean equals(Object o){
            if(o instanceof Client){
                Client cl = (Client) o;
                if (ip.equals(cl.ip) && port == cl.port) return true;
            }
            return false;
        }

        @Override 
        public String toString(){
            return ip + ":" + port;
        }
    }
    private class Game implements MessageInterpreter{ 
        String serverState;
        Client[] clients;
        ArrayList<Client> viewer;
        GameFrame gameFrame;
        boolean closed, updatingThread;
        Game(Client clientA, Client clientB){
            closed = false;
            updatingThread = false;

            clients = new Client[2];
            clients[0] = clientA;
            clients[1] = clientB;

            viewer = new ArrayList<Client>();
            gameFrame = new GameFrame(this);

            serverState = "INIT";

            Thread spamThread = new Thread (new Runnable() {
                        public void run(){
                            while(! gameFrame.isInitialized()){
                                warte(10);
                            }
                            System.out.println("GAME WAS INIT");
                            while(true){
                                for(int i = 0; i < clients.length; i++){
                                    if(gameFrame.getPlayer(1 + i).isMoving()) sendMessage("GAME POSITION " + i + " " + gameFrame.getPlayer(1 + i).toString());
                                }
                                warte(1000/ 60);
                            }
                        }
                    });
            spamThread.start();
        }

        Game(Client[] clients){ 
            this(clients[0], clients[1]);
        }

        public void userInput(Client a, String input){
            int index = getIndex(clients, a);
            gameFrame.getUserInput(index, input); 
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

        // public Client getOtherPlayer(Client a){
        // // if(!plays(a)) return null;
        // // if(a.equals(player[0])) return player[1];
        // // if(a.equals(player[1])) return player[0];
        // // return null;
        // }

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
            gameFrame.dispose();
            closed = true;
            closeGame(this);
        }
        private synchronized void closeCountDown(){
            if(closed) return;
            for(int i = 11; i >= 0; i--){
                try{
                    sendMessage("GAME CLOSING " + i);
                    Thread.sleep(1000);  // wait for 1 s
                }
                catch (InterruptedException ie){
                    ie.printStackTrace();
                }
            }
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
                for(int i = 0; i< clients.length; i++){
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
            if(!closed && str.startsWith("HEALTH")){
                String[] temp = str.split(" ");
                if(Integer.parseInt(temp[2]) <= 0){
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
    }
    private ArrayList<Game> gamesList;
    private ArrayList<Client> clientList;
    private ArrayList<Client> waitingList;
    private boolean running;
    public BroadcastServer(int pPortNum){
        super(pPortNum);
        clientList = new ArrayList<Client>();
        gamesList = new ArrayList<Game>();
        waitingList = new ArrayList<Client>();
        System.out.println("Server gestartet auf Port " + pPortNum + ".");

        running = true;
        Thread searchGameThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(running){
                            if(waitingList.size() > 1){
                                gamesList.add(new Game(waitingList.get(0), waitingList.get(1)));
                                System.out.println("SERVER says a gme was found");
                                send(waitingList.get(0), "+GAME FOUND");
                                send(waitingList.get(1), "+GAME FOUND");
                                waitingList.remove(0);
                                waitingList.remove(0);
                            }
                            try{
                                Thread.sleep(10);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                });
        searchGameThread.start();
    }

    public void processNewConnection(String pClientIP, int pClientPort){
        System.out.println("!Neue Verbindung " + pClientIP + ":" + pClientPort);
        //Client der Liste hinzufügen
        clientList.add(new Client(pClientIP, pClientPort));
        send(pClientIP, pClientPort, "+SPIELER OK");
    }

    public void processMessage(String pClientIP, int pClientPort, String pMessage){
        System.out.println(">>" + pMessage);
        String[] message = pMessage.split(" ", 2);
        // send(pClientIP, pClientPort, pMessage);
        Client cl = new Client(pClientIP, pClientPort);
        switch(message[0]){
                //INFO for ip and port and also a test if the communication between client and server works

                // all INFO commands are implemented here
            case "INFO" -> {
                    String[] command = message[1].split(" ", 2);
                    switch(command[0]){
                        case "USER"-> send(pClientIP, pClientPort, "INFO USER " + pClientIP + " " + pClientPort);
                        case "WAITINGLIST"-> send(pClientIP, pClientPort, "INFO WAITINGLIST " + listToString(waitingList));
                        case "CLIENTS"-> send(pClientIP, pClientPort, "INFO CLIENTS " + listToString(clientList));
                        case "GAME" ->{
                                String[] parameter = command[1].split(" ", 2);
                                switch(parameter[0]){
                                    case "NUMBER" -> send(pClientIP, pClientPort, "INFO GAME NUMBER " + gamesList.size());
                                    case "WATCH" -> send(pClientIP, pClientPort, "INFO GAME WATCH" + gamesList.indexOf(new Client ("", 0)));
                                }
                            }
                    }

                }

                // all connect commands are implemented here
            case "CONNECT" ->{
                    String[] command = message[1].split(" ", 2);
                    switch(command[0]){
                        case "SEARCHGAME" -> {
                                if(! waitingList.contains(new Client(pClientIP, pClientPort))){
                                    waitingList.add(new Client(pClientIP, pClientPort));
                                }
                            }
                        case "EXITGAME" ->{
                                int gameIndex = searchIndex(gamesList, new Client(pClientIP, pClientPort));
                                if(gameIndex == -1) {
                                    System.out.println("game not found" + cl.toString());
                                    return;
                                }
                                gamesList.get(gameIndex).exit(cl);
                            }
                    }
                }

            case "GAME" -> {
                    String[] args = message[1].split(" ", 2);
                    switch(args[0]){
                        case "INIT" -> {
                                int gameIndex = searchIndex (gamesList, new Client(pClientIP, pClientPort));
                                if(gameIndex == -1) {
                                    System.out.println("game couldnt be initialized");
                                    return;
                                }
                                gamesList.get(gameIndex).setPlayer(cl, args [1]);
                            }
                    }
                }

                // case "USERINPUT"
            case "USERINPUT"->{
                    int gameIndex = searchIndex(gamesList, cl);
                    gamesList.get(gameIndex).userInput(cl, message[1]);
                }
            default -> {
                    // String err = "ERROR: Nachricht konnte nicht verarbeitet werden. ";
                    // err += "Bitte verwende einen der folgenden Befehle:\n";
                    // err += "- 'ECHO <Nachricht>' sendet die Nachricht an dich zurück.\n";
                    // err += "- 'BROADCAST <Nachricht>' sendet die Nachricht an alle Teilnehmer.\n";
                    // err += "- 'SEND <IP> <Nachricht>' sendet die Nachricht, falls möglich, an die IP.\n";
                    // send(pClientIP, pClientPort, err);
                    send(pClientIP, pClientPort, "ERR: " + pMessage);
                }
        }
        // // System.out.println(">>" + pClientIP + ":" + pClientPort + ":" + pMessage);
        // // send(pClientIP, pClientPort, pMessage);
    }

    public void processClosingConnection(String pClientIP, int pClientPort){
        System.out.println("!Abmeldung Client " + pClientIP + ":" + pClientPort);
        int index = searchIndex(waitingList, new Client(pClientIP, pClientPort));
        if(index != -1) waitingList.remove(index);
        index = searchIndex(gamesList, new Client(pClientIP, pClientPort));
        if(index != -1) gamesList.remove(index);
        clientList.remove(new Client(pClientIP, pClientPort));
    }

    public String listToString(ArrayList<Client> a){
        String clientString = "";
        for(int i = 0; i < a.size(); i++){
            clientString += a.get(i).toString() + "|";
        }
        return a.size() + ">>" + clientString;
    }

    public void send(Client a, String pMessage){
        send(a.ip, a.port, pMessage);
    }

    private void closeGame(Game a){;
        gamesList.remove(0);
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

    private void print(Object a){
        System.out.println(a);
    }

    private void warte(int a){
        try{
            Thread.sleep(a); 
        }
        catch (InterruptedException ie){
            ie.printStackTrace();
        }
    }
    
    public int getIndex(Object[] arr, Object o){
        for(int i = 0; i < arr.length; i++){
            if(arr[i].equals(o)) return i;
        }
        return -1;
    }
}
