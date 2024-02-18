package assetLoader;

public class FunctionLoader{
    public FunctionLoader(){
        
    }
    
    public void warte(int pTime){
        try{
            Thread.sleep(pTime);
        }
        catch (InterruptedException ie){
            ie.printStackTrace();
        }
    }
}