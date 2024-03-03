package server;
import java.util.ArrayList;

public class Client{
    public final String ip;
    public final int port;
    private String name;
    private Game game;
    
    public Client(String pIP, int pPort){
        ip = pIP;
        port = pPort;
        name = "";
        game = null;
    }

    public String getIp(){
        return ip;
    }
    
    public int getPort(){
        return port;
    }
    
    public void setName(String pName){
        name = pName;
    }
    
    public String getName(){
        return name;
    }
    
    public void setGame(Game pGame){
        this.game = pGame;
    }
    
    public Game getGame(){
        return this.game;
    }
    
    @Override 
    public boolean equals(Object o){
        if(o instanceof Client){
            Client cl = (Client) o;
            if (ip.equals(cl.ip) && port == cl.port) return true;
            return false;
        }
        if(o instanceof Game){
            Game g = (Game) o;
            Client[] gameClients = g.getClients();
            if(gameClients == null) return false;
            for(int i = 0; i < gameClients.length; i++){
                if(gameClients[i].equals(this)) return true;
            }
            return false;
        }
        return false;
    }

    @Override 
    public String toString(){
        return ip + ":" + port + "; " + name + "; " + (game != null);
    }
}
