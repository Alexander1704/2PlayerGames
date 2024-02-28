package assetLoader;
import java.util.ArrayList;


/**
 * Write a description of class Tester here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Tester
{
    // instance variables - replace the example below with your own
    private int x;

    /**
     * Constructor for objects of class Tester
     */
    public Tester()
    {
        ArrayList<String> test = new ArrayList<String>();
        test.add("hallo welt0");
        test.add("hallo welt1");
        test.add("hallo welt2");
        test.add("hallo welt3");
        
        String listString = FunctionLoader.listToString(test);
        System.out.println(listString);
        
        test = FunctionLoader.stringToList(listString);
        FunctionLoader.printList(test);
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
