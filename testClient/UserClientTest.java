package testClient;


public class UserClientTest{
    private UserClient userClient;
    
    public UserClientTest(){
        userClient = new UserClient(null, "localhost", 55555);
        userClient.setProcessingMessages(false);
        
        while(! userClient.hasConnected()){
            warte(10);
        }
        userClient.send("CONNECT SEARCHGAME");
    }
    
    public void warte(int time){
        try{
            Thread.sleep(time);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
