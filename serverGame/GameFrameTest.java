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
        gameFrame.setPlayer(0, "Python");
        gameFrame.setPlayer(1, "Teleporter");
        gameFrame.show();
        gameFrame.start();
        
        System.out.println(gameFrame.getPlayer1().getCharacter());
    }
}
