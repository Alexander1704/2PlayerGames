package testClient;

import server.*;
public class ServerTest{
    BroadcastServer server;
    UserClient a;
    UserClient b;
    ServerTest(){
        server = new BroadcastServer(55555);
        
        a = new UserClient(null, "localhost", 55555);
        a.setProcessingMessages(false);
        b = new UserClient(null, "localhost", 55555);
        b.setProcessingMessages(false);
        
        while(!a.hasConnected() || ! b.hasConnected()){
            warte(10);
        }
        a.send("CONNECT SEARCHGAME");
        b.send("CONNECT SEARCHGAME");
    }
    
    public void warte(int time){
        try{
            Thread.sleep(time);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
