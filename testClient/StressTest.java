package testClient;

import assetLoader.*;

public class StressTest{
    UserClient[] userClients;
    public StressTest(int pClientNum, String pIp, int pPort){
        //login TestClients
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
                                FunctionLoader.warte(1000);
                                userClients[i].send("CONNECT SEARCHGAME");      //kann zu einer illegalState-Exception führen, dies ist aber nicht schlimm :)
                                while(!userClients[i].hasConnected()){
                                    FunctionLoader.warte(10);
                                }
                                // FunctionLoader.warte(4000);
                                // userClients[i].send("USERINPUT jump true");
                                // }else if(userClients[i].hasConnected()) userClients[i].send("USERINPUT ability true");
                            }
                        }
                    }
                });
        stressThread.start();
    }
}