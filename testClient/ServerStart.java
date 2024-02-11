package testClient;


import server.*;

public class ServerStart{
    BroadcastServer server;
    
    public ServerStart(){
        server = new BroadcastServer(55555);
    }
    
    public BroadcastServer getServer(){
        return server;
    }
}
