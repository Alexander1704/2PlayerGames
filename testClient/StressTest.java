package testClient;

import assetLoader.*;

public class StressTest{
    UserClient[] userClients;
    public StressTest(int pClientNum, String pIp, int pPort){
        userClients = new UserClient[pClientNum];
        for(int i = 0; i < userClients.length; i++){
            userClients[i] = new UserClient(null, pIp, pPort);
            userClients[i].setProcessingMessages(false);
            userClients[i].send("CONNECT SETNAME DUMMY");
        }

        makeSmth();
    }
    public void makeSmth(){
        TickThread stressThread = new TickThread(60, new Runnable(){
                    @Override
                    public void run(){
                        for(int i = 0; i < userClients.length; i++){
                            if(userClients[i].hasConnected() && !userClients[i].inGame()){
                                FunctionLoader.warte(5000);
                                userClients[i].send("CONNECT SEARCHGAME");
                            }
                            if(userClients[i].inGame()){
                                userClients[i].send("USERINPUT jump");
                                userClients[i].send("USERINPUT ability");
                            }
                        }
                    }
                });
        stressThread.start();
    }
}