package server;
import java.util.ArrayList;


public class Test{
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
    private class Game{
        Client[] player;
        ArrayList<Client> viewer;
        Game(Client clientA, Client clientB){
            player = new Client[2];
            player[0] = clientA;
            player[1] = clientB;
            
            viewer = new ArrayList<Client>();
        }
        
        public Client[] getPlayers(){
            return player;
        }
        public void addViewer(Client a){
            if(! viewer.contains(a) && !plays(a)) viewer.add(a);
        }
        public Client getOtherPlayer(Client a){
            if(!plays(a)) return null;
            if(a.equals(player[0])) return player[1];
            if(a.equals(player[1])) return player[0];
            return null;
        }
        public boolean plays(Client a){
            if(player[0].equals(a) || player[1].equals(a)) return true;
            return false;
        }
        public boolean isEmpty(){
            return player[0] == null && player[1] == null;
        }
        public void remove(){
            removeGame(this);
        }
        
        @Override
        public boolean equals(Object o){
            if(o instanceof Client){
                Client cl = (Client) o;
                if(cl.equals(player[0]) || cl.equals(player[1])) return true;
            }
            if(o instanceof Game){
                Game ga = (Game) o;
                if(ga.equals(player[0]) || ga.equals(player[1])) return true;
            }
            return false;
        }
    }
    Client a = new Client("127.0.0.1", 62394);
    Client b = new Client("127.0.0.1", 62395);
    
    ArrayList<Game> gameList = new ArrayList<Game>();
    Game c = new Game(a, b);
    public Test(){
        gameList.add(c);
        int index = gameList.indexOf(a);
        System.out.println(index);
        gameList.get(0).remove();
        System.out.println(gameList.size());
    }
    
    public void removeGame(Game a){
        gameList.remove(a);
    }
}
