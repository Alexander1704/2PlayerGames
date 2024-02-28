package serverGame;


public class GameFrameTest{
    private class MessageInterpreterTest implements MessageInterpreter{
        MessageInterpreterTest(){
            
        }
        
        @Override
        public void interpretMessage(String str){
            
        }
    }
    
    GameFrame gameFrame;
    public GameFrameTest(){
        gameFrame = new GameFrame(new MessageInterpreterTest());
        gameFrame.setPlayer(0, "Player");
        gameFrame.setPlayer(1, "Player");
        gameFrame.show();
        gameFrame.start();
    }
}
