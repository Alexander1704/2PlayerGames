package server;

import assetLoader.*;
import java.util.*;
import java.awt.*;
import serverGame.*;

public class BroadcastServer extends Server{
    private ArrayList<Client> clientList;
    private ArrayList<Client> waitingList;
    private ArrayList<Game> gamesList;
    public BroadcastServer(int pPortNum){
        super(pPortNum);
        clientList = new ArrayList<Client>(); 
        gamesList = new ArrayList<Game>();
        waitingList = new ArrayList<Client>();

        System.out.println("Server gestartet auf Port " + pPortNum + ".");

        TickThread searchGameThread = new TickThread(60, new Runnable() {
                    @Override
                    public void run() {
                        synchronized(waitingList){
                            if(waitingList.size() > 1){
                                gamesList.add(new Game(getServer(), waitingList.get(0), waitingList.get(1)));
                                System.out.println("+GAME : " + waitingList.get(0).toString() + " _ " + waitingList.get(1).toString());
                                send(waitingList.get(0), "+GAME FOUND");
                                send(waitingList.get(1), "+GAME FOUND");
                                waitingList.remove(0);
                                waitingList.remove(0);
                                System.out.println("There are " + waitingList.size() + " players left waiting and there are " + gamesList.size() + "games");
                            }
                        }
                    }
                });
        searchGameThread.start();
    }

    public BroadcastServer getServer(){
        return this;
    }

    public void processNewConnection(String pClientIP, int pClientPort){
        // System.out.println("!Neue Verbindung " + pClientIP + ":" + pClientPort);
        synchronized(clientList){
            clientList.add(new Client(pClientIP, pClientPort));
        }
        send(pClientIP, pClientPort, "+SPIELER OK"); 

        // System.out.println("There are " + clientList.size() + " clients using this server");
    }

    public void processMessage(String pClientIP, int pClientPort, String pMessage){
        String[] message = pMessage.split(" ", 2);
        Client client = new Client(pClientIP, pClientPort);
        // System.out.println(client.toString() + "; " + pMessage);
        switch(message[0]){
                //INFO for ip and port and also a test if the communication between client and server works

                // all INFO commands are implemented here
            case "INFO" -> {
                    message = message[1].split(" ", 2);
                    switch(message[0]){
                        case "USER"-> {
                                send(client , "INFO USER " + client.toString());
                            }
                        case "WAITINGLIST"-> {
                                synchronized(waitingList){
                                    send(client, "INFO WAITINGLIST " + FunctionLoader.listToString(waitingList));
                                }
                            }
                        case "CLIENTS"-> {
                                synchronized(clientList){
                                    send(client, "INFO CLIENTS " + FunctionLoader.listToString(clientList));
                                }
                            }
                        case "GAMES" ->{
                                message = message[1].split(" ", 2);
                                synchronized(gamesList){
                                    send(client, "INFO GAMES " + FunctionLoader.listToString(gamesList));
                                }
                            }
                    }

                }

                // all connect commands are implemented here
            case "CONNECT" ->{
                    message = message[1].split(" ", 2);
                    switch(message[0]){
                        case "SETNAME" -> {
                                synchronized(clientList){
                                    int clientIndex = clientList.indexOf(client);
                                    if(clientIndex == -1){
                                        send(client, "-ERR clientNotFound " + pMessage);
                                        return;
                                    }
                                    clientList.get(clientIndex).setName(message[1]);
                                }
                                send(client, "+OK SETNAME " + message[1]);
                            }
                        case "SEARCHGAME" -> {
                                synchronized(waitingList){
                                    if(! waitingList.contains(client)){
                                        synchronized(clientList){
                                            int clientIndex = clientList.indexOf(client);
                                            if(clientIndex == -1){
                                                send(client, "-ERR clientNotFound " + pMessage);
                                                return;
                                            } 
                                            synchronized(gamesList){
                                                if(! gamesList.contains(client))waitingList.add(clientList.get(clientIndex));
                                                else send(client, "-ERR illegalState " + pMessage + clientList.get(clientIndex).toString()); //Client ist noch in Spiel
                                            }

                                        }
                                    }
                                }

                            }
                        case "LEAVE" ->{
                                synchronized(gamesList){
                                    int gameIndex = gamesList.indexOf(client);
                                    if(gameIndex != -1) {
                                        gamesList.get(gameIndex).exit(client);
                                    }
                                }
                                synchronized(waitingList){
                                    int waitingIndex = waitingList.indexOf(client);
                                    if(waitingIndex != -1) {
                                        waitingList.remove(waitingIndex);
                                    }
                                }
                            }
                    }
                }

            case "GAME" -> {
                    message = message[1].split(" ", 2);
                    switch(message[0]){
                        case "INIT" -> {
                                synchronized(gamesList){
                                    int gameIndex = gamesList.indexOf(client);
                                    if(gameIndex == -1) {
                                        System.err.println("GameNotFound");
                                        return;
                                    }
                                    System.out.println("___ " + message[1]);
                                    gamesList.get(gameIndex).setPlayer(client, message [1]);
                                }
                            }
                    }
                }

                // case "USERINPUT"
            case "USERINPUT"->{
                    synchronized(gamesList){
                        message = message[1].split(" ", 2);
                        int gameIndex = gamesList.indexOf(client);
                        if(gameIndex != -1) {
                            gamesList.get(gameIndex).userInput(client, message [0], Boolean.parseBoolean(message[1]));
                        }
                    }
                }
            default -> {
                    send(client, "-ERR commandNotFound " + pMessage);
                }
        }
    }

    public void processClosingConnection(String pClientIP, int pClientPort){
        System.out.println("!Abmeldung Client " + pClientIP + ":" + pClientPort);

        processMessage(pClientIP, pClientPort, "CONNECT LEAVE");
        synchronized(clientList){
            int clientIndex = clientList.indexOf(new Client(pClientIP, pClientPort));
            if(clientIndex != -1){
                clientList.remove(clientIndex);
            }
        }
    }

    public void send(Client pClient, String pMessage){
        send(pClient.ip, pClient.port, pMessage);
    } 

    public void closeGame(Game pGame){
        synchronized(gamesList){
            int gameIndex = gamesList.indexOf(pGame);
            if(gameIndex == -1) {
                System.err.println("Game: " + pGame.toString() + " could not be find and removed");
            }
            gamesList.remove(gameIndex);
        }
    } 

    public int getIndex(Object[] arr, Object o){
        for(int i = 0; i < arr.length; i++){
            if(arr[i].equals(o)) return i;
        }
        return -1;
    }
}
