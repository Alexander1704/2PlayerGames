package server;
import java.util.ArrayList;


public class Test{
    ArrayList<Client> test;
    
    public Test(){
        // test = new ArrayList<Client>();
        // test.add(new Client("localhost", 0));
        // test.add(new Client("localhost", 1));
        // test.add(new Client("localhost", 2));
        // test.add(new Client("localhost", 3));
        // test.add(new Client("localhost", 4));
        
        // System.out.println(test.indexOf(new Client("localhost", 0)));
        // System.out.println(test.indexOf(new Client("localhost", 1)));
        // System.out.println(test.indexOf(new Client("localhost", 2)));
        // System.out.println(test.indexOf(new Client("localhost", 3)))
        
        ArrayList<Game> gameList = new ArrayList<Game>();
        gameList.add(new Game(null, new Client("localhost", 0), new Client("localhost", 1)));
        gameList.add(new Game(null, new Client("localhost", 2), new Client("localhost", 3)));
        gameList.add(new Game(null, new Client("localhost", 4), new Client("localhost", 5)));
        // gameList.add(new Game(null, new Client("localhost", 6), new Client("localhost", 7)));
        // gameList.add(new Game(null, new Client("localhost", 8), new Client("localhost", 9)));
        
        System.out.println(gameList.indexOf(new Client("localhost", 0)));
        System.out.println(gameList.indexOf(new Client("localhost", 1)));
        System.out.println(gameList.indexOf(new Client("localhost", 2)));
        System.out.println(gameList.indexOf(new Client("localhost", 3)));
        System.out.println(gameList.indexOf(new Client("localhost", 4)));
        System.out.println(gameList.indexOf(new Client("localhost", 5)));
    }
}
