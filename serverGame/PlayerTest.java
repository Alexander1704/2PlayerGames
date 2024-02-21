package serverGame;

import assetLoader.*;

/**
 * Write a description of class PlayerTest here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class PlayerTest
{
    private class MI implements MessageInterpreter{
        public void interpretMessage(String str){
            System.out.println(str);
        }
    }
    // instance variables - replace the example below with your own
    private int x;

    /**
     * Constructor for objects of class PlayerTest
     */
    public PlayerTest()
    {
        // initialise instance variables
        // x = 0;
        // GameFrame a = new GameFrame(new MI());
        // // Player player = new Player(a, "Player", 1);
        // a.setPlayer(1, "Player");
        // a.setPlayer(0, "Player");
        
        
        TickThread test = new TickThread(1, new Runnable(){
            public void run(){
                System.out.println("test1");
            }
        });
        test.start();
        
        TickThread test2 = new TickThread(1, new Runnable(){
            public void run(){
                System.out.println("test2");
            }
        });
        test2.start();
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  y  a sample parameter for a method
     * @return    the sum of x and y
     */
    public int sampleMethod(int y)
    {
        // put your code here
        return x + y;
    }
}
