package assetLoader;

public class FunctionLoader{
    public FunctionLoader(){
        
    }
    
    public static void warte(int pTime){
        if(pTime < 0) return;
        try{
            Thread.sleep(pTime);
        }
        catch (InterruptedException ie){
            ie.printStackTrace();
        }
    }
    public static void warte(long pTime){
        if(pTime < 0) return;
        try{
            Thread.sleep(pTime);
        }
        catch (InterruptedException ie){
            ie.printStackTrace();
        }
    }
}